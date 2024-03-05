package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T> ,Iterable<T>{
    private int size;
    private T[] innerArray;
    private int head;
    private int tail;
    private int capacity;
    public ArrayDeque(){
        size = 0;
        head = 0;
        tail = 1;
        capacity = 8;
        innerArray = (T[]) new Object[capacity];

    }
    private void resize(){
        int nextCapacity;
        if (size < capacity/4 && capacity >=16){
            nextCapacity = capacity / 4;
        }
        else{
            nextCapacity = capacity * 2;
        }
        T[] a = (T[]) new Object[nextCapacity];
        for (int i = 0; i < size; i++){
            a[i] = get(i);
        }
        innerArray = a;
        capacity = nextCapacity;
        head = (capacity-1)%capacity;
        tail = size;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addFirst(T element) {
        if (size == capacity) resize();
        innerArray[(head + capacity) % capacity] = element;
        head = (head + capacity - 1) % capacity;
        size += 1;
    }

    @Override
    public void addLast(T element) {
        if (size == capacity) resize();
        innerArray[(tail + capacity) % capacity] = element;
        tail = (tail + capacity + 1) % capacity;
        size += 1;
    }

    @Override
    public T removeFirst() {
        if (size == 0) return null;
        int headPosition = (head + capacity + 1) % capacity;
        T first = innerArray[headPosition];
        innerArray[headPosition] = null;
        head = headPosition;
        size -= 1;
        if (size < capacity/4 && capacity >=16)resize();
        return first;
    }

    @Override
    public T removeLast() {
        if (size == 0) return null;
        int tailPosition = (tail + capacity - 1) % capacity;
        T last = innerArray[tailPosition];
        innerArray[tailPosition] = null;
        tail = tailPosition;
        size -= 1;
        if (size < capacity/4 && capacity >=16)resize();
        return last;
    }

    @Override
    public T get(int index) {
        int position = (head + 1 + index) % capacity;
        return innerArray[position];
    }

    @Override
    public T getFirst() {
        return get(0);
    }

    @Override
    public T getLast() {
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
            T e1 = this.get(i);
            T e2 = (T) ((Deque<?>) o).get(i);
            if (!e1.equals(e2))return false;
        }
        return true;
    }

    private class ADIterator implements Iterator<T>{
        int offset;
        public ADIterator(){
            offset = 0;
        }

        @Override
        public boolean hasNext() {
            return offset < size;
        }

        @Override
        public T next() {
            T element = get(offset);
            offset += 1;
            return element;
        }
    }
    public Iterator<T> iterator(){
        return new ADIterator();
    }
    @Override
    public void printDeque(){
        for (int i = 0; i < this.size(); i++){
            System.out.print(this.get(i) + " ");
        }
        System.out.println("");
    }
}
