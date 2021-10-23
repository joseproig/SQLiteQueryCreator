package Utils;

public class ByteTreatment {
    public static long convertArrayToLong (short[] arrayToConvert) {
        long value = 0;
        for (int item: arrayToConvert) {
            value = value | (1 << item);
        }
        return value;
    }
}
