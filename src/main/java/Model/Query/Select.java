package Model.Query;

import Model.Columna;

import java.util.HashMap;

public class Select implements Cloneable{
    private HashMap<String, ColumnaResult> columnaResult;
    private From from;
    private Where where;


    public Select(HashMap<String, ColumnaResult> columnaResult, From from) {
        this.columnaResult = columnaResult;
        this.from = from;
        this.where = new Where();
    }

    public Select(HashMap<String, ColumnaResult> columnaResult, From from, Where where) throws CloneNotSupportedException {
        this.columnaResult = columnaResult;
        this.from = from;
        this.where = (Where) new Where(where.getExpression());
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public Where getWhere() {
        return where;
    }

    public void setWhere(Where where) {
        this.where = where;
    }

    public HashMap<String, ColumnaResult> getColumnaResult() {
        return columnaResult;
    }

    public void setColumnaResult(HashMap<String, ColumnaResult> columnaResult) {
        this.columnaResult = columnaResult;
    }

    public String toString () {
        StringBuilder stringBuilder = new StringBuilder("SELECT " + stringifyColumns());
        stringBuilder.append(from.toString());
        stringBuilder.append(where.toString());
        return stringBuilder.toString();
    }



    private String stringifyColumns () {
        StringBuilder columns = new StringBuilder();
        int i = 0;
        int sizeOfColumnaResult = columnaResult.size();

        for (String columnKey:columnaResult.keySet()) {
            columns.append(columnaResult.get(columnKey).toString());
            if (++i < sizeOfColumnaResult) {
                columns.append(",");
            } else {
                columns.append(" ");
            }
        }
        return columns.toString();
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
