package org.fourstack.temporal.swiggyorders.service;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.fourstack.temporal.swiggyorders.dto.ItemDTO;
import org.fourstack.temporal.swiggyorders.dto.OrderDTO;
import org.fourstack.temporal.swiggyorders.dto.PriceDTO;
import org.fourstack.temporal.swiggyorders.flowsactivities.OrderWorkFlow;
import org.fourstack.temporal.swiggyorders.helpers.OrderDaoHelper;
import org.fourstack.temporal.swiggyorders.util.OrderIdGenerationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.fourstack.temporal.swiggyorders.codetype.OrderStatus.*;
import static org.fourstack.temporal.swiggyorders.config.QueueConfigs.CUSTOMER_ORDER_QUEUE;

@Service
public class OrderService {

    @Autowired
    private WorkflowServiceStubs workflowServiceStubs;

    @Autowired
    private WorkflowClient workflowClient;

    @Autowired
    private OrderDaoHelper daoHelper;

    public OrderDTO createOrder(OrderDTO orderDTO) {
        orderDTO.setOrderId(OrderIdGenerationUtil.generateRandomId());
        calculateTotalPrice(orderDTO);
        placeOrder(orderDTO);
        orderDTO.setOrderStatus(CREATED);

        //save order to database
        daoHelper.createOrderInDB(orderDTO);
        return orderDTO;
    }

    public void placeOrder(OrderDTO orderDTO) {
        OrderWorkFlow workflow = createWorkFlowConnection(orderDTO.getOrderId());
        //WorkflowClient.start(() -> workflow.startApprovalWorkFlow(orderDTO));

        /*
         * WorkflowClient.start() - is used when the target Workflow doesn't accept any arguments.
         * WorkflowClient.execute() - is used when the target Workflow takes arguments.
         */
        WorkflowClient.execute(workflow::startApprovalWorkFlow, orderDTO);
    }

    public void updateToOrderAccepted(String workflowId) {
        OrderWorkFlow workflow = workflowClient.newWorkflowStub(OrderWorkFlow.class, workflowId);
        workflow.orderAcceptedSignal();

        daoHelper.updateOrderStatus(workflowId, ACCEPTED);
    }

    public void updateToOrderPickedUp(String workflowId) {
        OrderWorkFlow workFlow = workflowClient.newWorkflowStub(OrderWorkFlow.class, workflowId);
        workFlow.orderPickUpSignal();

        daoHelper.updateOrderStatus(workflowId, PICKED_UP);
    }

    public void updateToOrderDelivered(String workflowId) {
        OrderWorkFlow workFlow = workflowClient.newWorkflowStub(OrderWorkFlow.class, workflowId);
        workFlow.orderDeliveredSignal();

        daoHelper.updateOrderStatus(workflowId, DELIVERED);
    }

    private OrderWorkFlow createWorkFlowConnection(String id) {
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(CUSTOMER_ORDER_QUEUE)
                .setWorkflowId(id)
                .build();
        return workflowClient.newWorkflowStub(OrderWorkFlow.class, options);
    }

    private <T> T createWorkFlowConnection(String workflowId, String queueName, Class<T> type) {
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(queueName)
                .setWorkflowId(workflowId)
                .build();

        return workflowClient.newWorkflowStub(type, options);
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
            BigDecimal zero = BigDecimal.valueOf(0);
            price = new PriceDTO(zero, zero);
            item.setPrice(price);
        }
        return item;
    }
}
