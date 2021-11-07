package Model;

import Model.ParametersOfQuestion.ParametersConfig;

public class ProgramConfig {
    private static ProgramConfig instance;
    private String dbPath;
    private ParametersConfig filterParams;

    public static ProgramConfig getInstance() {
        if (instance == null) {
            instance = new ProgramConfig();
        }
        return instance;
    }

    public String getDbPath() {
        return dbPath;
    }

    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }

    public static void setInstance(ProgramConfig instance) {
        ProgramConfig.instance = instance;
    }

    public ParametersConfig getFilterParams() {
        return filterParams;
    }

    public void setFilterParams(ParametersConfig filterParams) {
        this.filterParams = filterParams;
    }
}
