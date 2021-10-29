package Model;

import java.util.HashMap;

public final class TablesData {
    private static TablesData instance;
    private HashMap<String, Taula> taules;
    private MatrixOfConnections matrixOfDirectlyConnections;
    private short [][] dist;
    private BestPathsMatrix bestPathsMatrix;
    private HashMap <Long, short[]> caminsFiltrats;
    HashMap<Long, ResultOfFiltering> caminsPossiblesSolucions;

    private TablesData() {}

    public static TablesData getInstance() {
        if (instance == null) {
            instance = new TablesData();
        }
        return instance;
    }

    public static void setInstance(TablesData instance) {
        TablesData.instance = instance;
    }

    public HashMap<String, Taula> getTaules() {
        return taules;
    }

    public void setTaules(HashMap<String, Taula> taules) {
        this.taules = taules;
    }

    public MatrixOfConnections getMatrixOfDirectlyConnections() {
        return matrixOfDirectlyConnections;
    }

    public void setMatrixOfDirectlyConnections(MatrixOfConnections matrixOfDirectlyConnections) {
        this.matrixOfDirectlyConnections = matrixOfDirectlyConnections;
    }

    public short[][] getDist() {
        return dist;
    }

    public void setDist(short[][] dist) {
        this.dist = dist;
    }

    public BestPathsMatrix getBestPathsMatrix() {
        return bestPathsMatrix;
    }

    public void setBestPathsMatrix(BestPathsMatrix bestPathsMatrix) {
        this.bestPathsMatrix = bestPathsMatrix;
    }

    public HashMap<Long, short[]> getCaminsFiltrats() {
        return caminsFiltrats;
    }

    public void setCaminsFiltrats(HashMap<Long, short[]> caminsFiltrats) {
        this.caminsFiltrats = caminsFiltrats;
    }


    public HashMap<Long, ResultOfFiltering> getCaminsPossiblesSolucions() {
        return caminsPossiblesSolucions;
    }

    public void setCaminsPossiblesSolucions(HashMap<Long, ResultOfFiltering> caminsPossiblesSolucions) {
        this.caminsPossiblesSolucions = caminsPossiblesSolucions;
    }
}
