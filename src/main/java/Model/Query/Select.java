package Model.Query;

public class Select {
    private From from;
    private Where where;


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

    public String toString () {
        StringBuilder stringBuilder = new StringBuilder("SELECT * ");
        stringBuilder.append(from.toString());
        return stringBuilder.toString();
    }
}
