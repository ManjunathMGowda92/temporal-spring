package org.fourstack.temporal.swiggyorders.controllers;

import org.fourstack.temporal.swiggyorders.dto.OrderDTO;
import org.fourstack.temporal.swiggyorders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder() {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createOrder());
    }

    @PostMapping("/start-flow/{orderId}")
    public String placeOrder(@PathVariable String orderId) {
        service.placeOrder(orderId);
        return "Order Placed";
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
