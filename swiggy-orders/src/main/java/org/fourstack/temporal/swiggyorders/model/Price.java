package org.fourstack.temporal.swiggyorders.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Price {
    private BigDecimal currentPrice;
    private BigDecimal totalPrice;
}
