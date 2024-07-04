package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        int[] buckets = new int[M];
        for (Oomage o : oomages) {
            buckets[(o.hashCode() & 0x7FFFFFFF) % M] += 1;
        }
        for (int i : buckets) {
            if (i > oomages.size() / 2.5 || i < oomages.size() / 50) {
                return false;
            }
        }
        return true;
    }
}
