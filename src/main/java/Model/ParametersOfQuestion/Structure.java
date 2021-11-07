package Model.ParametersOfQuestion;

import Model.Taula;

import java.util.HashMap;
import java.util.List;

public class Structure {
    private List<String> columnsToSeeInSelect;
    private List<FilterInSelect> columnsToFilterInSelect;
    private List<String> columnsToOrderBy;

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


}
