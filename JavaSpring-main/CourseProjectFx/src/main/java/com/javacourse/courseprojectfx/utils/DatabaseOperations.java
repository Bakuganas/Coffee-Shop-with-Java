package com.javacourse.courseprojectfx.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOperations {
    public static Connection connectToDb() throws ClassNotFoundException {
        Connection conn = null;
        String DB_URL = "jdbc:mysql://localhost/lecture_jdbc";
        String USER = "root";
        String PASS = "";
        try {
            Class.forName("com.mysql.jdbc.cj.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException| ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    public List<String> getProducts(){
        List<String> resultList = new ArrayList<>();
        try {
            Connection connection = connectToDb();
            String sql = "SELECT * FROM product as p WHERE";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            String result = "";
            while (resultSet.next()){
                 result = resultSet.getInt(1) + " " + resultSet.getString(2) + " " + resultSet.getString("description");
                 resultList.add(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }



}
