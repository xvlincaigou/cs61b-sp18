import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int maxLen = 0;
        for (String s : asciis)
            maxLen = Math.max(maxLen, s.length());
        String[] sorted = Arrays.copyOf(asciis, asciis.length);
        for (int i = maxLen - 1; i >= 0; i--)
            sortHelperLSD(sorted, i);
        return sorted;
    }

    private static int charAtOrMinChar(String s, int index) {
        if (index < s.length())
            return s.charAt(index);
        return 0;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param sorted Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] sorted, int index) {
        // Optional LSD helper method for optional LSD radix sort
        int[] counts = new int[257];
        for (String s : sorted)
            counts[charAtOrMinChar(s, index)]++;

        int[] starts = new int[257];
        int pos = 0;
        for (int i = 0; i < counts.length; i++) {
            starts[i] = pos;
            pos += counts[i];
        }
        String[] aux = new String[sorted.length];
        for (String s : sorted) {
            int i = starts[charAtOrMinChar(s, index)];
            aux[i] = s;
            starts[charAtOrMinChar(s, index)]++;
        }
        System.arraycopy(aux, 0, sorted, 0, sorted.length);
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
