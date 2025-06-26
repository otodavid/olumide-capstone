package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{   //used to get cart for the user
    ShoppingCart getByUserId(int userId);
    // add product
    void addProduct (int UserId, int productId, int qty);
    //increase quantity when we put item in the cart
    void updateQuantity(int userId, int productId, int Quantity);
    // delete the whole cart
    void delete(int userId);
    //get a single item from the cart
    ShoppingCartItem getSingleItem(int userId, int productId);
    //remove single item from cart
    void deleteItem(int userId, int productId);

}

