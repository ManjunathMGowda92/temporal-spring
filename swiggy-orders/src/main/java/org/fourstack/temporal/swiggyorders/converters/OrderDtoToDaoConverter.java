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
public class OrderDtoToDaoConverter implements Converter<OrderDTO, Order> {
    @Override
    public Order convert(OrderDTO source) {
        Order order = new Order();
        order.setOrderId(source.getOrderId());
        order.setOrderStatus(source.getOrderStatus());
        order.setTotalPriceOnOrder(source.getTotalPriceOnOrder());
        order.setItems(getItems(source.getItems()));
        return order;
    }

    private List<Item> getItems(List<ItemDTO> items) {
        if (items == null)
            return Collections.emptyList();

        return items.stream()
                .map(dto -> convertToItem(dto))
                .collect(Collectors.toList());
    }

    private Item convertToItem(ItemDTO dto) {
        Item item = new Item();
        item.setName(dto.getItemName());
        item.setQuantity(dto.getQuantity());
        item.setPrice(getPrice(dto.getPrice()));
        return item;
    }

    private Price getPrice(PriceDTO priceDTO) {
        Price price = new Price();
        price.setCurrentPrice(priceDTO.getCurrentPrice());
        price.setTotalPrice(priceDTO.getTotalPrice());
        return price;
    }
}
