package org.fourstack.temporal.swiggyorders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceDTO {
    private BigDecimal currentPrice;
    private BigDecimal totalPrice;
}
