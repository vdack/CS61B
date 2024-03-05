package deque;

public interface Deque <T>{

    public void addFirst(T element);
    public T getFirst();
    public T removeFirst();
    public void addLast(T element);
    public T getLast();
    public T removeLast();

    public int size();
    default public boolean isEmpty(){
        return size() == 0;
    }
    public T get(int index);

    default public void printDeque(){
        for (int i = 0; i < this.size(); i++){
            System.out.print(this.get(i) + " ");
        }
        System.out.println("");
    }

    @Override
    public boolean equals(Object o);
}
