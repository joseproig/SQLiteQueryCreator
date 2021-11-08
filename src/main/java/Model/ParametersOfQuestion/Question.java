package Model.ParametersOfQuestion;

public class Question {
    private String question;
    private int minNumTables;
    private int maxNumTables;
    private Structure structure;


    public Question() {
        structure = new Structure();
    }

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

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }
}