package Utils;

import java.util.List;

public class ByteTreatment {
    public static long convertArrayToLong (short[] arrayToConvert) {
        long value = 0;
        for (int item: arrayToConvert) {
            value = value | (1L << item);
        }
        return value;
    }

    public static long convertListIntegerToLong (List<Integer> arrayToConvert) {
        long value = 0;
        for (int item: arrayToConvert) {
            value = value | (1L << item);
        }
        return value;
    }
}
