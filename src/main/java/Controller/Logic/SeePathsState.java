package Controller.Logic;

import Model.TablesData;

import java.io.IOException;

public class SeePathsState extends State{
    @Override
    void doYourFunction(String string) throws IOException {
        for (Long clave: TablesData.getInstance().getCaminsFiltrats().keySet()) {
            short [] value = TablesData.getInstance().getCaminsFiltrats().get(clave);
            SubMatrixTreatment subMatrixTreatment = new SubMatrixTreatment(TablesData.getInstance().getMatrixOfDirectlyConnections(), value);
            subMatrixTreatment.start();
        }
    }
}
