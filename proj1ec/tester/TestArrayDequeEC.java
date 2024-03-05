package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;
public class TestArrayDequeEC {
    @Test
    public void mainTest(){
        ArrayDequeSolution<Integer> solutionDeque = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();

        double opcode;
        int N = 50000;
        for (int i = 0; i < N ; i++){
            opcode = StdRandom.uniform();
            if (opcode < 0.4){
                if (opcode < 0.12){
                    Integer num1 = i;
                    Integer num2 = i;
                    solutionDeque.addFirst(num1);
                    studentDeque.addFirst(num2);
                    System.out.println("addFirst(" + i + ")");
                }
                else if (opcode < 0.24){
                    Integer num1 = i;
                    Integer num2 = i;
                    solutionDeque.addLast(num1);
                    studentDeque.addLast(num2);
                    System.out.println("addLast(" + i + ")");
                }
                else if (opcode < 0.35){
                    System.out.println("size()");
                    assertEquals(solutionDeque.size(),studentDeque.size());
                }
                else {
                    System.out.println("isEmpty()");
                    assertEquals(solutionDeque.isEmpty(), studentDeque.isEmpty());
                }
            }
            else{
                if (solutionDeque.isEmpty())continue;
                if(opcode < 0.52){
                    System.out.println("removeFirst()");
                    assertEquals(solutionDeque.removeFirst(),studentDeque.removeFirst());
                }
                else if (opcode < 0.64){
                    System.out.println("removeLast()");
                    assertEquals(solutionDeque.removeLast(),studentDeque.removeLast());
                }
                else {
                    int index = StdRandom.uniform(0,solutionDeque.size());
                    System.out.println("get(" + index + ")");
                    assertEquals(solutionDeque.get(index), studentDeque.get(index));
                }
            }
        }
    }
}
