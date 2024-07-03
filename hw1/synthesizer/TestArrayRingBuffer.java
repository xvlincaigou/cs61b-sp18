package synthesizer;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

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

    @Test
    public void testIterator() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(5);
        arb.enqueue(1);
        arb.enqueue(2);
        arb.enqueue(3);

        Iterator<Integer> it = arb.iterator();
        assertTrue("Iterator should have a next element", it.hasNext());
        assertEquals("First element should be 1", Integer.valueOf(1), it.next());
        assertTrue("Iterator should have a next element after first", it.hasNext());
        assertEquals("Second element should be 2", Integer.valueOf(2), it.next());
        assertTrue("Iterator should have a next element after second", it.hasNext());
        assertEquals("Third element should be 3", Integer.valueOf(3), it.next());
        assertFalse("Iterator should not have a next element after third", it.hasNext());
    }
    
    @Test
    public void testIteratorWithWrapAround() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(3);
        arb.enqueue(1);
        arb.enqueue(2);
        arb.dequeue(); // Remove 1, making space at the beginning
        arb.enqueue(3); // Wrap around, 3 should be at the beginning
        arb.enqueue(4); // Now the buffer is [3, 4, 2]

        Iterator<Integer> it = arb.iterator();
        assertTrue("Iterator should have a next element", it.hasNext());
        assertEquals("First element should be 2 (the oldest)", Integer.valueOf(2), it.next());
        assertTrue("Iterator should have a next element after first", it.hasNext());
        assertEquals("Second element should be 3", Integer.valueOf(3), it.next());
        assertTrue("Iterator should have a next element after second", it.hasNext());
        assertEquals("Third element should be 4", Integer.valueOf(4), it.next());
        assertFalse("Iterator should not have a next element after third", it.hasNext());
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
