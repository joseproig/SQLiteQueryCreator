package Controller.Logic;

import Model.TablesData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SeePathsState extends State{
    @Override
    void doYourFunction(String string) throws IOException {
        List <SubMatrixTreatment> subMatrixTreatments = new ArrayList<>();
        HashMap<Long, List<Integer>> resultat = new HashMap<>();
        for (Long clave: TablesData.getInstance().getCaminsFiltrats().keySet()) {
            short [] value = TablesData.getInstance().getCaminsFiltrats().get(clave);
            SubMatrixTreatment subMatrixTreatment = new SubMatrixTreatment(TablesData.getInstance().getMatrixOfDirectlyConnections(), value);
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
    }
}
