public class ArrayDeque<Item> implements Deque<Item>{
    private Item array[];
    private int size;
    private int first;
    private int last;
    public ArrayDeque(){
        array = (Item[])new Object[8];
        size = 0;
        first = 4;
        last = 3;
    }
    @Override
    public int size(){
        return size;
    }
    @Override
    public Item get(int index){
        if((index <= size - 1)&&(index >= 0)){
            return array[index + first];
        }
        return null;
    }
    @Override
    public boolean isEmpty(){
        if(size == 0){
            return true;
        }
        return false;
    }
    @Override
    public void printDeque(){
        for(int i = 0;i < size;++ i){
            System.out.print(array[i + first] + " ");
        }
        System.out.println("");
    }
    private void resize(int i){
        Item[] _array = (Item[])new Object[array.length * 2];
        if(i == 0){
            System.arraycopy(array, first, _array, first, size);
        }else{
            System.arraycopy(array, first, _array, array.length + first, size);
            first += array.length;
            last += array.length;
        }
        array = _array;
    }
    @Override
    public void addLast(Item p){
        if(last == array.length - 1){
            resize(0);
        }
        array[last + 1] = p;
        size ++;
        last ++;
    }
    @Override
    public void addFirst(Item p){
        if(first == 0){
            resize(1);
        }
        array[first - 1] = p;
        first --;
        size ++;
    }
    private void shrink(){
        Item[] _array = (Item[])new Object[size * 2];
        System.arraycopy(array, first, _array, (int)(size / 2), size);
        array = _array;
        first = (int)(size / 2);
        last = first + size - 1;
    }
    @Override
    public Item removeLast(){
        if(size > 0){
            Item p = array[last];
            if((size * 4 <= array.length)&&(array.length > 16)){
                shrink();
            }    
            size --;
            last --;
            return p;
        }
        return null;
    }
    @Override
    public Item removeFirst(){
        if(size > 0){
            Item p = array[first];
            if((size * 4 <= array.length)&&(array.length > 16)){
                shrink();
            }
            size --;
            first ++;
            return p;
        }
        return null;
    }
}