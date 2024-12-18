package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    ProductDao productDao;

    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {

        ShoppingCart cart = new ShoppingCart();
        String sql = "SELECT * FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1, userId);
            ResultSet row = statement.executeQuery();
            while (row.next()) {
                ShoppingCartItem item = mapRow(row);
                cart.add(item);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cart;
    }

    @Override
    public ShoppingCart addToCart(int userId, Product product) {

        int quantity = 1;
        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity) " +
                    "VALUES(?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1,userId);
            statement.setInt(2,product.getProductId());
            statement.setInt(3,quantity);

            doUpdate(statement);
            return this.getByUserId(userId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ShoppingCart updateItemQuantity(int userId, int productId, int newQuantity) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
           statement.setInt(1,newQuantity);
           statement.setInt(2,userId);
           statement.setInt(3,productId);

           doUpdate(statement);
           return this.getByUserId(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void doUpdate(PreparedStatement statement) throws SQLException {
        int rows = statement.executeUpdate();
        System.out.println("Rows update: "+ rows);
    }

    @Override
    public void clearCart(int userId) {

        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1,userId);
            doUpdate(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ShoppingCartItem mapRow(ResultSet row) throws SQLException {
        ShoppingCartItem item = new ShoppingCartItem();
        item.setProduct(productDao.getById(row.getInt(2)));
        item.setQuantity(row.getInt(3));

        return item;
    }
}
