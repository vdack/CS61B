package deque;

import java.util.Iterator;

public class ArrayDeque<Item> implements Deque<Item>{
    int size;
    Item[] innerArray;
    int head;
    int tail;
    int capacity;
    public ArrayDeque(){
        size = 0;
        head = 0;
        tail = 1;
        capacity = 8;
        innerArray = (Item[]) new Object[capacity];

    }
    private void resize(){
        if (size < capacity/4 && capacity >=16){
            capacity /= 4;
        }
        else{
            capacity *= 2;
        }
        Item[] a = (Item[]) new Object[capacity];
        System.arraycopy(innerArray, head, a, 0, size);
        innerArray = a;
        head = 0;
        tail = size;
    }
//    private int getHeadPosition(){
//        return (head+capacity)%capacity;
//    }
//    private int getTailPosition(){
//        return (tail+capacity)%capacity;
//    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(Item element) {
        if (size == capacity) resize();
//        if (size == 0){
//            tail += 1;
//        }
        innerArray[(head + capacity) % capacity] = element;
        head = (head + capacity - 1) % capacity;
        size += 1;
    }

    @Override
    public void addLast(Item element) {
        if (size == capacity) resize();
//        if (size == 0){
//            head -= 1;
//        }
        innerArray[(tail + capacity) % capacity] = element;
        tail = (tail + capacity + 1) % capacity;
        size += 1;
    }

    @Override
    public Item removeFirst() {
        if (size == 0) return null;
        int headPosition = (head + capacity + 1) % capacity;
        Item first = innerArray[headPosition];
        innerArray[headPosition] = null;
        head = headPosition;
        size -= 1;
        if (size < capacity/4 && capacity >=16)resize();
        return first;
    }

    @Override
    public Item removeLast() {
        if (size == 0) return null;
        int tailPosition = (tail + capacity - 1) % capacity;
        Item last = innerArray[tailPosition];
        innerArray[tailPosition] = null;
        tail = tailPosition;
        size -= 1;
        if (size < capacity/4 && capacity >=16)resize();
        return last;
    }

    @Override
    public Item get(int index) {
        int position = (head + 1 + index) % capacity;
        return innerArray[position];
    }

    @Override
    public Item getFirst() {
        return get(0);
    }

    @Override
    public Item getLast() {
        return get(size - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        if (!(o instanceof Deque))return false;
        if (((Deque<?>) o).size() != this.size())return false;
        if (this.size() == 0) return true; //HERE I AM NOT SURE.
        for (int i = 0; i < this.size(); i++){
            Item e1 = this.get(i);
            Item e2 = (Item) ((Deque<?>) o).get(i);
            if (!e1.equals(e2))return false;
        }
        return true;
    }

    private class ADIterator implements Iterator<Item>{
        int offset;
        public ADIterator(){
            offset = 0;
        }

        @Override
        public boolean hasNext() {
            return offset < size;
        }

        @Override
        public Item next() {
            return get(offset);
        }
    }
    public Iterator<Item> iterator(){
        return new ADIterator();
    }
}
