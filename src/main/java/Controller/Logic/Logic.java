package Controller.Logic;

import Controller.DBLogic.DBConnection;
import Model.Columna;
import Model.DynamicMatrix;
import Model.ProgramConfig;
import Model.Taula;
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

    public DynamicMatrix initializeGraph (HashMap <String, Taula> taules) {
        Iterator<String> it = taules.keySet().iterator();
        DynamicMatrix graph = new DynamicMatrix(taules.size());

        while(it.hasNext()){
            String nomTaula = it.next();
            Taula taula = taules.get(nomTaula);
            List<Columna> fks = taula.getForeignKeys();
            int [] fksTableId = new int[fks.size()];

            int i = 0;
            while (i < fks.size()) {
                String tableRef = fks.get(i).getTableReference();
                fksTableId[i] = taules.get(tableRef).getId();
                i++;
            }
            graph.insertarTaula(taula.getId(),fksTableId);
        }

        return graph;
    }
}
