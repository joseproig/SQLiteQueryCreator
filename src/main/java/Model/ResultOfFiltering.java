package Model;

import java.util.List;

public class ResultOfFiltering {
    private TableLayer connections;
    private List<Integer> tablesOfTheResult;


    public ResultOfFiltering(TableLayer connections, List<Integer> tablesOfTheResult) {
        this.connections = connections;
        this.tablesOfTheResult = tablesOfTheResult;
    }

    public TableLayer getConnections() {
        return connections;
    }

    public void setConnections(TableLayer connections) {
        this.connections = connections;
    }

    public List<Integer> getTablesOfTheResult() {
        return tablesOfTheResult;
    }

    public void setTablesOfTheResult(List<Integer> tablesOfTheResult) {
        this.tablesOfTheResult = tablesOfTheResult;
    }
}
