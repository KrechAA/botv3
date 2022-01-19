package com.krech.botv3.config;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector implements Closeable {

   private final Connection connection;


    public DbConnector(String urlDb, String userDb, String passDb) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        try {
            connection = DriverManager.getConnection(urlDb, userDb, passDb);

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
