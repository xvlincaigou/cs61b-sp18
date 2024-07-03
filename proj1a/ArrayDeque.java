public class ArrayDeque<T>{
    private T array[];
    private int size;
    private int first;
    private int last;
    public ArrayDeque(){
        array = (T[])new Object[8];
        size = 0;
        first = 4;
        last = 3;
    }
    public int size(){
        return size;
    }
    public T get(int index){
        if((index <= size - 1)&&(index >= 0)){
            return array[index + first];
        }
        return null;
    }
    public boolean isEmpty(){
        if(size == 0){
            return true;
        }
        return false;
    }
    public void printDeque(){
        for(int i = 0;i < size;++ i){
            System.out.print(array[i + first] + " ");
        }
        System.out.println("");
    }
    private void resize(int i){
        T[] _array = (T[])new Object[array.length * 2];
        if(i == 0){
            System.arraycopy(array, first, _array, first, size);
        }else{
            System.arraycopy(array, first, _array, array.length + first, size);
            first += array.length;
            last += array.length;
        }
        array = _array;
    }
    public void addLast(T p){
        if(last == array.length - 1){
            resize(0);
        }
        array[last + 1] = p;
        size ++;
        last ++;
    }
    public void addFirst(T p){
        if(first == 0){
            resize(1);
        }
        array[first - 1] = p;
        first --;
        size ++;
    }
    private void shrink(){
        T[] _array = (T[])new Object[size * 2];
        System.arraycopy(array, first, _array, (int)(size / 2), size);
        array = _array;
        first = (int)(size / 2);
        last = first + size - 1;
    }
    public T removeLast(){
        if(size > 0){
            T p = array[last];
            if((size * 4 <= array.length)&&(array.length > 16)){
                shrink();
            }    
            size --;
            last --;
            return p;
        }
        return null;
    }
    public T removeFirst(){
        if(size > 0){
            T p = array[first];
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