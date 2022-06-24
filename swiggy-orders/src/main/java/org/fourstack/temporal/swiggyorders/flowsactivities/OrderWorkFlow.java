package org.fourstack.temporal.swiggyorders.flowsactivities;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.fourstack.temporal.swiggyorders.dto.OrderDTO;

@WorkflowInterface
public interface OrderWorkFlow {

    @WorkflowMethod
    void startApprovalWorkFlow(OrderDTO order);

    @SignalMethod
    void orderAcceptedSignal();

    @SignalMethod
    void orderPickUpSignal();

    @SignalMethod
    void orderDeliveredSignal();
}
