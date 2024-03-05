package deque;

import java.util.Comparator;

public class Laucher {
    public static void main(String[] args){
        Comparator<Integer> c = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 > o2)return 1;
                if (o1 < o2)return -1;
                return 0;
            }
        };
        Comparator<Integer> abs = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1*o1 > o2*o2)return 1;
                if (o1*o1 < o2*o2)return -1;
                return 0;
            }
        };

        MaxArrayDeque<Integer> lst = new MaxArrayDeque<>(c);
        lst.addFirst(-3);
        lst.addFirst(-2);
        lst.addFirst(3);
        lst.addFirst(-4);
        lst.addFirst(1);
        lst.addFirst(0);
        System.out.println("max is " + lst.max() + " and absMax is " + lst.max(abs));
    }
}
