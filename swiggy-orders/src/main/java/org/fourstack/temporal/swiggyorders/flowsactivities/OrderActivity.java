package org.fourstack.temporal.swiggyorders.flowsactivities;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import org.fourstack.temporal.swiggyorders.dto.OrderDTO;

@ActivityInterface
public interface OrderActivity {

    @ActivityMethod
    OrderDTO placeOrder(OrderDTO dto);

    @ActivityMethod
    void setOrderAccepted();

    @ActivityMethod
    void setOrderPickUp();

    @ActivityMethod
    void setOrderDelivered();
}
