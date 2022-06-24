package org.fourstack.temporal.swiggyorders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private String itemName;
    private int quantity;
    private PriceDTO price;
}
