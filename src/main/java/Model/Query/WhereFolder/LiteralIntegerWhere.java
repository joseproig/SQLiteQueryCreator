package Model.Query.WhereFolder;



public class LiteralIntegerWhere extends WhereOperand {
    int number;

    public LiteralIntegerWhere(int number) {
        super("LiteralIntegerWhere");
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


    @Override
    public String toString() {
        return "" + number;
    }
}
