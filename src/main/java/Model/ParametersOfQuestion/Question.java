package Model.ParametersOfQuestion;

public class Question {
    private String question;
    private int minNumTables;
    private int maxNumTables;
    private Structure options;


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getMinNumTables() {
        return minNumTables;
    }

    public void setMinNumTables(int minNumTables) {
        this.minNumTables = minNumTables;
    }

    public int getMaxNumTables() {
        return maxNumTables;
    }

    public void setMaxNumTables(int maxNumTables) {
        this.maxNumTables = maxNumTables;
    }

    public Structure getOptions() {
        return options;
    }

    public void setOptions(Structure options) {
        this.options = options;
    }
}
