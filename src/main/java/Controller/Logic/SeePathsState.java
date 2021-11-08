package Controller.Logic;

import Model.ProgramConfig;
import Model.ResultOfFiltering;
import Model.TablesData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SeePathsState extends State{
    @Override
    void doYourFunction(String string) throws IOException {
        //TODO: Bucles entre taules

        List <SubMatrixTreatment> subMatrixTreatments = new ArrayList<>();
        HashMap<Long, ResultOfFiltering> resultat = new HashMap<>();
        for (Long clave: TablesData.getInstance().getCaminsFiltrats().keySet()) {
            short [] value = TablesData.getInstance().getCaminsFiltrats().get(clave);
            SubMatrixTreatment subMatrixTreatment = new SubMatrixTreatment(TablesData.getInstance().getMatrixOfDirectlyConnections(), value, ProgramConfig.getInstance().getFilterParams().getQuestions().get(Integer.parseInt(string)).getMinNumTables(), ProgramConfig.getInstance().getFilterParams().getQuestions().get(Integer.parseInt(string)).getMaxNumTables());
            subMatrixTreatments.add(subMatrixTreatment);
            subMatrixTreatment.start();
        }
        for (int i = 0; i < subMatrixTreatments.size(); i++) {
            try {
                subMatrixTreatments.get(i).join();
                resultat.putAll(subMatrixTreatments.get(i).getResultat());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        TablesData.getInstance().setCaminsPossiblesSolucions(resultat);
        context.changeState(new MakeFromState());
        context.doStateFunction(string);
    }
}
