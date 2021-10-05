package Model;

public class Columna {
    private String columnName;
    private Boolean isFK;
    private String tableReference;
    private String columnReference;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Boolean getFK() {
        return isFK;
    }

    public void setFK(Boolean FK) {
        isFK = FK;
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
}
