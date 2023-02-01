package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    Connection connection = Util.getConnection();
    public UserDaoJDBCImpl() throws SQLException,ClassNotFoundException {
    }
    private static final String SQL_TABLE = "CREATE TABLE USERS" +
            "(`ID` INT NOT NULL AUTO_INCREMENT," +
            "`NAME` VARCHAR (100) NOT NULL," +
            "`LASTNAME` VARCHAR (150) NOT NULL," +
            "`AGE` INT NOT NULL," +
            "PRIMARY KEY (`ID`)" +
            ")";

    public void createUsersTable() {
        try(Statement statement = connection.createStatement()){
            statement.executeUpdate(SQL_TABLE);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public void dropUsersTable() {
        try(Statement statement = connection.createStatement()){
            String dropTableUsers = "DROP TABLE USERS";
            statement.executeUpdate(dropTableUsers);
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void saveUser(String name, String lastName, byte age){
        String userAddInSchema = null;
        if (name != null && lastName != null && age != 0) {
            userAddInSchema = "INSERT USERS (NAME, LASTNAME, AGE) \n" +
                    "VALUES ('" + name + "', '" + lastName + "', " + age + ")";
            try(Statement statement = connection.createStatement()){
                statement.executeUpdate(userAddInSchema);
            } catch (SQLException e){
                e.printStackTrace();
            }
        } else {
            System.err.println("В базу данных должны быть добавленны все параметры: имя, фамилия, возраст");
        }

    }

    public void removeUserById(long id)  {
        String removeUSerById = null;
        if (id!=0) {
            removeUSerById = "DELETE FROM USERS \n" +
                    "WHERE ID = " + id;
            try(Statement statement = connection.createStatement()){
                statement.executeUpdate(removeUSerById);
            } catch (SQLException e){
                e.printStackTrace();
            }
        } else {
            System.err.println("в поле ID не было переданно данных");
        }
        //statement.execute("SELECT * FROM INSTRUCTOR.USERS");
    }

    public List<User> getAllUsers() {
        String getAllUsers = "SELECT * FROM INSTRUCTOR.USERS";
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(getAllUsers)) {
            while (rs.next()) {
                User user1 = new User( rs.getString("NAME"), rs.getString("LASTNAME"), rs.getByte("AGE"));
                user1.setId(rs.getLong("ID"));
                users.add(user1);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        String removeAllUsers = "DELETE FROM INSTRUCTOR.USERS\n"+
                "WHERE ID > 0";
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate(removeAllUsers);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
