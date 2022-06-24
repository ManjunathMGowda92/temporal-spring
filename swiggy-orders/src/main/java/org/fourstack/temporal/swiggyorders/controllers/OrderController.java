package org.fourstack.temporal.swiggyorders.controllers;

import org.fourstack.temporal.swiggyorders.dto.OrderDTO;
import org.fourstack.temporal.swiggyorders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createOrder(orderDTO));
    }

    @PostMapping("/accept/{orderId}")
    public String acceptOrder(@PathVariable String orderId) {
        service.updateToOrderAccepted(orderId);
        return "Order Accepted.";
    }

    @PostMapping("/picked-up/{orderId}")
    public String orderPickedUp(@PathVariable String orderId) {
        service.updateToOrderPickedUp(orderId);
        return "Order Picked up.";
    }

    @PostMapping("/deliver/{orderId}")
    public String orderDelivered(@PathVariable String orderId) {
        service.updateToOrderDelivered(orderId);
        return "Order Delivered.";
    }
}
