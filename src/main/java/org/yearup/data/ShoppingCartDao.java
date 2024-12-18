package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here

    ShoppingCart addToCart(int userId, Product product);

    ShoppingCart updateItemQuantity(int userId ,int productId, int newQuantity);

    void clearCart(int userId);
}
