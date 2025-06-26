package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

// convert this class to a REST controller
@RestController
// only logged users should have access to these actions
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/cart")
public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;

    //Constructor injection
    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao){
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
    }

    // each method in this controller requires a Principal object as a parameter
    //Get the whole cart
    @GetMapping()
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            // get the currently logged username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the shoppingcartDao to get all items in the cart and return the cart
            return shoppingCartDao.getByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not get the cart",  e);
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PostMapping("/products/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartItem addProduct(@PathVariable int id,
                                       @RequestParam(name = "quantity", defaultValue = "1") int quantityParam,
                                       @RequestParam(name = "qty", required = false) Integer qtyAlias, Principal principal){
        int quantity = (qtyAlias != null) ? qtyAlias : quantityParam;
        //System.out.println("controller qty = " + quantity);
        if (quantity <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Quantity must be positive");
        }
        //Find how to get user using jwt and spring. We should be logged in already
        //our application know we logged in and how to get it from the jwt
        //And you should figure out to rturn a single cart item
        try{
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            shoppingCartDao.addProduct(userId, id, quantity);
            ShoppingCartItem shoppingCartItem = shoppingCartDao.getSingleItem(userId, id);
            if(shoppingCartItem == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found in cart");
            }
            return shoppingCartItem;
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Product not added", e);
        }

    }



    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateQuantity(@PathVariable int id, @RequestBody ShoppingCartItem body, Principal principal){
            //Check JSON
            if (body == null || body.getQuantity() <= 0){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be positive whole number");
            }
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            ShoppingCartItem shoppingCartItem = shoppingCartDao.getSingleItem(userId, id);
            if (shoppingCartItem == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart has no product " + id);
            }
            try{
                shoppingCartDao.updateQuantity(userId,id, body.getQuantity());
            }

        catch (RuntimeException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error updating the cart" + e);
        }
    }


    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Principal principal){
        try{
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            shoppingCartDao.delete(userId);
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error deleting the cart" , e);
        }
    }
    //Delete single cart item
    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable int id, Principal principal) {
        int userId = userDao.getByUserName(principal.getName()).getId();
        shoppingCartDao.deleteItem(userId, id);
    }

}
