package synthesizer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    private ArrayRingBuffer<Integer> arb;

    @Before
    public void setUp() {
        arb = new ArrayRingBuffer<>(4);
    }

    @Test
    public void testEnqueueDequeue() {
        arb.enqueue(1);
        assertEquals("Enqueue 1 into the buffer", Integer.valueOf(1), arb.dequeue());
        
        arb.enqueue(2);
        arb.enqueue(3);
        assertEquals("Enqueue 2 then 3 into the buffer, dequeue should return 2", Integer.valueOf(2), arb.dequeue());
    }

    @Test(expected = RuntimeException.class)
    public void testEnqueueOverflow() {
        for (int i = 0; i < 5; i++) { // Attempt to enqueue more elements than capacity
            arb.enqueue(i);
        }
    }

    @Test(expected = RuntimeException.class)
    public void testDequeueUnderflow() {
        arb.dequeue(); // Attempt to dequeue from an empty buffer
    }

    @Test
    public void testPeek() {
        arb.enqueue(1);
        arb.enqueue(2);
        assertEquals("Peek should return the first element enqueued", Integer.valueOf(1), arb.peek());
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
