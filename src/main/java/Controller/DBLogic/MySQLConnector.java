package Controller.DBLogic;

import Model.MysqlConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class MySQLConnector {
    private Connection connect = null;

    private static MySQLConnector instance;


    public MySQLConnector(Connection connect) {
        this.connect = connect;
    }

    public static MySQLConnector getInstance() {
        if (instance == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println(MysqlConfig.getInstance().getMysql_user());
                // Setup the connection with the DB
                Connection connect = DriverManager.getConnection("jdbc:mysql://localhost?" + "user=" + MysqlConfig.getInstance().getMysql_user() +"&password=" + MysqlConfig.getInstance().getMysql_passwd());
                instance = new MySQLConnector(connect);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return instance;
    }



    public void deleteDatabasecreateNewDatabase(String nameOfDatabase) {
        Statement stmt = null;
        Statement stmt2 = null;
        try {
            stmt = connect.createStatement();
            String sql = "DROP DATABASE IF EXISTS " + nameOfDatabase;
            stmt.executeUpdate(sql);
            stmt.close();

            stmt2 = connect.createStatement();
            String sql2 = "CREATE DATABASE " + nameOfDatabase;
            stmt2.executeUpdate(sql2);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt2 != null) {
                try {
                    stmt2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }



    public void close() {
        try {

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

}