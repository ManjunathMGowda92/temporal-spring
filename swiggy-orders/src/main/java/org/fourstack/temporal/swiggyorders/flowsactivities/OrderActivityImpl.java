package org.fourstack.temporal.swiggyorders.flowsactivities;

import lombok.extern.slf4j.Slf4j;
import org.fourstack.temporal.swiggyorders.dto.ItemDTO;
import org.fourstack.temporal.swiggyorders.dto.OrderDTO;
import org.fourstack.temporal.swiggyorders.dto.PriceDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class OrderActivityImpl implements OrderActivity {
    @Override
    public void placeOrder(OrderDTO dto) {
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
