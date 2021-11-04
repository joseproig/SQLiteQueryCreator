package Model.Query;

public class IntegerRef {
    private Integer integer;

    public IntegerRef(Integer integer) {
        this.integer = integer;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public void incrementInteger () {
        this.integer += 1;
    }
}
