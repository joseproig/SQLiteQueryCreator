package Controller.Logic;

import Model.MatrixOfConnections;
import Model.ProgramConfig;

import java.util.ArrayList;
import java.util.List;

public class SubMatrixTreatment extends Thread{
    MatrixOfConnections submatrixOfConnections;


    public SubMatrixTreatment (MatrixOfConnections submatrixOfConnections) {
        this.submatrixOfConnections = submatrixOfConnections;
    }

    @Override
    public void run() {
        //TODO: Filtratge de submatrixOfConnections amb el subgraph creat
        for (int i = 0; i < submatrixOfConnections.getListOfConnections().size();i++) {
            List<List <Integer>> nodesConsultats = new ArrayList<>();
            nodesConsultats.add(submatrixOfConnections.getListOfConnections().get(i));
            backtracking(i,0,nodesConsultats);
        }
    }

    public void backtracking (int nodeActual, int llistaAPlenar, List<List<Integer>> nodesConsultats) {
        if (ProgramConfig.getInstance().getFilterParams().getMinNumTables() > nodesConsultats.get(llistaAPlenar).size()) {
            for (int i =0 ;i < submatrixOfConnections.getListOfConnections().get(nodeActual).size(); i++) {
                nodesConsultats.get(llistaAPlenar).add(nodeActual);
                if (submatrixOfConnections.getListOfConnections().get(nodeActual).size() > 1) {
                    nodesConsultats.add(nodesConsultats.get(nodesConsultats.size()-1));
                }
                backtracking(submatrixOfConnections.getListOfConnections().get(nodeActual).get(i),nodesConsultats.size()-1,nodesConsultats);
            }
        }
    }
}
