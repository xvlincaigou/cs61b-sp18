/**
 * Class with 2 ways of doing Counting sort, one naive way and one "better" way
 *
 * @author Akhil Batra, Alexander Hwang
 *
 **/
public class CountingSort {
    /**
     * Counting sort on the given int array. Returns a sorted version of the array.
     * Does not touch original array (non-destructive method).
     * DISCLAIMER: this method does not always work, find a case where it fails
     *
     * @param arr int array that will be sorted
     * @return the sorted array
     */
    public static int[] naiveCountingSort(int[] arr) {
        // find max
        int max = Integer.MIN_VALUE;
        for (int i : arr) {
            max = max > i ? max : i;
        }

        // gather all the counts for each value
        int[] counts = new int[max + 1];
        for (int i : arr) {
            counts[i]++;
        }

        // when we're dealing with ints, we can just put each value
        // count number of times into the new array
        int[] sorted = new int[arr.length];
        int k = 0;
        for (int i = 0; i < counts.length; i += 1) {
            for (int j = 0; j < counts[i]; j += 1, k += 1) {
                sorted[k] = i;
            }
        }

        // however, below is a more proper, generalized implementation of
        // counting sort that uses start position calculation
        int[] starts = new int[max + 1];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        int[] sorted2 = new int[arr.length];
        for (int i = 0; i < arr.length; i += 1) {
            int item = arr[i];
            int place = starts[item];
            sorted2[place] = item;
            starts[item] += 1;
        }

        // return the sorted array
        return sorted;
    }

    /**
     * Counting sort on the given int array, must work even with negative numbers.
     * Note, this code does not need to work for ranges of numbers greater
     * than 2 billion.
     * Does not touch original array (non-destructive method).
     *
     * @param arr int array that will be sorted
     */
    public static int[] betterCountingSort(int[] arr) {
        // find max
        int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE, count = 0, negCount = 0;
        for (int i : arr) {
            max = max > i ? max : i;
            min = min < i ? min : i;
            if (i < 0)
                negCount++;
            else
                count++;
        }

        // if all values are non-negative, we can use the naive counting sort
        if (min >= 0)
            return naiveCountingSort(arr);

        // gather all the counts for each value
        int[] counts = new int[max + 1], negCounts = new int[Math.abs(min) + 1];
        for (int i : arr) {
            if (i < 0)
                negCounts[Math.abs(i)]++;
            else
                counts[i]++;
        }

        int[] starts = new int[max + 1], negStarts = new int[Math.abs(min) + 1];
        int pos = 0, negPos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }
        for (int i = 0; i < negStarts.length; i += 1) {
            negStarts[i] = negPos;
            negPos += negCounts[i];
        }

        int[] sorted = new int[count], negSorted = new int[negCount];
        for (int i = 0; i < arr.length; i += 1) {
            int item = arr[i];
            if (item < 0) {
                int place = negStarts[Math.abs(item)];
                negSorted[place] = item;
                negStarts[Math.abs(item)] += 1;
            } else {
                int place = starts[item];
                sorted[place] = item;
                starts[item] += 1;
            }
        }

        int[] result = new int[arr.length];
        for (int i = 0; i < negSorted.length; i += 1) {
            result[i] = negSorted[negSorted.length - 1 - i];
        }
        for (int i = 0; i < sorted.length; i += 1) {
            result[i + negSorted.length] = sorted[i];
        }

        return result;
    }
}
