package flik;
import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import net.sf.saxon.trans.SymbolicName;
import org.junit.Test;
public class FlikTest {

    @Test
    public void TestOneToNine(){
        for (int i = -10; i < 10; i++){
            Integer numA = i;
            Integer numB = i;
            Integer numC = i+1;
            assertTrue(Flik.isSameNumber(numA,numB));
            assertFalse(Flik.isSameNumber(numA,numC));
        }
    }

    @Test
    public void TestGreaterThanThousand(){
        for (int i = 1; i < 30; i+=7){
            Integer x = i * 1453;
            Integer y = i * 1453;
            Integer z = i * 1644;
            assertTrue(Flik.isSameNumber(x,y));
            assertFalse(Flik.isSameNumber(y,z));
        }
    }

    @Test
    public void TestRandom(){
        for (int i = 0 ;i < 64; i++){
            int n = StdRandom.uniform(14530529);
            if (n % 2 == 0){
                Integer x = n;
                Integer y = n;
                x = x + 1;
                y = y + 1;
                assertTrue(Flik.isSameNumber(x, y));
            } else{
                Integer x = n;
                Integer y = n;
                x = x - 1;
                y = y + 1;
                assertFalse(Flik.isSameNumber(x, y));
            }
        }
    }
}
