package com.krech.botv3;

import java.sql.*;

public class JDBCExample {

    static void selectData () throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "Creeper_Z1")) {
            try (Statement stmt = con.createStatement()) {
                // use stmt here
                String selectSql = "SELECT * FROM brand";
                try (ResultSet resultSet = stmt.executeQuery(selectSql)) {
                    while (resultSet.next()){
                        System.out.println(resultSet.getInt("id") + " " + resultSet.getInt("name1"));

                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }




    }

    public static void main(String[] args) throws ClassNotFoundException {
        selectData();
    }



}
