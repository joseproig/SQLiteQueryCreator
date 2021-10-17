import Controller.Logic.Logic;
import Model.DynamicMatrix;
import Model.MatrixOfConnections;
import Model.Taula;
import Utils.Algorithms.FloydWarshall;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        /**
         * En aquest programa hi haurà tres estructures principals:
         * - matrixOfDirectlyConnections: Que serà una llista que contindrà per cada taula, els noms de les taules a les que aquesta pot accedir
         * - connectionBetweenTables: Què serà una matriu binaria d'adjacencia que representarà la distribució del graph
         * - dist: Què serà una matriu que contindrà el màxim número de taules que es pot abarcar d'una taula a una altra
         */
        Logic logic = new Logic();
        HashMap<String, Taula> taules = logic.getTaules();
        MatrixOfConnections matrixOfDirectlyConnections = new MatrixOfConnections();
        DynamicMatrix connectionsBetweenTables = logic.initializeGraphAndListOfConnections(taules,matrixOfDirectlyConnections);

        FloydWarshall floydWarshall = new FloydWarshall(connectionsBetweenTables.getQuantitat());
        short[][] dist = floydWarshall.floydWarshall(connectionsBetweenTables);

        //TODO: Es podria mirar que al aplicar Floyd Warshall es guardessin tots els camins intentats
        //TODO: Resposta per descartar solucions repetides fer Hashmap de ids de les taules ordenats
        System.out.println("El programa ha finalitzat");
    }
}
