package org.fourstack.temporal.swiggyorders.converters;

import org.fourstack.temporal.swiggyorders.dto.ItemDTO;
import org.fourstack.temporal.swiggyorders.dto.OrderDTO;
import org.fourstack.temporal.swiggyorders.dto.PriceDTO;
import org.fourstack.temporal.swiggyorders.model.Item;
import org.fourstack.temporal.swiggyorders.model.Order;
import org.fourstack.temporal.swiggyorders.model.Price;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDaoToDtoConverter implements Converter<Order, OrderDTO> {
    @Override
    public OrderDTO convert(Order source) {
        OrderDTO target = new OrderDTO();
        target.setOrderId(source.getOrderId());
        target.setOrderStatus(source.getOrderStatus());
        target.setTotalPriceOnOrder(source.getTotalPriceOnOrder());
        target.setItems(getItems(source.getItems()));
        return target;
    }

    private List<ItemDTO> getItems(List<Item> items) {
        if (items == null)
            return Collections.emptyList();

        return items.stream()
                .map(item -> getItemDTO(item))
                .collect(Collectors.toList());
    }

    private ItemDTO getItemDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setItemName(item.getName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(getPriceDTO(item.getPrice()));
        return dto;
    }

    private PriceDTO getPriceDTO(Price price) {
        PriceDTO dto = new PriceDTO();
        if (price != null) {
            dto.setCurrentPrice(price.getCurrentPrice());
            dto.setTotalPrice(price.getTotalPrice());
        }
        return dto;
    }
}
