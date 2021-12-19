package Model.ParametersOfQuestion;

import Model.ParametersOfQuestion.FilterOptions.FilterOption;

public class FilterInSelect {
    private FilterOption filterOption1;
    private String typeOfFilter;
    private FilterOption filterOption2;

    public FilterInSelect(FilterOption filterOption1, String typeOfFilter, FilterOption filterOption2) {
        this.filterOption1 = filterOption1;
        this.typeOfFilter = typeOfFilter;
        this.filterOption2 = filterOption2;
    }

    public FilterOption getFilterOption1() {
        return filterOption1;
    }

    public void setFilterOption1(FilterOption filterOption1) {
        this.filterOption1 = filterOption1;
    }

    public FilterOption getFilterOption2() {
        return filterOption2;
    }

    public void setFilterOption2(FilterOption filterOption2) {
        this.filterOption2 = filterOption2;
    }

    public String getTypeOfFilter() {
        return typeOfFilter;
    }

    public void setTypeOfFilter(String typeOfFilter) {
        this.typeOfFilter = typeOfFilter;
    }
}
