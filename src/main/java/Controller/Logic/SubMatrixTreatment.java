package Controller.Logic;

import Model.MatrixOfConnections;
import Model.ProgramConfig;
import Model.TablesData;
import Model.Taula;
import Utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SubMatrixTreatment extends Thread{
    MatrixOfConnections submatrixOfConnections;
    short [] listOfSubmatrix;

    public SubMatrixTreatment (MatrixOfConnections submatrixOfConnections, short [] listOfSubmatrix) {
        this.submatrixOfConnections = submatrixOfConnections;
        this.listOfSubmatrix = listOfSubmatrix;
    }

    @Override
    public void run() {
        List<List<Integer>> nodesConsultats = new ArrayList<>();
        int j = 0;
        List<List <Integer>> resultat = new ArrayList<>();
        for (int i = 0; i < submatrixOfConnections.getListOfConnections().size();i++) {
            short shortValue = ((short)i);
            //TODO: Cambiar este asList, si conve crear una funció utils
            if (ArrayUtils.shortArrayContains(listOfSubmatrix,shortValue)) {
                nodesConsultats.add(new ArrayList<>());
                nodesConsultats.get(j++).add(i);
                ArrayList<Integer> integers = new ArrayList<>();
                integers.add(i);
                backtracking(integers, nodesConsultats.size() - 1 , nodesConsultats,resultat);
                System.out.println("BU");
            }
        }
        System.out.println("BU");
    }

    public void backtracking (List<Integer> nodesActual, Integer llistaAPlenar, List<List<Integer>> nodesConsultats,List<List <Integer>> resultat) {
        //if (ProgramConfig.getInstance().getFilterParams().getMinNumTables() > nodesConsultats.get(llistaAPlenar).size()) {
            int numPossibilities = 0;
            List<Integer> tableOptions = new ArrayList<>();
            for (int y = 0; y < nodesActual.size(); y++) {
                for (int i = 0; i < submatrixOfConnections.getListOfConnections().get(nodesActual.get(y)).size(); i++) {
                    short shortValue = submatrixOfConnections.getListOfConnections().get(nodesActual.get(y)).get(i).shortValue();
                    if ((!nodesConsultats.get(llistaAPlenar).contains(submatrixOfConnections.getListOfConnections().get(nodesActual.get(y)).get(i))) && ArrayUtils.shortArrayContains(listOfSubmatrix, shortValue) && !tableOptions.contains(submatrixOfConnections.getListOfConnections().get(nodesActual.get(y)).get(i))) {
                        tableOptions.add(submatrixOfConnections.getListOfConnections().get(nodesActual.get(y)).get(i));
                        numPossibilities++;
                    }
                }
            }
            double possibilities = Math.pow(2, numPossibilities);


            if (numPossibilities == 0) {
                resultat.add(nodesConsultats.get(llistaAPlenar));
                return ;
            }
            for (int i = 1; i < possibilities; i++) {
                if ((i+1) < possibilities) {
                    nodesConsultats.add(new ArrayList<>(nodesConsultats.get(llistaAPlenar)));
                }
                List<Integer> listOfAddedNodes = new ArrayList<>();
                for (int j = 0; j < Math.pow(2, numPossibilities - 1); j++) {
                    if ((i >> j & 1) == 1) {
                        nodesConsultats.get(llistaAPlenar).add(tableOptions.get(j));
                        listOfAddedNodes.add(tableOptions.get(j));
                    }
                }

                backtracking(listOfAddedNodes, llistaAPlenar, new ArrayList<>(nodesConsultats),resultat);

                llistaAPlenar = nodesConsultats.size() - 1;
            }

            System.out.println("Bu");



    }
}
