package Model.ParametersOfQuestion;

import Model.Taula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Structure {
    private List<String> columnsToSeeInSelect;
    private List<FilterInSelect> columnsToFilterInSelect;
    private List<String> columnsToOrderBy;

    public Structure() {
        columnsToSeeInSelect = new ArrayList<>();
        columnsToFilterInSelect = new ArrayList<>();
        columnsToOrderBy = new ArrayList<>();
    }

    public List<String> getColumnsToSeeInSelect() {
        return columnsToSeeInSelect;
    }

    public void setColumnsToSeeInSelect(List<String> columnsToSeeInSelect) {
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


    public void addColumnToSeeInSelect (String string) {
        columnsToSeeInSelect.add(string);
    }

    public void addColumnToFilterInSelect (FilterInSelect filterInSelect) {
        columnsToFilterInSelect.add(filterInSelect);
    }

    public void addColumnToOrderBy (String string) {
        columnsToOrderBy.add(string);
    }

}
