package Model.ParametersOfQuestion;

public class FilterInSelect {
    private String columnThatContainsFilter;
    private String typeOfFilter;

    public FilterInSelect(String columnThatContainsFilter, String typeOfFilter) {
        this.columnThatContainsFilter = columnThatContainsFilter;
        this.typeOfFilter = typeOfFilter;
    }

    public String getColumnThatContainsFilter() {
        return columnThatContainsFilter;
    }

    public void setColumnThatContainsFilter(String columnThatContainsFilter) {
        this.columnThatContainsFilter = columnThatContainsFilter;
    }

    public String getTypeOfFilter() {
        return typeOfFilter;
    }

    public void setTypeOfFilter(String typeOfFilter) {
        this.typeOfFilter = typeOfFilter;
    }
}
