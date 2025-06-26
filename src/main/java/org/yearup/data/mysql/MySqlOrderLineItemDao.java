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
    public void create(int orderId, OrderLineItem orderLineItem){
        String query = "INSERT INTO order_line_items (order_id, product_id, sales_price, quantity, discount) VALUES (?,?,?,?,?)";

        try(
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(query)
                ){
            ps.setInt(1, orderId);
            ps.setInt(2, orderLineItem.getProductId());
            ps.setBigDecimal(3, orderLineItem.getSalesPrice());
            ps.setInt(4, orderLineItem.getQuantity());
            ps.setBigDecimal(5, orderLineItem.getDiscount());

            ps.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException("Error inserting line item", e);
        }
    }
}
