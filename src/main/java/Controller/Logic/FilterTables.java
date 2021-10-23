package Controller.Logic;

import Model.BestPathsMatrix;
import Model.ProgramConfig;
import Model.TablesData;
import Utils.ByteTreatment;

import java.io.IOException;
import java.util.HashMap;

public class FilterTables extends State{
    @Override
    void doYourFunction(String string) throws IOException {
        TablesData.getInstance().setCaminsFiltrats(filterTables(TablesData.getInstance().getDist(),TablesData.getInstance().getBestPathsMatrix()));
        context.changeState(new SeePathsState());
        context.doStateFunction(null);
    }

    private HashMap<Long, short[]> filterTables (short [][] dist, BestPathsMatrix bestPathsMatrix) {
        HashMap<Long, short[]> caminsFiltrats = new HashMap<>();
        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist[i].length;j++){
               if (dist[i][j] >= ProgramConfig.getInstance().getFilterParams().getMinNumTables()){
                    caminsFiltrats.put(ByteTreatment.convertArrayToLong(bestPathsMatrix.getBestPaths()[i][j]),bestPathsMatrix.getBestPaths()[i][j]);
               }
            }
        }
        return caminsFiltrats;
    }
}
