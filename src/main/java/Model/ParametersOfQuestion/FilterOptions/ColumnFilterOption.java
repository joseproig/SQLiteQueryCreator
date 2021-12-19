package Model.ParametersOfQuestion.FilterOptions;

public class ColumnFilterOption extends FilterOption{
    private String tableReference;
    private String columnRference;

    public ColumnFilterOption(String type, String tableReference, String columnRference) {
        super(type);
        this.tableReference = tableReference;
        this.columnRference = columnRference;
    }

    public String getTableReference() {
        return tableReference;
    }

    public void setTableReference(String tableReference) {
        this.tableReference = tableReference;
    }

    public String getColumnRference() {
        return columnRference;
    }

    public void setColumnRference(String columnRference) {
        this.columnRference = columnRference;
    }
}
