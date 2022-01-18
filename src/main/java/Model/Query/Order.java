package Model.Query;

import Model.Query.WhereFolder.ColumnWhere;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<ColumnWhere> orderByResults;

    public Order() {
        this.orderByResults = new ArrayList<>();
    }

    public Order(List<ColumnWhere> orderByResults) {
        this.orderByResults = orderByResults;
    }

    public List<ColumnWhere> getOrderByResults() {
        return orderByResults;
    }

    public void setOrderByResults(List<ColumnWhere> orderByResults) {
        this.orderByResults = orderByResults;
    }

    public void addColumnaResultIntoArray (ColumnWhere columnaResultToAdd) {
        this.orderByResults.add(columnaResultToAdd);
    }

    public String toString () {
        if (orderByResults.size() != 0) {
            StringBuilder orderBy = new StringBuilder(" ORDER BY ");
            for (int j = 0; j < (orderByResults.size() - 1); j++) {
                orderBy.append(orderByResults.get(j).toString()).append(" , ");
            }
            orderBy.append(orderByResults.get(orderByResults.size() - 1));
            return orderBy.toString();
        }
        return "";
    }
}
