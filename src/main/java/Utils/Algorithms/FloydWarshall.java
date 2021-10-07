package Utils.Algorithms;

import Model.DynamicMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FloydWarshall {
    final static short INF = -9999;
    short numOfTables;

    public FloydWarshall (short numOfTables) {
        this.numOfTables = numOfTables;
    }

    public short [][] floydWarshall(DynamicMatrix dynamicMatrix) {
        short dist[][] = new short[numOfTables][numOfTables];
        short i, j, k, w;

        List<Short> directCommunicationsTable = new ArrayList<>();

        short firstIteration = 0;

        for (i = 0; i < numOfTables; i++) {
            for (w = 0; w < numOfTables; w++) {
                if (i == w) {
                    dist[i][w] = 0;
                } else {
                    dist[i][w] = INF;
                }
                if(firstIteration < 9) {
                    directCommunicationsTable.add((short) (dynamicMatrix.retornaNumComunicacionsDirectesTaula(w)));
                    firstIteration++;
                }
            }
            Short shorts [] = dynamicMatrix.retornaTaulesVinculadesAUnaTaula(i);
            if (shorts != null) {
                for (j = 0; j < shorts.length; j++) {
                    Short shorts_j = shorts[j];
                    if (shorts_j != null) {
                        dist[i][shorts_j] = directCommunicationsTable.get(shorts_j);
                        dist[shorts_j][i] = directCommunicationsTable.get(i);
                    }
                }
            }
        }


        for (k = 0; k < numOfTables; k++) {
            for (i = 0; i < numOfTables; i++) {
                for (j = 0; j < numOfTables; j++) {
                    if ((dist[i][k] + dist[k][j] > dist[i][j]) && i != j && (k != j && k != i) && dist[i][j] <= 0) {
                        short result = (short) (dist[i][k] + dist[k][j]);
                        if (result > 0) {
                            dist[i][j] = (short) (result - 1);
                        } else {
                            dist[i][j] = INF;
                        }
                    }
                }
            }
        }
        i = 0;

        return dist;
        // Print the shortest distance matrix
        //printSolution(dist);
    }
}
