package org.fourstack.temporal.swiggyorders.flowsactivities;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface OrderActivity {

    @ActivityMethod
    void placeOrder();

    @ActivityMethod
    void setOrderAccepted();

    @ActivityMethod
    void setOrderPickUp();

    @ActivityMethod
    void setOrderDelivered();
}
