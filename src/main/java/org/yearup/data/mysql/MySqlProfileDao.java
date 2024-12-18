package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao
{
    public MySqlProfileDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public Profile create(Profile profile)
    {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate();

            return profile;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void update (int userId, Profile profile){
        String sql = """
                         UPDATE profiles
                         SET first_name = ?, last_name = ?, phone = ?,
                         email = ?, address = ?, city = ?, state = ?
                         zip = ?;
                     """;

        try( Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setString(1, profile.getFirstName());
            statement.setString(2, profile.getLastName());
            statement.setString(3, profile.getPhone());
            statement.setString(4, profile.getAddress());
            statement.setString(5, profile.getCity());
            statement.setString(6, profile.getState());
            statement.setString(7, profile.getZip());

            int rows = statement.executeUpdate();
            System.out.println("Rows updated");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Profile getByUserId (int userId){

        String sql = "SELECT * FROM profiles WHERE user_id = ?";

        try( Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return mapRow(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Profile mapRow(ResultSet resultSet) throws SQLException {

        Profile profile = new Profile();
        if (resultSet.next()){
            int id = resultSet.getInt(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            String phone = resultSet.getString(4);
            String email = resultSet.getString(5);
            String address = resultSet.getString(6);
            String city = resultSet.getString(7);
            String state = resultSet.getString(8);
            String zip = resultSet.getString(9);
            profile = new Profile(id,firstName,lastName,phone,email,address,city,state,zip);
        }
        return profile;
    }

}
