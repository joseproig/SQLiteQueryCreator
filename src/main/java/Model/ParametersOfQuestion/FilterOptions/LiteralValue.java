package Model.ParametersOfQuestion.FilterOptions;

public class LiteralValue extends FilterOption{
    private String value;

    public LiteralValue(String type, String value) {
        super(type);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
