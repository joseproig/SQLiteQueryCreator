package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BestPathsMatrix {
    private short bestPaths [][][];


    public BestPathsMatrix(int matrixLength) {
        bestPaths = new short[matrixLength][matrixLength][];
    }

    public short[][][] getBestPaths() {
        return bestPaths;
    }

    public void setBestPaths(short[][][] bestPaths) {
        this.bestPaths = bestPaths;
    }

    public short[] getVinculationsOfTable (short i, short j) {
        return bestPaths[i][j];
    }

    public void setArrayValuesBidirectional (short i, short j, Short[] arr, Short[] arr2) {
        //Creem un array que abarqui les relacions d'ambdues taules
        int totalLength = lengthWithoutNulls(arr) + lengthWithoutNulls(arr2);
        bestPaths [i][j] = new short[totalLength];
        bestPaths [j][i] = new short[totalLength];
        int u = 0;
        for (Short s: arr) {
            if (s != null) {
                bestPaths[i][j][u] = s;
                bestPaths[j][i][u] = s;
                u++;
            }
        }
        for (Short s: arr2) {
            if (s != null) {
                bestPaths[i][j][u] = s;
                bestPaths[j][i][u] = s;
                u++;
            }
        }
    }

    public int setArrayValuesWithBasicShortAndObjectShort (short i, short j, short[] arr, short [] arr2) {
        //Creem un array que abarqui les relacions d'ambdues taules
        int totalLength = arr.length + arr2.length;

        short [] mergedArr = new short[totalLength];
        int u = 0;
        for (Short s: arr) {
            if (s != null) {
                mergedArr[u] = s;
                u++;
            }
        }
        for (Short s: arr2) {
            if (s != null) {
                mergedArr[u] = s;
                u++;
            }
        }
        Arrays.sort(mergedArr);
        ArrayList<Short> mergedListWithoutDuplicates = new ArrayList<>();
        u = 1;
        mergedListWithoutDuplicates.add(mergedArr[0]);
        while(u < mergedArr.length) {
            if (mergedArr[u-1] != mergedArr[u]) {
                mergedListWithoutDuplicates.add(mergedArr[u]);
            }
            u++;
        }
        short [] result = new short[mergedListWithoutDuplicates.size()];
        u = 0;
        for (Short item:mergedListWithoutDuplicates) {
            result[u++] = item;
        }
        bestPaths[i][j] = result;
        return result.length;
    }

    private int lengthWithoutNulls (Short [] arr) {
        ArrayList l = new ArrayList(Arrays.asList(arr));
        l.removeAll(Collections.singleton(null));
        return l.size();
    }
}
