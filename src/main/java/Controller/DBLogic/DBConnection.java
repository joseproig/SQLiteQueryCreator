package Controller.DBLogic;

import Model.Columna;
import Model.Taula;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;
    private String pathFile;

    private DBConnection(String pathFile) {
        this.pathFile = pathFile;
        connection = null;
        try
        {
            System.out.println(pathFile);
            // Obrim la base de dades introduida per l'usuari
            connection = DriverManager.getConnection("jdbc:sqlite:" + pathFile);
        }
        catch(SQLException e)
        {
            // Aquest missatge indica que possiblement no s'ha trobat el fitxer especificat
            System.err.println(e.getMessage());
        }

    }

    public static DBConnection getInstance(String pathFile) {
        if (instance == null) {
            instance = new DBConnection(pathFile);
        }
        return instance;
    }

    public HashMap<String, Taula> showTables (){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%'");

            HashMap<String, Taula> taules = new HashMap<String, Taula>();

            int id = 0;
            while(rs.next()) {
                //tables.add(rs.getString("name"));
                String name = rs.getString("name");
                List<Columna> fks = new ArrayList<Columna>();
                HashMap<String, Columna> columnes = describeTable(name,fks);
                taules.put(name,new Taula(id++,name,columnes,fks));
            }

            return taules;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throwables.getSQLState();
        }
        return null;
    }

    private HashMap<String, Columna> describeTable (String table, List<Columna> fkRef) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("pragma table_info('" + table + "')");

            HashMap<String, Columna> columnes = new HashMap<String, Columna>();

            while(rs.next()) {
                Columna columna = new Columna();
                columna.setColumnName(rs.getString("name"));

                columnes.put(columna.getColumnName(),columna);
            }

            ResultSet rs2 = statement.executeQuery("pragma foreign_key_list('" + table + "')");
            while(rs2.next()) {
                String clau = rs2.getString("from");

                Columna columna = columnes.get(clau);
                columnes.remove(clau);
                columna.setTableReference(rs2.getString("table"));
                columna.setColumnReference(rs2.getString("to"));

                fkRef.add(columna);
            }

            return columnes;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


}
