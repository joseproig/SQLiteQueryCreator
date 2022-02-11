package Controller.DBLogic;

import Model.MysqlConfig;
import Model.Taula;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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


    public void createNecessaryTablesForLogos(String nameOfDatabase) {
        Statement stmt2 = null;
        Statement stmt3 = null;
        Statement stmt4 = null;
        Statement stmt5 = null;
        Statement stmt6 = null;
        Statement stmt7 = null;
        try {
            stmt2 = connect.createStatement();
            String sql2 = "CREATE TABLE " + nameOfDatabase +".translation_relation_node_labels(id SERIAL PRIMARY KEY, relation_name TEXT, label TEXT)";
            stmt2.executeUpdate(sql2);
            stmt2.close();

            stmt3 = connect.createStatement();
            String sql3 = "CREATE TABLE " + nameOfDatabase +".translation_attribute_node_labels(id SERIAL PRIMARY KEY, relation_name TEXT, attribute_name TEXT, label TEXT)";
            stmt3.executeUpdate(sql3);
            stmt3.close();

            stmt4 = connect.createStatement();
            String sql4 = "CREATE TABLE " + nameOfDatabase +".translation_primary_relations(id SERIAL PRIMARY KEY, relation_name TEXT)";
            stmt4.executeUpdate(sql4);
            stmt4.close();

            stmt5 = connect.createStatement();
            String sql5 = "CREATE TABLE " + nameOfDatabase +".translation_heading_attributes(id SERIAL PRIMARY KEY, relation_name TEXT, attribute_name TEXT)";
            stmt5.executeUpdate(sql5);
            stmt5.close();

            stmt6 = connect.createStatement();
            String sql6 = "CREATE TABLE " + nameOfDatabase +".translation_edge_labels(id SERIAL PRIMARY KEY, src_relation TEXT, src_attribute TEXT, dest_relation TEXT, dest_attribute TEXT, edge_type TEXT, label TEXT)";
            stmt6.executeUpdate(sql6);
            stmt6.close();

            stmt7 = connect.createStatement();
            String sql7 = "CREATE TABLE " + nameOfDatabase +".translation_external_joins(id SERIAL PRIMARY KEY, src_relation TEXT, src_attribute TEXT, dest_relation TEXT, dest_attribute TEXT)";
            stmt7.executeUpdate(sql7);
            stmt7.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateLogosTables (String nameOfDatabase, HashMap<String, Taula> taules) {
        for(Map.Entry<String, Taula> entry : taules.entrySet()) {
            try {
                Statement stmt2 = connect.createStatement();
                String sql2 = null;
                if (!entry.getKey().substring(entry.getKey().length() - 1).equals("s")) {
                    sql2 = "INSERT INTO " + nameOfDatabase + ".translation_relation_node_labels (relation_name, label) VALUES ('" + entry.getKey() + "', '" + entry.getKey() + "s')";
                } else {
                    sql2 = "INSERT INTO " + nameOfDatabase + ".translation_relation_node_labels (relation_name, label) VALUES ('" + entry.getKey() + "', '" + entry.getKey() + "')";
                }
                stmt2.executeUpdate(sql2);
                stmt2.close();
            } catch (SQLException e) {
                e.printStackTrace();
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