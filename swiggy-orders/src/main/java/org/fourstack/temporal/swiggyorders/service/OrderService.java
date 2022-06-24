package org.fourstack.temporal.swiggyorders.service;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.workflow.Workflow;
import org.fourstack.temporal.swiggyorders.dto.OrderDTO;
import org.fourstack.temporal.swiggyorders.flowsactivities.OrderWorkFlow;
import org.fourstack.temporal.swiggyorders.util.OrderIdGenerationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.fourstack.temporal.swiggyorders.config.QueueConfigs.CUSTOMER_ORDER_QUEUE;

@Service
public class OrderService {

    @Autowired
    WorkflowServiceStubs workflowServiceStubs;

    @Autowired
    WorkflowClient workflowClient;

    public OrderDTO createOrder(OrderDTO orderDTO) {
        orderDTO.setOrderId(OrderIdGenerationUtil.generateRandomId());
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
}
