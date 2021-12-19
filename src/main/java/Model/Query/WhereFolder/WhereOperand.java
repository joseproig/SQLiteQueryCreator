package Model.Query.WhereFolder;

public abstract class WhereOperand {
    private String type;

    public WhereOperand(String type) {
        this.type = type;
    }

    public abstract String toString();
}
