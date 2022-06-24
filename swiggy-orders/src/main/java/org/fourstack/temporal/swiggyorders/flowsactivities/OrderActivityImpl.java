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
    public OrderDTO placeOrder(OrderDTO dto) {
        log.info("OrderActivity: Order has been placed successfully");
        calculateTotalPrice(dto);
        return dto;
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
        }
        return item;
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
