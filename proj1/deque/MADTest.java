package deque;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Comparator;

public class MADTest {
    @Test
    public void TestInteger(){
        class normalComparator implements Comparator<Integer>{
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 > o2) return 1;
                if (o1 < o2) return -1;
                return 0;
            }
        }
        class absComparator implements Comparator<Integer>{
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 * o1 > o2 * o2)return 1;
                if (o1 * o1 < o2 * o2)return -1;
                return 0;
            }
        }

        MaxArrayDeque<Integer> lst = new MaxArrayDeque<>(new absComparator());
        lst.addFirst(3);
        lst.addFirst(5);
        lst.addLast(-7);
        lst.addLast(1);
        lst.addFirst(10);
        lst.removeFirst();
        assertEquals("abs error!",-7, (Object) lst.max());
        assertEquals("normal error!", 5, (Object) lst.max(new normalComparator()));

    }
    @Test
    public void TestString(){
        class byFirst implements Comparator<String>{
            @Override
            public int compare(String o1, String o2) {
                char c1 = o1.charAt(0);
                char c2 = o2.charAt(0);
                if (c1 > c2) return 1;
                if (c1 < c2) return -1;
                return 0;
            }
        }
        class byLength implements Comparator<String>{
            @Override
            public int compare(String o1, String o2) {
                int l1 = o1.length();
                int l2 = o2.length();
                if (l1 > l2) return 1;
                if (l1 < l2) return -1;
                return 0;
            }
        }
        MaxArrayDeque<String> lst = new MaxArrayDeque<>(new byFirst());
        String temp = "aa b ccc dd e";
        String[] strs = temp.split(" ");
        for (int i = 0; i < strs.length; i++){
            lst.addLast(strs[i]);
        }
        assertEquals("byFirst", "e", lst.max());
        assertEquals("byLength", "ccc", lst.max(new byLength()));
    }
    @Test
    public void TestAB(){
        class ABNum{
            public int a;
            public int b;
            ABNum(int a, int b){
                this.a = a;
                this.b = b;
            }
            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof ABNum))return false;
                ABNum o = (ABNum) obj;
                return o.a == a && o.b == b;
            }
        }
        class byA implements Comparator<ABNum>{
            @Override
            public int compare(ABNum o1, ABNum o2) {
                if (o1.a > o2.a)return 1;
                if (o1.a < o2.a)return -1;
                return 0;
            }
        }
        class byB implements  Comparator<ABNum>{
            @Override
            public int compare(ABNum o1, ABNum o2) {
                if (o1.b > o2.b)return 1;
                if (o1.b < o2.b)return -1;
                return 0;
            }
        }
        class byAB implements Comparator<ABNum>{
            @Override
            public int compare(ABNum o1, ABNum o2) {
                if (o1.a + o1.b > o2.a + o2.b)return 1;
                if (o1.a + o1.b < o2.a + o2.b)return -1;
                return 0;
            }
        }
        assertEquals("ABNum Equals",new ABNum(3,4), new ABNum(3,4));
        MaxArrayDeque<ABNum> lst = new MaxArrayDeque<>(new byAB());
        lst.addLast(new ABNum(1,1));
        lst.addLast(new ABNum(5,-7));
        lst.addLast(new ABNum(3,0));
        lst.addLast(new ABNum(4,2));
        lst.addLast(new ABNum(-6,3));
        assertEquals("byAB", new ABNum(4,2), lst.max());
        assertEquals("byA", new ABNum(5,-7),lst.max(new byA()));
        assertEquals("byA", new ABNum(-6,3),lst.max(new byB()));

    }

    @Test
    public void TestAnimal(){
        class Animal{
            public String name;
            public int size;
            private int id;
            Animal(int d, int s, String n){
                id = d;
                size = s;
                name = n;
            }

            @Override
            public boolean equals(Object obj) {
                if(!(obj instanceof Animal)){
                    return false;
                }
                Animal other = (Animal) obj;
                return id == other.id;
            }
        }
        class Cat extends Animal{
//            String name;
            public Cat(int id, int size, String name){
                super(id, size, name);
//                name = "cat";
            }

        }
        class Dog extends Animal{
//            String name;
            public Dog (int id, int size, String name){
                super(id, size, name);
//                name = "dog";
            }
        }

        class bySize implements Comparator<Animal>{
            @Override
            public int compare(Animal o1, Animal o2) {
                if (o1.size > o2.size)return 1;
                if (o1.size < o2.size)return -1;
                return 0;
            }
        }
        class byName implements Comparator<Animal>{
            @Override
            public int compare(Animal o1, Animal o2) {
                if (o1.name.charAt(0) > o2.name.charAt(0))return -1;
                if (o1.name.charAt(0) < o2.name.charAt(0) )return 1;
                return 0;
            }
        }

        Animal animal1 = new Animal(1,7,"a");
        Animal animal2 = new Animal(1,5,"pp");
        Cat cat1 = new Cat(2,4,"c");
        Animal cat2 = new Cat(3,9,"b");
        Dog dog1 = new Dog(4, 20,"d");
        Animal dog2 = new Dog(5,10,"e");
        MaxArrayDeque<Animal> lst = new MaxArrayDeque<>(new bySize());
        assertEquals("Animal Equals",animal1,animal2);
        lst.addLast(animal1);
        lst.addLast(cat1);
        lst.addLast(cat2);
        assertEquals("bySize No dog",new Animal(3,0,"cat"),lst.max());
        assertEquals("byName No dog",new Animal(1,0,"animal"),lst.max(new byName()));
        lst.removeFirst();
        lst.addLast(dog1);
        lst.addLast(dog2);
        assertEquals("bySize with dog",new Animal(4,0,"dog"),lst.max());
        assertEquals("byName with dog",new Animal(3,0,"cat"),lst.max(new byName()));

    }
}
