package org.fourstack.temporal.swiggyorders.flowsactivities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderActivityImpl implements OrderActivity{
    @Override
    public void placeOrder() {
        log.info("OrderActivity: Order has been placed successfully");
    }

    @Override
    public void setOrderAccepted() {
        log.info("OrderActivity: Restaurant accepted your order.");
    }

    @Override
    public void setOrderPickUp() {
        log.info("OrderActivity: Order has been picked up.");
    }

    @Override
    public void setOrderDelivered() {
        log.info("OrderActivity: Order Delivered.");
    }
}
