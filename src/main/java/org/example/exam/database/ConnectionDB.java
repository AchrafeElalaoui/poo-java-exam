package org.example.exam.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDB {
    private static Connection connection;
    public static Connection getConnection(){

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/rest","root","achraf.12");
        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }
}
