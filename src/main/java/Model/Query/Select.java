package Model.Query;

import Model.Columna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Select implements Cloneable{
    private HashMap<String, ColumnaResult> columnaResult;
    private From from;
    private Where where;
    private Order order;
    private List<String> questions;


    public Select(HashMap<String, ColumnaResult> columnaResult, From from) {
        this.columnaResult = columnaResult;
        this.from = from;
        this.where = new Where();
        this.questions = new ArrayList<>();
    }

    public Select(HashMap<String, ColumnaResult> columnaResult, From from, Where where) throws CloneNotSupportedException {
        this.columnaResult = columnaResult;
        this.from = from;
        this.where = (Where) new Where(where.getExpression());
        this.questions = new ArrayList<>();
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String toString () {
        StringBuilder stringBuilder = new StringBuilder("SELECT " + stringifyColumns());
        stringBuilder.append(from.toString());
        stringBuilder.append(where.toString());
        if (order != null) {
            stringBuilder.append(order.toString());
        }
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

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestion(List<String> question) {
        this.questions = question;
    }

    public void addQuestion (String questionToAdd) {
        this.questions.add(questionToAdd);
    }

    public String printAllQuestions () {
        int i = 1;
        StringBuilder questionsString = new StringBuilder("");
        for (String question:questions) {
            questionsString.append(i).append(".").append(question).append("\n");
            i++;
        }
        return questionsString.toString();
    }
}
