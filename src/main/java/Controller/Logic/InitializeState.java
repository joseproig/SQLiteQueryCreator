package Controller.Logic;

import Controller.DBLogic.DBConnection;
import Controller.DBLogic.MySQLConnector;
import Model.*;
import Model.DatabaseData.DatabaseData;
import Model.ParametersOfQuestion.FilterInSelect;
import Model.ParametersOfQuestion.FilterOptions.ColumnFilterOption;
import Model.ParametersOfQuestion.FilterOptions.FilterOption;
import Model.ParametersOfQuestion.FilterOptions.LiteralValue;
import Model.ParametersOfQuestion.ParametersConfig;
import Model.ParametersOfQuestion.Question;
import Model.ParametersOfQuestion.SelectFolder.ColumnInSelect;
import Model.ParametersOfQuestion.Structure;
import Utils.Algorithms.FloydWarshall;
import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitializeState extends State {
    public static final String JSON_PATH = "src/fileUtils/config.json";
    public static final String JSON_PATH_2 = "src/fileUtils/mysql_config.json";
    private HashMap <String, Taula> getTaules (HashMap<Integer,Taula> taulesById, int numOfQuestion) {
        try {
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new FileReader(JSON_PATH));
            ProgramConfig programConfig = gson.fromJson(br, ProgramConfig.class);
            BufferedReader br2 = new BufferedReader(new FileReader(JSON_PATH_2));
            MysqlConfig mysqlConfig = gson.fromJson(br2, MysqlConfig.class);
            ProgramConfig.setInstance(programConfig);
            MysqlConfig.setInstance(mysqlConfig);
            //Guardem els parametres que ha de tenir la pregunta
            takeParametersOfQuestion();

            HashMap <String, Taula> taules = DBConnection.getInstance(programConfig.getDbPath()).showTables(taulesById);
            DatabaseData.getInstance().setTaules(taules);
            //Conexió a la BBDD de mysql, es necessari per tal de poder-se conectar-se a Logos més tard.

            try {
                MySQLConnector.getInstance().deleteDatabasecreateNewDatabase(DBConnection.getInstance(null).getDatabaseName());
                MySQLConnector.getInstance().createNecessaryTablesForLogos(DBConnection.getInstance(null).getDatabaseName());
                //MySQLConnector.getInstance().updateLogosTables (DBConnection.getInstance(null).getDatabaseName(), taules);

                ProcessBuilder builder = new ProcessBuilder("bash", "-c","./sqlite3mysql -f " + ProgramConfig.getInstance().getDbPath() + " -d " + DBConnection.getInstance(null).getDatabaseName() + " -u " + mysqlConfig.getMysql_user() + " --mysql-password " + mysqlConfig.getMysql_passwd());
                builder.directory(new File(ProgramConfig.getInstance().getMysqlConverterPath()));
                builder.redirectErrorStream(true);
                Process p2 = builder.start();


            } catch (Exception e) {
                e.printStackTrace();
            }

            return taules;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void takeParametersOfQuestion () {
        String regularExpression = "(?<=\\{)(.*?)(?=\\})";
        Pattern pat = Pattern.compile(regularExpression);
        String regularExpression2 = "^(.[a-zA-Z0-9_+\\-\\/* ]+?)(>=|<=|>|<|==|!=)(.[a-zA-Z0-9_+\\-\\/* ]+?)$";
        Pattern pat2 = Pattern.compile(regularExpression2);
        String regularExpression3 = "^(c[0-9]+_t[0-9]+\\_s)$";
        Pattern pat3 = Pattern.compile(regularExpression3);
        String regularExpression4 = "^(t[0-9]+)$";
        Pattern pat4 = Pattern.compile(regularExpression4);
        String regularExpression5 = "^(c[0-9]+_t[0-9]+)$";
        Pattern pat5 = Pattern.compile(regularExpression5);
        String regularExpression6 = "^(c[0-9]+_t[0-9]+\\_o)$";
        Pattern pat6 = Pattern.compile(regularExpression6);

        int i = 0;
        for (Question question: ProgramConfig.getInstance().getFilterParams().getQuestions()) {
            Matcher mat = pat.matcher(question.getQuestion());
            while (mat.find()) {
                String contentInKeys = mat.group(0).toLowerCase();
                if (!getColumnsToAppearInSelect (i,pat3,contentInKeys)) {
                    if (!getColumnsToTakeIntoAccount (i, pat5, contentInKeys)) {
                        if (!getTablesThatWillParticipateInSelect(i, pat4, contentInKeys)) {
                            if (!getFilterParams(i, contentInKeys, pat2)) {
                                getColumnsToOrderBy(i, pat6, contentInKeys);
                            }
                        }
                    }
                }
            }
            Question actualQuestion = ProgramConfig.getInstance().getFilterParams().getQuestions().get(i);
            actualQuestion.setMinNumTables(actualQuestion.getStructure().getTablesThatAppearInSelect().size());
            i++;
        }
    }

    private boolean getTablesThatWillParticipateInSelect (int numberOfQuestion, Pattern pat4, String contentInKeys) {
        Matcher matches4 = pat4.matcher(contentInKeys);
        if (matches4.find()) {
            String nameOfTable = matches4.group(0);
            ProgramConfig.getInstance().getFilterParams().getQuestions().get(numberOfQuestion).getStructure().addTableToAppearInSelect(nameOfTable);
            return true;
        }
        return false;
    }

    private boolean getColumnsToAppearInSelect (int numberOfQuestion, Pattern pat3, String contentInKeys) {
        Matcher matches3 = pat3.matcher(contentInKeys);
        if (matches3.find()) {
            String [] splits = matches3.group(0).split("_");
            String column = splits [0];
            String table = splits[1];
            ColumnInSelect columnInSelect = new ColumnInSelect(column,table);
            ProgramConfig.getInstance().getFilterParams().getQuestions().get(numberOfQuestion).getStructure().addColumnToSeeInSelect(columnInSelect);
            return true;
        }
        return false;
    }

    private boolean getColumnsToTakeIntoAccount (int numberOfQuestion, Pattern pat5, String contentInKeys) {
        Matcher matches5 = pat5.matcher(contentInKeys);
        if (matches5.find()) {
            String [] splits = matches5.group(0).split("_");
            String column = splits [0];
            String table = splits[1];
            ColumnInSelect columnInSelect = new ColumnInSelect(column,table);
            ProgramConfig.getInstance().getFilterParams().getQuestions().get(numberOfQuestion).getStructure().addColumnToTakeIntoAccount(columnInSelect);
            return true;
        }
        return false;
    }

    private boolean getColumnsToOrderBy (int numberOfQuestion, Pattern pat6, String contentInKeys) {
        Matcher matches6 = pat6.matcher(contentInKeys);
        if (matches6.find()) {
            String [] splits = matches6.group(0).split("_");
            String column = splits [0];
            String table = splits[1];
            ColumnInSelect columnInSelect = new ColumnInSelect(column,table);
            ProgramConfig.getInstance().getFilterParams().getQuestions().get(numberOfQuestion).getStructure().addColumnToOrderBy(columnInSelect);
            return true;
        }
        return false;
    }

    private boolean getFilterParams (int numberOfQuestion, String contentInKeys, Pattern pat2) {
        Matcher matches2 = pat2.matcher(contentInKeys);
        if (matches2.find()) {
            FilterOption filterOption1;
            FilterOption filterOption2;
            if (matches2.group(1).equals("literal")) {
                filterOption1 = new LiteralValue("LiteralValue", matches2.group(1));
            } else {
                String [] splits = matches2.group(1).split("_");
                filterOption1 = new ColumnFilterOption("ColumnFilterOption",splits[1],splits[0]);
            }

            if (matches2.group(3).equals("literal")) {
                filterOption2 = new LiteralValue("LiteralValue", matches2.group(3));
            } else {
                String [] splits = matches2.group(3).split("_");
                filterOption2 = new ColumnFilterOption("ColumnFilterOption",splits[1],splits[0]);
            }
            ProgramConfig.getInstance().getFilterParams().getQuestions().get(numberOfQuestion).getStructure().addColumnToFilterInSelect(new FilterInSelect(filterOption1,matches2.group(2),filterOption2));
            return true;
        }

        return false;
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
