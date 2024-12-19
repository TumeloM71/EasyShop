package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.data.OrderDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Order;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("orders")
@CrossOrigin
public class OrderController {

    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private OrderDao orderDao;
    private ProfileDao profileDao;

    public OrderController(ShoppingCartDao shoppingCartDao, UserDao userDao, OrderDao orderDao, ProfileDao profileDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.profileDao = profileDao;
    }

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Order> create(Principal principal){
        String userName = principal.getName();
        User user = userDao.getByUserName(userName);
        int userId = user.getId();

        Profile profile = profileDao.getByUserId(userId);
        ShoppingCart cart = shoppingCartDao.getByUserId(userId);
        if (cart.getItems().isEmpty())
            return ResponseEntity.notFound().build();

        else {
            Order order = orderDao.create(profile, cart);
            shoppingCartDao.clearCart(userId);

            return ResponseEntity.ok(order);
        }
    }
}
