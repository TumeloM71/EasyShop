package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderLineItemDao;
import org.yearup.models.OrderLineItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class MySqlOrderLineItemDao extends MySqlDaoBase implements OrderLineItemDao {

    public MySqlOrderLineItemDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void create(int orderId, OrderLineItem orderLineItem) {
        String sql = """
                    INSERT INTO order_line_items (order_id, product_id,sales_price,quantity,discount)
                    VALUES(?,?,?,?,?)
                    """;

        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1,orderLineItem.getOrderId());
            statement.setInt(2, orderLineItem.getLineItem().getProductId());
            statement.setBigDecimal(3,orderLineItem.getCost());
            statement.setInt(4,orderLineItem.getLineItem().getQuantity());
            statement.setBigDecimal(5, orderLineItem.getLineItem().getDiscountPercent());

            int rows = statement.executeUpdate();
            System.out.println("Rows updated: "+rows);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
