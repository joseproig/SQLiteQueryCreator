package Controller.Logic;

import Controller.DBLogic.DBConnection;
import Model.*;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Logic {
    public static final String JSON_PATH = "src/fileUtils/config.json";
    public HashMap <String, Taula> getTaules () {
        try {
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new FileReader(JSON_PATH));
            ProgramConfig programConfig = gson.fromJson(br, ProgramConfig.class);

            HashMap <String, Taula> taules = DBConnection.getInstance(programConfig.getDbPath()).showTables();

            return taules;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DynamicMatrix initializeGraphAndListOfConnections (HashMap <String, Taula> taules, MatrixOfConnections matrixOfConnections) {
        Iterator<String> it = taules.keySet().iterator();
        DynamicMatrix graph = new DynamicMatrix(taules.size());


        matrixOfConnections.initializeListOfConnections(taules.size());

        while(it.hasNext()){
            String nomTaula = it.next();
            Taula taula = taules.get(nomTaula);
            List<Columna> fks = taula.getForeignKeys();


            //Copiem a la llista tots els ids de les taules a les que fa referencia
            int i = 0;
            while (i < fks.size()) {
                String tableRef = fks.get(i).getTableReference();
                matrixOfConnections.setNewRelation(taula.getId(),taules.get(tableRef).getId());
                i++;
            }

            //Creem les relacions de les FK al graph original
            graph.insertarTaula(taula.getId(),matrixOfConnections.getRelationsOfATable(taula.getId()));

        }

        //Convertim el graph creat en bidireccional (de tal forma que una relació de FK, es convertirà en bidireccional)
        graph.convertirEnBidireccional();
        matrixOfConnections.makeItBidirectional();
        return graph;
    }
}
