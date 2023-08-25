import static org.junit.Assert.*;
import org.junit.Test;
public class TestArrayDequeGold {
    @Test
    public void testArrayDequeGold(){
        ArrayDequeSolution<Integer> p = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> q = new StudentArrayDeque<>();
        String bug = new String();
        for(int i = 0;i < 1000;++ i){
            Integer x = StdRandom.uniform(200);
            if(x < 100){
                p.addLast(x);
                q.addLast(x);
                bug = bug + "addLast(" + x + ")\n";
            }else{
                p.addFirst(x);
                q.addFirst(x);
                bug = bug + "addFirst(" + x + ")\n";
            }
        }
        for(int i = 0;i < 500;++ i){
            bug = bug + "removeLast()\n";
            assertEquals(bug,p.removeLast(),q.removeLast());
        }
    }
}
