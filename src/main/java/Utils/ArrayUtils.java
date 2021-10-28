package Utils;

public class ArrayUtils {
    public static boolean shortArrayContains (short [] shortarray, short valueToSearch) {
        for (short s: shortarray) {
            if (s == valueToSearch) {
                return true;
            }
        }
        return false;
    }
}
