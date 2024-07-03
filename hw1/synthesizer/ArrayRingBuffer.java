package synthesizer;
import java.util.Iterator;

//TODO: Make sure to make this class and all of its methods public
public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T>{
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        first = 0;
        last = 0;
        fillCount = 0;
        this.capacity = capacity;
        rb = (T[]) new Object[this.capacity];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (fillCount == 0) {
            rb[last] = x;
            fillCount += 1;
        } else if (fillCount == this.capacity) {
            throw new RuntimeException("Ring Buffer Overflow");
        } else {
            last = (last + 1) % this.capacity;
            rb[last] = x;
            fillCount += 1;
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        if (fillCount == 0) {
            throw new RuntimeException("Ring Buffer Underflow");
        } else {
            T toDequeue = rb[first];
            first = (first + fillCount == 1 ? 0 : 1) % this.capacity;
            fillCount -= 1;
            return toDequeue;
        }
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        return rb[first];
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    public class ArrayRingBufferIterator implements Iterator<T>{
        private int pos;
        public ArrayRingBufferIterator() {
            pos = first;
        }

        @Override
        public boolean hasNext() {
            if (isEmpty())
                return false;
            return pos <= last && first <= last || first > last && (pos >= first || pos <= last);
        }

        public T next() {
            T toReturn = rb[pos];
            pos = (pos + 1) % capacity;
            return toReturn;
        }
    }
}
