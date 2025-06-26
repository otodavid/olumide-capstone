package org.yearup.data.mysql;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {

    public MySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Order create(Order order){
        final String query = "INSERT INTO orders (user_id, date, address, city, state, zip, shipping_amount) VALUES (?, NOW(), ?,?,?,?,?";
        try (
                Connection connection = getConnection();
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
                ){
            ps.setInt(1, order.getUserId());
            ps.setString(2, order.getAddress());
            ps.setString(3, order.getCity());
            ps.setString(4, order.getState());
            ps.setString(5, order.getZip());
            ps.setBigDecimal(6, order.getShippingAmount());

            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    order.setOrderId(rs.getInt(1));
                }
            }
            return order;
        }
        catch (SQLException e){
            throw new RuntimeException("Error inserting order", e);
        }
    }
}
