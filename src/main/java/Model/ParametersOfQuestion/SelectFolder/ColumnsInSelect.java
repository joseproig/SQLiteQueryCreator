package Model.ParametersOfQuestion.SelectFolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ColumnsInSelect {
    private HashMap<String, List<ColumnInSelect>> tablesWithHisRespectiveColumns;

    public ColumnsInSelect() {
        tablesWithHisRespectiveColumns = new HashMap<>();
    }

    public HashMap<String, List<ColumnInSelect>> getTablesWithHisRespectiveColumns() {
        return tablesWithHisRespectiveColumns;
    }

    public void setTablesWithHisRespectiveColumns(HashMap<String, List<ColumnInSelect>> tablesWithHisRespectiveColumns) {
        this.tablesWithHisRespectiveColumns = tablesWithHisRespectiveColumns;
    }

    public void addNewColumnInSelect (ColumnInSelect columnInSelect) {
        List<ColumnInSelect> listOfColumnsOfThisTablesInSelect;
        if (tablesWithHisRespectiveColumns.containsKey(columnInSelect.getTableReference())) {
            listOfColumnsOfThisTablesInSelect = tablesWithHisRespectiveColumns.get(columnInSelect.getTableReference());
        } else {
            listOfColumnsOfThisTablesInSelect = new ArrayList<>();
            tablesWithHisRespectiveColumns.put(columnInSelect.getTableReference(),listOfColumnsOfThisTablesInSelect);
        }
        listOfColumnsOfThisTablesInSelect.add(columnInSelect);
    }


}
