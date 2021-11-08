package Controller.Logic;

import Controller.DBLogic.DBConnection;
import Model.*;
import Model.ParametersOfQuestion.FilterInSelect;
import Model.ParametersOfQuestion.ParametersConfig;
import Model.ParametersOfQuestion.Question;
import Model.ParametersOfQuestion.Structure;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitializeState extends State {
    public static final String JSON_PATH = "src/fileUtils/config.json";
    private HashMap <String, Taula> getTaules (HashMap<Integer,Taula> taulesById, int numOfQuestion) {
        try {
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new FileReader(JSON_PATH));
            ProgramConfig programConfig = gson.fromJson(br, ProgramConfig.class);

            ProgramConfig.setInstance(programConfig);
            //Guardem els parametres que ha de tenir la pregunta
            takeParametersOfQuestion();


            HashMap <String, Taula> taules = DBConnection.getInstance(programConfig.getDbPath()).showTables(taulesById);

            return taules;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void takeParametersOfQuestion () {
        String regularExpression = "(?<=\\{)(.*?)(?=\\})";
        Pattern pat = Pattern.compile(regularExpression);


        int i = 0;
        for (Question question: ProgramConfig.getInstance().getFilterParams().getQuestions()) {
            Matcher mat = pat.matcher(question.getQuestion());
            while (mat.find()) {
                String argument = mat.group(0);

                //Una vegada tenim l'interior del que hi ha als {} anem a veure que es.
                String [] splits = argument.split("_");
                int numberOfParts = splits.length;

                //String expressionOfType = "[a-zA-Z]+";
                //String type = getStringOfPattern(expressionOfType,splits[0]);
                switch (splits[0]) {
                    case "S":
                        ProgramConfig.getInstance().getFilterParams().getQuestions().get(i).getStructure().addColumnToSeeInSelect(splits[1]);
                        break;
                    case "F":
                        String typeOfFilter = null;
                        if (numberOfParts > 2) {
                            typeOfFilter = splits[2];
                        }
                        ProgramConfig.getInstance().getFilterParams().getQuestions().get(i).getStructure().addColumnToFilterInSelect(new FilterInSelect(splits[1],typeOfFilter));
                        break;
                    case "O":
                        ProgramConfig.getInstance().getFilterParams().getQuestions().get(i).getStructure().addColumnToOrderBy(splits[1]);
                        break;
                    default:
                        break;
                }
            }
            i++;
        }
    }

    private String getStringOfPattern (String expression, String stringToExtract) {
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(stringToExtract);
        matcher.find();
        return matcher.group(0);
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

        HashMap<Integer,Taula> taulesById = new HashMap<>();
        HashMap<String, Taula> taules = getTaules(taulesById, Integer.parseInt(string));
        MatrixOfConnections matrixOfDirectlyConnections = new MatrixOfConnections();
        DynamicMatrix connectionsBetweenTables = initializeGraphAndListOfConnections(taules,matrixOfDirectlyConnections);

        BestPathsMatrix bestPathsMatrix = new BestPathsMatrix(connectionsBetweenTables.getQuantitat());
        short [][] dist = calculateMaximmumPaths(connectionsBetweenTables,bestPathsMatrix);

        TablesData.getInstance().setMatrixOfDirectlyConnections(matrixOfDirectlyConnections);
        TablesData.getInstance().setBestPathsMatrix(bestPathsMatrix);
        TablesData.getInstance().setDist(dist);
        TablesData.getInstance().setTaules(taules);
        TablesData.getInstance().setTaulesById(taulesById);

        context.changeState(new FilterTables());
        context.doStateFunction(string);
    }
}
