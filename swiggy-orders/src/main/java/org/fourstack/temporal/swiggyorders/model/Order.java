package org.fourstack.temporal.swiggyorders.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fourstack.temporal.swiggyorders.codetype.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String orderId;
    private List<Item> items;
    private BigDecimal totalPriceOnOrder;
    private OrderStatus orderStatus;
}
