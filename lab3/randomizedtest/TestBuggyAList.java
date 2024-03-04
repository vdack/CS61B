package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  @Test
    public void AddThreeRemoveThree(){
      BuggyAList<Integer> baList = new BuggyAList<>();
      AListNoResizing<Integer> naList = new AListNoResizing<>();
      for (int i = 0 ; i < 3; i++){
          baList.addLast(i);
          naList.addLast(i);
//          assertEquals(baList.toString(), naList.toString());
      }

      for (int i = 0 ; i < 3; i++){
          assertEquals(baList.removeLast(), naList.removeLast());
      }
  }

  @Test
    public void RandomizedTest(){
      AListNoResizing<Integer> L = new AListNoResizing<>();
      BuggyAList<Integer> baL = new BuggyAList<>();
      int N = 50000;
      for (int i = 0; i < N; i += 1) {
          int operationNumber = StdRandom.uniform(0, 5);
          if (operationNumber == 0) {
              // addLast
              int randVal = StdRandom.uniform(0, 100);
              L.addLast(randVal);
              baL.addLast(randVal);
//              System.out.println("addLast(" + randVal + ")");
          } else if (operationNumber == 1) {
              // size
              int size = L.size();
              assertEquals(L.size(), baL.size());
//              System.out.println("size: " + size);
          }
          else if (operationNumber == 2 && L.size() > 0){
              //getLast
              int last1 = L.getLast();
              int last2 = baL.getLast();
              assertEquals(last1, last2);
//              System.out.println("Last Value: "+last1);
          }
          else if (operationNumber == 4 && L.size() > 0){
              //remove Last
              int last1 = L.removeLast();
              int last2 = baL.removeLast();
              assertEquals(last1, last2);
//              System.out.println("Remove Last: "+last1);
          }
      }
  }
}
