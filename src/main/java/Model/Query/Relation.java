package Model.Query;

import java.util.List;

public class Relation {
    private List<FK> fks;
    private TypeRelation type;
    private TableFrom tableFrom;

    public Relation(List<FK> fks, TypeRelation type, TableFrom tableFrom) {
        this.fks = fks;
        this.type = type;
        this.tableFrom = tableFrom;
    }

    public List<FK> getFks() {
        return fks;
    }

    public void setFks(List<FK> fks) {
        this.fks = fks;
    }

    public TypeRelation getType() {
        return type;
    }

    public void setType(TypeRelation type) {
        this.type = type;
    }

    public TableFrom getTableFrom() {
        return tableFrom;
    }

    public void setTableFrom(TableFrom tableFrom) {
        this.tableFrom = tableFrom;
    }
}
