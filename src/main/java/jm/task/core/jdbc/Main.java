package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;


import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException{
        // реализуйте алгоритм здесь
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();

        User user = new User("Ivan", "Lapchen", (byte) 34);
        User user1 = new User("Sasha", "Gulaev", (byte) 24);
        User user2 = new User("Sergey", "Sergeev", (byte) 12);
        User user3 = new User("Pavel", "Trubchinov", (byte) 13);

        userService.saveUser(user.getName(),user.getLastName(),user.getAge());
        userService.saveUser(user1.getName(),user1.getLastName(),user1.getAge());
        userService.saveUser(user2.getName(),user2.getLastName(),user2.getAge());
        userService.saveUser(user3.getName(),user3.getLastName(),user3.getAge());

        List<User> userList =  userService.getAllUsers();
        userList.forEach(e-> System.out.println(e.toString()));
/*
        userService.cleanUsersTable();
        userService.dropUsersTable();*/


    }
}
