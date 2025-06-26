package org.yearup.data.mysql;

import com.mysql.cj.protocol.Resultset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    private DataSource dataSource;

    @Autowired
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public List<Category> getAllCategories()
    {
        // get all categories
        String query = "SELECT * FROM categories";
        List<Category>categories = new ArrayList<>();
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet resultSet = ps.executeQuery()
                ){
            while (resultSet.next()){
                Category category = mapRow(resultSet);
                categories.add(category);
            }

        } catch (SQLException e){
            System.out.println("database error: " + e.getMessage());
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        // get category by id
        String query = "SELECT * FROM categories\n" +
                "WHERE category_id = ?";
        Category category = null;
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)
        ){
            ps.setInt(1,categoryId);
            try(
                    ResultSet resultSet = ps.executeQuery()
                    ){
                while (resultSet.next()){
                    category = new Category(
                            resultSet.getInt("category_id"),
                            resultSet.getString("name"),
                            resultSet.getString("description")
                    );
                }
            }
        } catch (SQLException e){
            System.out.println("database error " + e.getMessage());
        }
        return category;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        String query = "INSERT INTO categories(category_id, name, description) VALUES(?,?,?)";
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
                ){
            ps.setInt(1,category.getCategoryId());
            ps.setString(2,category.getName());
            ps.setString(3, category.getDescription());
            ps.executeUpdate();
            try(
                    ResultSet resultSet = ps.getGeneratedKeys()
                    ){
                resultSet.next();
                category.setCategoryId(resultSet.getInt(1));
            }
        } catch (SQLException e){
            System.out.println("database error " + e.getMessage());
        }
        return category;
    }

    @Override
    public boolean update(int categoryId, Category category)
    {
        // update category
        String query = "UPDATE categories SET name = ? WHERE categories_id = ? ";
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)
                ){
            ps.setString(1,category.getName());
            ps.setInt(2, categoryId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
        catch (SQLException e){
            System.out.println("update error in our database: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int categoryId)
    {
        // delete category
        String query = "DELETE FROM categories WHERE category_id = ?";
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(query)
                ){
            ps.setInt(1, categoryId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
        catch (SQLException e){
            System.out.println("delete error: " + e.getMessage());
            return false;
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
