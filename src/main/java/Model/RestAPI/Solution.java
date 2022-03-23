package Model.RestAPI;

import Model.Query.Select;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import java.util.List;

public class Solution {
    @JsonIgnore
    private static Solution instance;

    private List<List<Select>> possibleQueries;

    public static Solution getInstance () {
        if (instance == null) {
            instance = new Solution();
        }
        return instance;
    }

    public static void setInstance(Solution instance) {
        Solution.instance = instance;
    }

    public List<List<Select>> getPossibleQueries() {
        return possibleQueries;
    }

    public void setPossibleQueries(List<List<Select>> possibleQueries) {
        this.possibleQueries = possibleQueries;
    }
}
