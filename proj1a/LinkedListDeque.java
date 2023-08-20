public class LinkedListDeque<Item>{
    public class Node{
        public Item info;
        public Node next;
        public Node prev;
        public Node(Item _info,Node _next,Node _prev){
            info = _info;
            next = _next;
            prev = _prev;
        }
    }

    Node head;
    Node tail;
    int size;

    public LinkedListDeque(){
        head = new Node(null,null,null);
        tail = new Node(null,head,head);
        head.next = tail;
        head.prev = tail;
        size = 0;
    }
    public void addFirst(Item p){
        Node tmp = new Node(p,head.next,head);
        head.next = tmp;
        tmp.next.prev = tmp;
        size ++;
    }
    public void addLast(Item p){
        Node tmp = new Node(p,tail,tail.prev);
        tail.prev = tmp;
        tmp.prev.next = tmp;
        size ++;
    }
    public boolean isEmpty(){
        if(size == 0){
            return true;
        }
        return false;
    }
    public int size(){
        return size;
    }
    public void printDeque(){
        for(Node p = head.next;p != tail;p = p.next){
            System.out.print(p.info + " ");
        }
        System.out.println("");
    }
    public Item removeFirst(){
        if(!isEmpty()){
            Node p = head.next;
            head.next = p.next;
            p.next.prev = head;
            size --;
            return p.info;
        }
        return null;
    }
    public Item removeLast(){
        if(!isEmpty()){
            Node p = tail.prev;
            tail.prev = p.prev;
            p.prev.next = tail;
            size --;
            return p.info;
        }
        return null;
    }
    public Item get(int index){
        if(index < size){
            Node p = head.next;
            for(int i = 0;i < index;++ i){
                p = p.next;
            }
            return p.info;
        }        
        return null;
    }
    private Item getRecursiveHelper(int index,Node p){
        if(index == 0){
            return p.info;
        }
        return getRecursiveHelper(index - 1,p.next);
    }
    public Item getRecursive(int index){
        if(index < size){
            return getRecursiveHelper(index,head.next);
        }
        return null;
    }
}