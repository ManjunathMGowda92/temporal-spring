package org.fourstack.temporal.swiggyorders.flowsactivities;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface OrderWorkFlow {

    @WorkflowMethod
    void startApprovalWorkFlow();

    @SignalMethod
    void orderAcceptedSignal();

    @SignalMethod
    void orderPickUpSignal();

    @SignalMethod
    void orderDeliveredSignal();
}
