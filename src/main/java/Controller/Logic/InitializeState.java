package Controller.Logic;

import Controller.DBLogic.DBConnection;
import Model.*;
import Utils.Algorithms.FloydWarshall;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class InitializeState extends State {
    public static final String JSON_PATH = "src/fileUtils/config.json";
    private HashMap <String, Taula> getTaules () {
        try {
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new FileReader(JSON_PATH));
            ProgramConfig programConfig = gson.fromJson(br, ProgramConfig.class);
            ProgramConfig.setInstance(programConfig);
            HashMap <String, Taula> taules = DBConnection.getInstance(programConfig.getDbPath()).showTables();

            return taules;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DynamicMatrix initializeGraphAndListOfConnections (HashMap <String, Taula> taules, MatrixOfConnections matrixOfConnections) {
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

    private short[][] calculateMaximmumPaths (DynamicMatrix connectionsBetweenTables, BestPathsMatrix bestPathsMatrix) {
        FloydWarshall floydWarshall = new FloydWarshall(connectionsBetweenTables.getQuantitat());
        return floydWarshall.floydWarshall(connectionsBetweenTables, bestPathsMatrix);
    }

    @Override
    void doYourFunction(String string) throws IOException {
        /**
         * En aquest programa hi haurà tres estructures principals:
         * - matrixOfDirectlyConnections: Que serà una llista que contindrà per cada taula, els noms de les taules a les que aquesta pot accedir
         * - connectionBetweenTables: Què serà una matriu binaria d'adjacencia que representarà la distribució del graph
         * - dist: Què serà una matriu que contindrà el màxim número de taules que es pot abarcar d'una taula a una altra
         * - bestPathsMatrix: Es una matriu que contindrà les taules que engloben les distancies de dist.
         * - matrixOfPaths: Que serà una matriu que mostrarà el camí màxim obtingut
         */

        HashMap<String, Taula> taules = getTaules();
        MatrixOfConnections matrixOfDirectlyConnections = new MatrixOfConnections();
        DynamicMatrix connectionsBetweenTables = initializeGraphAndListOfConnections(taules,matrixOfDirectlyConnections);

        BestPathsMatrix bestPathsMatrix = new BestPathsMatrix(connectionsBetweenTables.getQuantitat());
        short [][] dist = calculateMaximmumPaths(connectionsBetweenTables,bestPathsMatrix);

        TablesData.getInstance().setMatrixOfDirectlyConnections(matrixOfDirectlyConnections);
        TablesData.getInstance().setBestPathsMatrix(bestPathsMatrix);
        TablesData.getInstance().setDist(dist);
        TablesData.getInstance().setTaules(taules);
        context.changeState(new FilterTables());
        context.doStateFunction(null);
    }
}
