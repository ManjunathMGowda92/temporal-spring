package org.fourstack.temporal.swiggyorders.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fourstack.temporal.swiggyorders.codetype.OrderStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order")
public class Order {
    @Id
    private String orderId;
    private List<Item> items;
    private BigDecimal totalPriceOnOrder;
    private OrderStatus orderStatus;
}
