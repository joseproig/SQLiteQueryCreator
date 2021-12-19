package Model.ParametersOfQuestion.SelectFolder;

public class ColumnInSelect {
    private String columnReference;
    private String tableReference;

    public ColumnInSelect(String columnReference, String tableReference) {
        this.columnReference = columnReference;
        this.tableReference = tableReference;
    }

    public String getColumnReference() {
        return columnReference;
    }

    public void setColumnReference(String columnReference) {
        this.columnReference = columnReference;
    }

    public String getTableReference() {
        return tableReference;
    }

    public void setTableReference(String tableReference) {
        this.tableReference = tableReference;
    }

    public String generateName (){
        return tableReference + "_" + columnReference;
    }
}
