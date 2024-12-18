package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.Optional;

// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@RequestMapping("/cart")
@CrossOrigin
public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // each method in this controller requires a Principal object as a parameter
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the shoppingcartDao to get all items in the cart and return the cart
            return shoppingCartDao.getByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PostMapping("/products/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ShoppingCart> addToCart(@PathVariable int productId, Principal principal){

            try {
                String userName = principal.getName();
                User user = userDao.getByUserName(userName);
                int userId = user.getId();
                Product product = productDao.getById(productId);

                Optional<ShoppingCart> optionalShoppingCart = Optional.ofNullable(shoppingCartDao.getByUserId(userId));

                if (optionalShoppingCart.isPresent() && optionalShoppingCart.get().contains(productId)){
                    int newQuantity = optionalShoppingCart.get().get(productId).getQuantity() + 1;
                    return ResponseEntity.status(HttpStatus.CREATED).body((shoppingCartDao.updateItemQuantity(userId, productId, newQuantity)));
                }
                else
                    return ResponseEntity.status(HttpStatus.CREATED).body((shoppingCartDao.addToCart(userId, product)));

            }
            catch(Exception e)
            {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
            }

    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> updateItem(@RequestBody ShoppingCartItem item, Principal principal){

        try
        {
            ShoppingCart cart;
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            if (!shoppingCartDao.getByUserId(userId).contains(item.getProductId()))
                return ResponseEntity.notFound().build();
            else {
                cart = shoppingCartDao.updateItemQuantity(userId, item.getProductId(), item.getQuantity());
                return ResponseEntity.ok().build();
            }

        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> clearCart(Principal principal){

        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        int userId = user.getId();
        shoppingCartDao.clearCart(userId);
        return ResponseEntity.ok().build();
    }

}
