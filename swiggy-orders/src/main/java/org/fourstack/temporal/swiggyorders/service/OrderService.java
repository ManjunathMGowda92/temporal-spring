package org.fourstack.temporal.swiggyorders.service;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.workflow.Workflow;
import org.fourstack.temporal.swiggyorders.dto.ItemDTO;
import org.fourstack.temporal.swiggyorders.dto.OrderDTO;
import org.fourstack.temporal.swiggyorders.dto.PriceDTO;
import org.fourstack.temporal.swiggyorders.flowsactivities.OrderWorkFlow;
import org.fourstack.temporal.swiggyorders.util.OrderIdGenerationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.fourstack.temporal.swiggyorders.config.QueueConfigs.CUSTOMER_ORDER_QUEUE;

@Service
public class OrderService {

    @Autowired
    WorkflowServiceStubs workflowServiceStubs;

    @Autowired
    WorkflowClient workflowClient;

    public OrderDTO createOrder(OrderDTO orderDTO) {
        orderDTO.setOrderId(OrderIdGenerationUtil.generateRandomId());
        calculateTotalPrice(orderDTO);
        placeOrder(orderDTO);
        return orderDTO;
    }

    public void placeOrder(OrderDTO orderDTO) {
        OrderWorkFlow workflow = createWorkFlowConnection(orderDTO.getOrderId());
        WorkflowClient.start(() -> workflow.startApprovalWorkFlow(orderDTO));
    }

    public void updateToOrderAccepted(String workflowId) {
        OrderWorkFlow workflow = workflowClient.newWorkflowStub(OrderWorkFlow.class, workflowId);
        workflow.orderAcceptedSignal();
    }

    public void updateToOrderPickedUp(String workflowId) {
        OrderWorkFlow workFlow = workflowClient.newWorkflowStub(OrderWorkFlow.class, workflowId);
        workFlow.orderPickUpSignal();
    }

    public void updateToOrderDelivered(String workflowId) {
        OrderWorkFlow workFlow = workflowClient.newWorkflowStub(OrderWorkFlow.class, workflowId);
        workFlow.orderDeliveredSignal();
    }

    private OrderWorkFlow createWorkFlowConnection(String id) {
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(CUSTOMER_ORDER_QUEUE)
                .setWorkflowId(id)
                .build();
        return workflowClient.newWorkflowStub(OrderWorkFlow.class, options);
    }

    /**
     * Calculate total price on Order.
     *
     * @param dto Order Object
     */
    private void calculateTotalPrice(OrderDTO dto) {
        if (Objects.nonNull(dto)) {
            List<ItemDTO> items = dto.getItems();

            if (Objects.nonNull(items) && !items.isEmpty()) {
                Optional<BigDecimal> optionalTotal = items.stream()
                        .map(this::calculateItemPrice)
                        .map(item -> (Objects.nonNull(item.getPrice())) ? item.getPrice().getTotalPrice()
                                : BigDecimal.valueOf(0))
                        .reduce(BigDecimal::add);

                dto.setTotalPriceOnOrder(optionalTotal.orElse(BigDecimal.valueOf(0)));
            }
        }
    }

    /**
     * Calculate total price on each item by multiplying the current price
     * with quantity of Item.
     *
     * @param item {@link ItemDTO} object having item details (name, quantity and price)
     * @return ItemDTO Object
     */
    private ItemDTO calculateItemPrice(ItemDTO item) {
        PriceDTO price = item.getPrice();
        int quantity = item.getQuantity();
        if (Objects.nonNull(price)) {
            BigDecimal currentPrice = price.getCurrentPrice();
            price.setTotalPrice(currentPrice.multiply(BigDecimal.valueOf(quantity)));
        } else {
            price = new PriceDTO();
            item.setPrice(price);
        }
        return item;
    }
}
