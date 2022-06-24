package org.fourstack.temporal.swiggyorders.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fourstack.temporal.swiggyorders.dto.PriceDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String name;
    private int quantity;
    private Price price;
}
