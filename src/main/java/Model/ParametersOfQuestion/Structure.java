package Model.ParametersOfQuestion;


import Model.ParametersOfQuestion.SelectFolder.ColumnInSelect;
import Model.ParametersOfQuestion.SelectFolder.ColumnsInSelect;

import java.util.ArrayList;
import java.util.List;

public class Structure {
    private ColumnsInSelect columnsToSeeInSelect;
    private List<FilterInSelect> columnsToFilterInSelect;
    private List<String> tablesThatAppearInSelect;
    private List<String> columnsToOrderBy;

    public Structure() {
        columnsToSeeInSelect = new ColumnsInSelect();
        columnsToFilterInSelect = new ArrayList<>();
        columnsToOrderBy = new ArrayList<>();
        tablesThatAppearInSelect = new ArrayList<>();
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

    public List<String> getColumnsToOrderBy() {
        return columnsToOrderBy;
    }

    public void setColumnsToOrderBy(List<String> columnsToOrderBy) {
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

    public void addColumnToOrderBy (String string) {
        columnsToOrderBy.add(string);
    }

}
