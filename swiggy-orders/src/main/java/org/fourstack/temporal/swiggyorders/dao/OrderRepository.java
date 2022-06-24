package org.fourstack.temporal.swiggyorders.dao;

import org.fourstack.temporal.swiggyorders.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
