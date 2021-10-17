package Model;

import java.util.ArrayList;
import java.util.List;

public class MatrixOfConnections {
    List<List<Integer>> listOfConnections;



    public void initializeListOfConnections (int numOfTables) {
        listOfConnections = new ArrayList<>();
        for (int i = 0; i < numOfTables; i++) {
            listOfConnections.add(new ArrayList<>());
        }
    }



    public void makeItBidirectional () {
        List<List<Integer>> copiaListOfConnections = new ArrayList<List<Integer>> ();

        for (int i = 0; i < listOfConnections.size();i++) {
            copiaListOfConnections.add(new ArrayList<>(listOfConnections.get(i)));
        }

        for (int i = 0; i < copiaListOfConnections.size(); i++) {
            for (int j = 0; j < copiaListOfConnections.get(i).size(); j++) {
                listOfConnections.get(listOfConnections.get(i).get(j)).add(i);
            }
        }
    }

    public void setNewRelation (int tableId, int tableToPoint) {
        //Comprovem que la taula no apunti dos o més vegades a la mateixa taula, per exemple com passava a Follower
        if (!checkIfTableContainsAnotherTable(tableId,tableToPoint)) {
            listOfConnections.get(tableId).add(tableToPoint);
        }
    }

    public boolean checkIfTableContainsAnotherTable (int tableId, int tableToCheck) {
        return listOfConnections.get(tableId).contains(tableToCheck);
    }

    public int [] getRelationsOfATable (int tableId) {
        List <Integer> connectionsOfTable = listOfConnections.get(tableId);
        int [] arrayOfConnections = new int[connectionsOfTable.size()];
        for (int i = 0; i < connectionsOfTable.size();i++) {
            arrayOfConnections[i] = connectionsOfTable.get(i);
        }
        return arrayOfConnections;
    }

    public List<List<Integer>> getListOfConnections() {
        return listOfConnections;
    }

    public void setListOfConnections(List<List<Integer>> listOfConnections) {
        this.listOfConnections = listOfConnections;
    }
}
