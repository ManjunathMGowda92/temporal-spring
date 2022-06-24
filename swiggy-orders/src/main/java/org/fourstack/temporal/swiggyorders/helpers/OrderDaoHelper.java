package org.fourstack.temporal.swiggyorders.helpers;

import org.fourstack.temporal.swiggyorders.codetype.OrderStatus;
import org.fourstack.temporal.swiggyorders.dao.OrderRepository;
import org.fourstack.temporal.swiggyorders.dto.OrderDTO;
import org.fourstack.temporal.swiggyorders.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Component
public class OrderDaoHelper {
    @Autowired
    private OrderRepository repository;

    @Autowired
    private ConversionService orderDaoToDtoConverter;

    private ConversionService orderDtoToDaoConverter;

    public void createOrderInDB(OrderDTO dto) {
        var daoObj = orderDtoToDaoConverter.convert(dto, Order.class);
        repository.save(daoObj);
    }

    public OrderDTO updateOrderStatus(String orderId, OrderStatus status) {
        var optionalOrder = repository.findById(orderId);
        OrderDTO orderDto = null;
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setOrderStatus(status);
            repository.save(order);

            orderDto = orderDaoToDtoConverter.convert(order, OrderDTO.class);
        }
        return orderDto;
    }
}
