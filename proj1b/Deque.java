public interface Deque<Item> {
    public int size();
    public Item get(int index);
    public boolean isEmpty();
    public void printDeque();
    public void addLast(Item p);
    public void addFirst(Item p);
    public Item removeLast();
    public Item removeFirst();
}
