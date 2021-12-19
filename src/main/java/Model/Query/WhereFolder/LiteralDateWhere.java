package Model.Query.WhereFolder;

public class LiteralDateWhere extends WhereOperand{
    String date;

    public LiteralDateWhere(String date) {
        super("LiteralDateWhere");
        this.date = date;
    }

    @Override
    public String toString() {
        return date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
