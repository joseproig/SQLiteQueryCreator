package Model;

public class Columna {
    private String columnName;
    private String tableName;
    private String tableNameInFrom;
    private String type;
    private Boolean isFK;
    private String tableReference;
    private String columnReference;


    public String getTableNameInFrom() {
        return tableNameInFrom;
    }

    public void setTableNameInFrom(String tableNameInFrom) {
        this.tableNameInFrom = tableNameInFrom;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
