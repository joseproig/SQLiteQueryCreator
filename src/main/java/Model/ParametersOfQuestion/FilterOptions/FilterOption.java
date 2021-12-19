package Model.ParametersOfQuestion.FilterOptions;

public abstract class FilterOption {
    String type;

    public FilterOption(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
