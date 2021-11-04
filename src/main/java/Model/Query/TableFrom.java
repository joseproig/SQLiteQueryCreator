package Model.Query;

import java.util.ArrayList;
import java.util.List;

public class TableFrom {
    private String tableName;
    private String tableRealName;
    private List<Relation> relations;

    public TableFrom(String tableName) {
        this.tableName = tableName;
        relations = new ArrayList<>();
    }

    public TableFrom(String tableName, String tableRealName) {
        this.tableName = tableName;
        this.tableRealName = tableRealName;
        relations = new ArrayList<>();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public void addRelation (Relation relation) {
        relations.add(relation);
    }

    public String getTableRealName() {
        return tableRealName;
    }

    public void setTableRealName(String tableRealName) {
        this.tableRealName = tableRealName;
    }

    public String toString (boolean firstTable) {
        StringBuilder stringBuilder = new StringBuilder("");
        int i = 0;
        for (Relation relation: relations) {
            if (relation.getType() == TypeRelation.INNER_JOIN) {
                if (firstTable && i == 0) {
                    stringBuilder.append(tableRealName).append(" AS ").append(tableName);
                }
                stringBuilder.append(" INNER JOIN ").append(relation.getTableFrom().getTableRealName()).append(" AS ").append(relation.getTableFrom().getTableName()).append(" ON ");
                int j = 0;
                for (FK fk: relation.getFks()) {
                    stringBuilder.append(tableName).append(".").append(fk.getNomOrigen()).append("=").append(relation.getTableFrom().getTableName()).append(".").append(fk.getNomDesti());
                    if (++j != relation.getFks().size()) {
                        stringBuilder.append(" and ");
                    }
                }
                if (relation.getTableFrom().getRelations().size() != 0) {
                    stringBuilder.append(relation.getTableFrom().toString(false));
                }
                if (++i != relations.size()) {
                    stringBuilder.append(" ");
                }
            }
        }
        return stringBuilder.toString();
    }
}
