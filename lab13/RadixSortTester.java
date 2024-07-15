import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class RadixSortTester {

    @Test
    public void testSort_Simple() {
        String[] input = {"c", "a", "b"};
        String[] expected = {"a", "b", "c"};
        String[] result = RadixSort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testSort_EmptyArray() {
        String[] input = {};
        String[] expected = {};
        String[] result = RadixSort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testSort_VaryingLengths() {
        String[] input = {"apple", "app", "banana", "ban", "orange"};
        String[] expected = {"app", "apple", "ban", "banana", "orange"};
        String[] result = RadixSort.sort(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testSort_NonDestructive() {
        String[] input = {"c", "a", "b"};
        String[] original = Arrays.copyOf(input, input.length);
        RadixSort.sort(input);
        assertArrayEquals("Original array should not be modified.", original, input);
    }
}