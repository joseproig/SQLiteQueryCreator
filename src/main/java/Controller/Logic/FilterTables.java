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
        int minNumTables = ProgramConfig.getInstance().getFilterParams().getQuestions().get(Integer.parseInt(string)).getMinNumTables();
        int maxNumTables = ProgramConfig.getInstance().getFilterParams().getQuestions().get(Integer.parseInt(string)).getMaxNumTables();
        TablesData.getInstance().setCaminsFiltrats(filterTables(TablesData.getInstance().getDist(),TablesData.getInstance().getBestPathsMatrix(),minNumTables,maxNumTables));
        context.changeState(new SeePathsState());
        context.doStateFunction(string);
    }

    private HashMap<Long, short[]> filterTables (short [][] dist, BestPathsMatrix bestPathsMatrix, int minNumTables, int maxNumTables) {
        HashMap<Long, short[]> caminsFiltrats = new HashMap<>();
        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist[i].length;j++){
               if (dist[i][j] >= minNumTables && dist[i][j] <= maxNumTables){
                    caminsFiltrats.put(ByteTreatment.convertArrayToLong(bestPathsMatrix.getBestPaths()[i][j]),bestPathsMatrix.getBestPaths()[i][j]);
               }
            }
        }
        return caminsFiltrats;
    }
}
