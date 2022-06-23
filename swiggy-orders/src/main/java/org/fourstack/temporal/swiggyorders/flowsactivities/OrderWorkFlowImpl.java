package org.fourstack.temporal.swiggyorders.flowsactivities;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class OrderWorkFlowImpl implements OrderWorkFlow {
    private final RetryOptions retryOptions = RetryOptions.newBuilder()
            .setInitialInterval(Duration.ofSeconds(1))
            .setMaximumInterval(Duration.ofSeconds(100))
            .setBackoffCoefficient(2)
            .setMaximumAttempts(50000).build();

    private final ActivityOptions activityOptions = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(30))
            .setRetryOptions(retryOptions).build();

    private final OrderActivity activity = Workflow.newActivityStub(OrderActivity.class, activityOptions);

    public boolean isOrderConfirmed = false;
    public boolean isOrderPickedUp = false;
    public boolean isOrderDelivered = false;

    @Override
    public void startApprovalWorkFlow() {
        log.info("OrderWorkFlow: Order placement Started.");
        activity.placeOrder();

        log.info("OrderWorkFlow: Waiting for Restaurant to confirm your order.");
        Workflow.await(() -> isOrderConfirmed);

        log.info("OrderWorkFlow: Please wait till we assign a delivery executive");
        Workflow.await(() -> isOrderPickedUp);

        Workflow.await(() -> isOrderDelivered);
    }

    @Override
    public void orderAcceptedSignal() {
        activity.setOrderAccepted();
        this.isOrderConfirmed = true;
    }

    @Override
    public void orderPickUpSignal() {
        activity.setOrderPickUp();
        this.isOrderPickedUp = true;
    }

    @Override
    public void orderDeliveredSignal() {
        activity.setOrderDelivered();
        this.isOrderDelivered = true;
    }
}
