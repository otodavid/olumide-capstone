package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.*;
import org.yearup.models.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/checkout")
@PreAuthorize("hasRole('ROLE_USER')")
public class OrderController {
    private final ShoppingCartDao cartDao;
    private final ProfileDao profileDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final UserDao userDao;

    @Autowired
    public OrderController(ShoppingCartDao cartDao, ProfileDao profileDao, OrderDao orderDao, OrderLineItemDao orderLineItemDao, UserDao userDao) {
        this.cartDao = cartDao;
        this.profileDao = profileDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.userDao = userDao;
    }

//    @PostMapping()
//    @ResponseStatus(HttpStatus.CREATED)
//    public Order checkout(Principal principal){
//        // get the currently logged username
//        String userName = principal.getName();
//        // find database user by userId
//        User user = userDao.getByUserName(userName);
//        int userId = user.getId();
//
//        ShoppingCart shoppingCart = cartDao.getByUserId(userId);
//
//        if(shoppingCart.getItems().isEmpty()){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
//        }
//
//        Profile profile = profileDao.getProfileByUserId(userId);
//        Order order = new Order(userId, LocalDateTime.now(), profile.getAddress(), profile.getCity(), profile.getState(), profile.getZip());
//        order = orderDao.create(order);
//
//        for(ShoppingCartItem item: shoppingCart.getItems()){
//            OrderLineItem orderLineItem = new OrderLineItem(
//            )
//        }
//
//    }
}
