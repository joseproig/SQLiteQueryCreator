package Model.Query;

import Model.Columna;
import Model.TablesData;
import Model.Taula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Select implements Cloneable{
    private HashMap<String, ColumnaResult> columnaResult;
    private From from;
    private Where where;
    private Order order;
    private List<String> questions;
    private List<String> logosQuestions;
    private List<String> eqsplainQuestions;
    private List<String> templateQuestions;
    public static final String LOGOS = "Logos";
    public static final String EQSPLAIN = "EqsPlain";
    public static final String TEMPLATE = "Template";





    public Select(HashMap<String, ColumnaResult> columnaResult, From from) {
        this.columnaResult = columnaResult;
        this.from = from;
        this.where = new Where();
        this.questions = new ArrayList<>();
        this.logosQuestions = new ArrayList<>();
        this.eqsplainQuestions = new ArrayList<>();
        this.templateQuestions = new ArrayList<>();
    }

    public Select(HashMap<String, ColumnaResult> columnaResult, From from, Where where) throws CloneNotSupportedException {
        this.columnaResult = columnaResult;
        this.from = from;
        this.where = (Where) new Where(where.getExpression());
        this.questions = new ArrayList<>();
        this.logosQuestions = new ArrayList<>();
        this.eqsplainQuestions = new ArrayList<>();
        this.templateQuestions = new ArrayList<>();
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

    public String printQuestions (String program) {
        List <String> strings;
        switch (program) {
            case LOGOS:
                strings = logosQuestions;
                break;
            case EQSPLAIN:
                strings = eqsplainQuestions;
                break;
            case TEMPLATE:
                strings = templateQuestions;
                break;
            default:
                strings = new ArrayList<>();
                break;
        }
        int i  = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (String s: strings) {
            stringBuilder.append(i).append(".").append(s);
            i++;
        }
        return stringBuilder.toString();
    }

    public void addQuestion (String questionToAdd, String program) {
        this.questions.add(questionToAdd);
        switch (program) {
            case LOGOS:
                this.logosQuestions.add(questionToAdd);
                break;
            case EQSPLAIN:
                this.eqsplainQuestions.add(questionToAdd);
                break;
            case TEMPLATE:
                this.templateQuestions.add(questionToAdd);
                break;
            default:
                break;
        }
    }

    public void addQuestions (List<String> questionToAdd, String program) {
        this.questions.addAll(questionToAdd);
        switch (program) {
            case LOGOS:
                this.logosQuestions.addAll(questionToAdd);
                break;
            case EQSPLAIN:
                this.eqsplainQuestions.addAll(questionToAdd);
                break;
            case TEMPLATE:
                this.templateQuestions.addAll(questionToAdd);
                break;
            default:
                break;
        }
    }

    public String printAllQuestions () {
        StringBuilder questionsString = new StringBuilder("");
        questionsString.append("<br><br><em><strong>").append("These are the options of the question:").append("</br></br></em></strong>").append("\n");
        questionsString.append("<ul>").append("\n");
        for (String question:questions) {
            questionsString.append("<li>").append(question).append("</li>").append("\n");
        }
        questionsString.append("</ul>").append("\n");
        questionsString.append("<u>Schema:</u><p></p><ul>");
        for (Map.Entry<String, Taula> entry: TablesData.getInstance().getTaules().entrySet()){
            questionsString.append("<li><strong><span class=\"\" style=\"color: rgb(51, 102, 255);\">").append(entry.getValue().getNomTaula()).append("(");
            int i = 0;

            for(Columna entry3:entry.getValue().getForeignKeys()) {
                boolean isPK = entry3.getPK();
                if(isPK) {
                    questionsString.append("<span><u><span class=\"\" style=\"color: rgb(51, 102, 255);\">");
                }
                questionsString.append(entry3.getColumnName()).append("(FK)");

                if(isPK) {
                    questionsString.append("</span></u></span>");
                }
                if ((i != entry.getValue().getForeignKeys().size()-1) || (entry.getValue().getColumnes().size() != 0 && i == entry.getValue().getForeignKeys().size()-1)) {
                    questionsString.append(",");
                }
                i++;
            }

            i = 0;
            for (Map.Entry<String, Columna> entry2:entry.getValue().getColumnes().entrySet()){
                boolean isPK = entry2.getValue().getPK();
                if(isPK) {
                    questionsString.append("<span><u><span class=\"\" style=\"color: rgb(51, 102, 255);\">");
                }
                questionsString.append(entry2.getValue().getColumnName());
                if(isPK) {
                    questionsString.append("</span></u></span>");
                }
                if (i != entry.getValue().getColumnes().entrySet().size()-1) {
                    questionsString.append(",");
                }
                i++;
            }
            questionsString.append(")</span></strong></li>");
        }

        questionsString.append("</ul>");
        return questionsString.toString();
    }
}
