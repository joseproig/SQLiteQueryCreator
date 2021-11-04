package Model.Query;

import java.util.List;

public class From {
    private TableFrom tableFrom;

    public From(TableFrom tableFrom) {
        this.tableFrom = tableFrom;
    }

    public TableFrom getTableFrom() {
        return tableFrom;
    }

    public void setTableFrom(TableFrom tableFrom) {
        this.tableFrom = tableFrom;
    }

    public String toString () {
        StringBuilder stringBuilder = new StringBuilder("FROM ");
        stringBuilder.append(tableFrom.toString(true));
        return stringBuilder.toString();
    }

}
