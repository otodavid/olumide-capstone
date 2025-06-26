package org.yearup.data;

import org.yearup.models.Order;

public interface OrderDao {

    Order create(Order order);
    //Get order by id: Order getById(int orderId);
    //List order by user id: List<Order> listByUserid(int userId);
}
