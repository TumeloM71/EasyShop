package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.data.OrderLineItemDao;
import org.yearup.models.*;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {

    OrderLineItemDao orderLineItemDao;

    @Autowired
    public MySqlOrderDao(DataSource dataSource, OrderLineItemDao orderLineItemDao) {

        super(dataSource);
        this.orderLineItemDao = orderLineItemDao;
    }

    @Override
    public Order create(Profile profile, ShoppingCart shoppingCart) {

        Order order = new Order();
        String sql = """
                INSERT INTO orders
                (user_id, date, address, city, state, zip, shipping_amount)
                VALUES(?,?,?,?,?,?,?)
                """;

        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS))
        {

            statement.setInt(1,profile.getUserId());
            statement.setDate(2, Date.valueOf(LocalDate.now()));
            statement.setString(3, profile.getAddress());
            statement.setString(4, profile.getCity());
            statement.setString(5, profile.getState());
            statement.setString(6, profile.getZip());
            statement.setBigDecimal(7, shoppingCart.getTotal());

            int rows = statement.executeUpdate();
            System.out.println("Rows updated: "+rows);
            order.setUserId(profile.getUserId());
            order.setDate(Date.valueOf(LocalDate.now()));
            order.setAddress(profile.getAddress());
            order.setCity(profile.getCity());
            order.setState(profile.getState());
            order.setZip(profile.getZip());
            order.setCart(shoppingCart);
            order.setShippingAmount(shoppingCart.getTotal());
            ResultSet key = statement.getGeneratedKeys();
            if (key.next()){
                order.setOrderId(key.getInt(1));
            }

            for (ShoppingCartItem s: order.getCart().getItems().values()){
                OrderLineItem lineItem = new OrderLineItem(order.getOrderId(), s);
                orderLineItemDao.create(order.getOrderId(), lineItem);
            }


            return order;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
