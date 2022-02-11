package Model.DatabaseData;

import Model.Taula;

import java.util.HashMap;

public class DatabaseData {
    private HashMap<String, Taula> taules;
    private static DatabaseData instance;

    public static DatabaseData getInstance () {
        if (instance == null) {
            instance = new DatabaseData();
        }
        return instance;
    }



    public HashMap<String, Taula> getTaules() {
        return taules;
    }

    public void setTaules(HashMap<String, Taula> taules) {
        this.taules = taules;
    }
}
