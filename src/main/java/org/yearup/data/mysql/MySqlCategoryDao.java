package org.yearup.data.mysql;

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
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            ResultSet rows = statement.executeQuery();
            while (rows.next()){
                categories.add(mapRow(rows));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        Category category;
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1,categoryId);
            ResultSet rows = statement.executeQuery();
            if (rows.next()){
                return mapRow(rows);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Category create(Category category)
    {
        String sql = "INSERT INTO categories (name, description) VALUES(?, ?)";
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1,category.getName());
            statement.setString(2,category.getDescription());
            doUpdate(statement);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void doUpdate(PreparedStatement statement) throws SQLException {
        int rows = statement.executeUpdate();
        System.out.println("Rows updated: "+rows);
    }

    @Override
    public void update(int categoryId, Category category)
    {
        String sql = "UPDATE categories SET name = ? , description = ? WHERE category_id = ?";
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
        {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);
            doUpdate(statement);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId)
    {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        try (Connection connection = this.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setInt(1,categoryId);
            doUpdate(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
