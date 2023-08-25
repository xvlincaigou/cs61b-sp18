import static org.junit.Assert.*;
import org.junit.Test;
public class TestArrayDequeGold {
    @Test
    public void testArrayDequeGold(){
        ArrayDequeSolution<Integer> p = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> q = new StudentArrayDeque<>();
        for(int i = 0;i < 1000;++ i){
            Integer x = StdRandom.uniform(200);
            if(x < 100){
                p.addLast(x);
                q.addLast(x);
                assertEquals("addLast(" + x + ")\n",x,x);
            }else{
                p.addFirst(x);
                q.addFirst(x);
                assertEquals("addFirst(" + x + ")\n",x,x);
            }
        }
        for(int i = 0;i < 500;++ i){
            assertEquals("removeLast()\n" + i,p.removeLast(),q.removeLast());
        }
    }
}
