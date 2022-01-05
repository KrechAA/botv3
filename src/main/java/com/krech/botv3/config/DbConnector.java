package com.krech.botv3.config;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector implements Closeable {

   private final Connection connection;

    public DbConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "Creeper_Z1");

        } catch (SQLException throwables) {
            throw new IllegalStateException(throwables);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
