package Model.ParametersOfQuestion;


import Model.ParametersOfQuestion.SelectFolder.ColumnInSelect;
import Model.ParametersOfQuestion.SelectFolder.ColumnsInSelect;

import javax.naming.ldap.HasControls;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Structure {
    private ColumnsInSelect columnsToSeeInSelect;
    private List<FilterInSelect> columnsToFilterInSelect;
    private List<String> tablesThatAppearInSelect;
    private List<ColumnInSelect> columnsToOrderBy;
    private ColumnsInSelect columnsToTakeIntoAccountInSelect;
    private List<String> differentTables;

    public Structure() {
        columnsToSeeInSelect = new ColumnsInSelect();
        columnsToFilterInSelect = new ArrayList<>();
        columnsToOrderBy = new ArrayList<>();
        tablesThatAppearInSelect = new ArrayList<>();
        columnsToTakeIntoAccountInSelect = new ColumnsInSelect();
        differentTables = new ArrayList<>();
    }

    public List<String> getTablesThatAppearInSelect() {
        return tablesThatAppearInSelect;
    }

    public void setTablesThatAppearInSelect(List<String> tablesThatAppearInSelect) {
        this.tablesThatAppearInSelect = tablesThatAppearInSelect;
    }

    public ColumnsInSelect getColumnsToSeeInSelect() {
        return columnsToSeeInSelect;
    }

    public void setColumnsToSeeInSelect(ColumnsInSelect columnsToSeeInSelect) {
        this.columnsToSeeInSelect = columnsToSeeInSelect;
    }

    public List<FilterInSelect> getColumnsToFilterInSelect() {
        return columnsToFilterInSelect;
    }

    public void setColumnsToFilterInSelect(List<FilterInSelect> columnsToFilterInSelect) {
        this.columnsToFilterInSelect = columnsToFilterInSelect;
    }

    public List<ColumnInSelect> getColumnsToOrderBy() {
        return columnsToOrderBy;
    }

    public void setColumnsToOrderBy(List<ColumnInSelect> columnsToOrderBy) {
        this.columnsToOrderBy = columnsToOrderBy;
    }

    public void addTableToAppearInSelect (String string) {
        tablesThatAppearInSelect.add(string);
    }

    public void addColumnToSeeInSelect (ColumnInSelect columnInSelect) {
        columnsToSeeInSelect.addNewColumnInSelect(columnInSelect);
    }

    public void addColumnToFilterInSelect (FilterInSelect filterInSelect) {
        columnsToFilterInSelect.add(filterInSelect);
    }

    public void addColumnToOrderBy (ColumnInSelect columnInSelect) {
        columnsToOrderBy.add(columnInSelect);
    }

    public void addColumnToTakeIntoAccount (ColumnInSelect columnInSelect) {
        columnsToTakeIntoAccountInSelect.addNewColumnInSelect(columnInSelect);
    }

    public ColumnsInSelect getColumnsToTakeIntoAccountInSelect() {
        return columnsToTakeIntoAccountInSelect;
    }

    public void setColumnsToTakeIntoAccountInSelect(ColumnsInSelect columnsToTakeIntoAccountInSelect) {
        this.columnsToTakeIntoAccountInSelect = columnsToTakeIntoAccountInSelect;
    }

    public void addDifferentTables (String table) {
        boolean isNewTable = true;
        for (String s: differentTables) {
            if (s.equals(table)){
                isNewTable = false;
                break;
            }
        }
        if (isNewTable) {
            differentTables.add(table);
        }
    }

    public int getDifferentTables(){
        return differentTables.size();
    }
}
