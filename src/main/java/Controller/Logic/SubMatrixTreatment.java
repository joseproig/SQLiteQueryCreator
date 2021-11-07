package Controller.Logic;

import Model.*;
import Utils.ArrayUtils;
import Utils.ByteTreatment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SubMatrixTreatment extends Thread{
    MatrixOfConnections submatrixOfConnections;
    short [] listOfSubmatrix;
    HashMap<Long, ResultOfFiltering> resultat;
    private int minNumTables;
    private int maxNumTables;

    public SubMatrixTreatment (MatrixOfConnections submatrixOfConnections, short [] listOfSubmatrix, int minNumTables, int maxNumTables) {
        this.submatrixOfConnections = submatrixOfConnections;
        this.listOfSubmatrix = listOfSubmatrix;
        this.resultat = new HashMap<>();
        this.minNumTables = minNumTables;
        this.maxNumTables = maxNumTables;
    }

    @Override
    public void run() {
        for (int i = 0; i < submatrixOfConnections.getListOfConnections().size();i++) {
            short shortValue = ((short)i);
            if (ArrayUtils.shortArrayContains(listOfSubmatrix,shortValue)) {
                List<List<Integer>> nodesConsultats = new ArrayList<>();
                List<TableLayer> tableLayers = new ArrayList<>();
                nodesConsultats.add(new ArrayList<>());
                nodesConsultats.get(0).add(i);
                tableLayers.add(new TableLayer(i));
                ArrayList<Integer> integers = new ArrayList<>();
                integers.add(i);
                backtracking(integers, 0 , nodesConsultats, tableLayers, resultat);
            }
        }
    }

    public void backtracking (List<Integer> nodesActual, Integer llistaAPlenar, List<List<Integer>> nodesConsultats,List<TableLayer> tableLayers ,  HashMap<Long, ResultOfFiltering> resultat) {
        //if (ProgramConfig.getInstance().getFilterParams().getMinNumTables() > nodesConsultats.get(llistaAPlenar).size()) {
            int numPossibilities = 0;
            List<Integer> tableOptions = new ArrayList<>();
            List<Integer> originOptions = new ArrayList<>();

            for (int y = 0; y < nodesActual.size(); y++) {
                for (int i = 0; i < submatrixOfConnections.getListOfConnections().get(nodesActual.get(y)).size(); i++) {
                    short shortValue = submatrixOfConnections.getListOfConnections().get(nodesActual.get(y)).get(i).shortValue();
                    if ((!nodesConsultats.get(llistaAPlenar).contains(submatrixOfConnections.getListOfConnections().get(nodesActual.get(y)).get(i))) && ArrayUtils.shortArrayContains(listOfSubmatrix, shortValue) && !tableOptions.contains(submatrixOfConnections.getListOfConnections().get(nodesActual.get(y)).get(i))) {
                        tableOptions.add(submatrixOfConnections.getListOfConnections().get(nodesActual.get(y)).get(i));
                        originOptions.add(nodesActual.get(y));
                        numPossibilities++;
                    }
                }
            }
            double possibilities = Math.pow(2, numPossibilities);


            if (numPossibilities == 0) {
                if (minNumTables <= nodesConsultats.get(llistaAPlenar).size() && maxNumTables >= nodesConsultats.get(llistaAPlenar).size()) {
                    resultat.put(ByteTreatment.convertListIntegerToLong(nodesConsultats.get(llistaAPlenar)), new ResultOfFiltering(tableLayers.get(llistaAPlenar),nodesConsultats.get(llistaAPlenar)));
                }
                return ;
            }
            List<TableLayer> newLastLayers = new ArrayList<>();
            for (int i = 1; i < possibilities; i++) {
                if ((i+1) < possibilities) {
                    nodesConsultats.add(new ArrayList<>(nodesConsultats.get(llistaAPlenar)));
                    tableLayers.add(new TableLayer(tableLayers.get(llistaAPlenar)));
                }

                List<TableLayer> lastLayers = tableLayers.get(llistaAPlenar).getLastLayer(nodesActual);

                List<Integer> listOfAddedNodes = new ArrayList<>();
                for (int j = 0; j < Math.pow(2, numPossibilities - 1); j++) {
                    if ((i >> j & 1) == 1) {
                        nodesConsultats.get(llistaAPlenar).add(tableOptions.get(j));
                        int numOfLayer;
                        for (numOfLayer = 0; numOfLayer < lastLayers.size(); numOfLayer++) {
                            if (originOptions.get(j) == lastLayers.get(numOfLayer).getTableNum()) {
                                break;
                            }
                        }
                        TableLayer tableLayer = lastLayers.get(numOfLayer).addNewLayer(tableOptions.get(j));
                        newLastLayers.add(tableLayer);
                        listOfAddedNodes.add(tableOptions.get(j));
                    }
                }

                backtracking(listOfAddedNodes, llistaAPlenar, new ArrayList<>(nodesConsultats),new ArrayList<>(tableLayers), resultat);

                llistaAPlenar = nodesConsultats.size() - 1;
            }
    }

    public MatrixOfConnections getSubmatrixOfConnections() {
        return submatrixOfConnections;
    }

    public void setSubmatrixOfConnections(MatrixOfConnections submatrixOfConnections) {
        this.submatrixOfConnections = submatrixOfConnections;
    }

    public short[] getListOfSubmatrix() {
        return listOfSubmatrix;
    }

    public void setListOfSubmatrix(short[] listOfSubmatrix) {
        this.listOfSubmatrix = listOfSubmatrix;
    }

    public HashMap<Long, ResultOfFiltering> getResultat() {
        return resultat;
    }

    public void setResultat(HashMap<Long, ResultOfFiltering> resultat) {
        this.resultat = resultat;
    }
}
