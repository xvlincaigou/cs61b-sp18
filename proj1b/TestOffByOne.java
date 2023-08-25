import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();
    @Test
    public void testOffByOne(){
        assertTrue(offByOne.equalChars('a','b'));
        assertFalse(offByOne.equalChars('a','y'));
        assertTrue(offByOne.equalChars('r','q'));
        assertTrue(offByOne.equalChars('f','e'));
        assertTrue(offByOne.equalChars('l','k'));
        assertTrue(offByOne.equalChars('<','='));
        assertFalse(offByOne.equalChars('A','b'));
    }
}
