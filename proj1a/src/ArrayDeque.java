public class ArrayDeque<Item>{
    Item array[];
    int size;
    public ArrayDeque(){
        array = (Item[])new Object[8];
        size = 0;
    }
    private void resize(int i){
        Item[] _array = (Item[])new Object[size * 2];
        System.arraycopy(array, 0, _array, i, size);
        array = _array;
    }
    private void shrink(int i){
        Item[] _array = (Item[])new Object[size];
        System.arraycopy(array, i, _array, 0, size - i);
        array = _array;
    }
    public int size(){
        return size;
    }
    public Item get(int index){
        if(index < size){
            return array[index];
        }
        return null;
    }
    public void addLast(Item p){
        if(size == array.length){
            resize(0);
        }
        array[size] = p;
        size ++;
    }
    public void removeLast(){
        if(size > 0){
            if((size * 4 > array.length)||(array.length <= 16)){
                size --;
            }else{
                shrink(0);
                size --;
            }
        }
    }
    public void addFirst(Item p){
        if(size == array.length){
            resize(1);
        }
        array[0] = p;
        size ++;
    }
    public void removeFirst(){
        if(size > 0){
            if((size * 4 > array.length)||(array.length <= 16)){
                size --;
            }else{
                shrink(1);
                size --;
            }
        }
    }
}