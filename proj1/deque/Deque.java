package deque;

public interface Deque <Item>{

    public void addFirst(Item element);
    public Item getFirst();
    public Item removeFirst();
    public void addLast(Item element);
    public Item getLast();
    public Item removeLast();

    public int size();
    default public boolean isEmpty(){
        return size() == 0;
    }
    public Item get(int index);

    default public void printDeque(){
        for (int i = 0; i < this.size(); i++){
            System.out.print(this.get(i) + " ");
        }
        System.out.println("");
    }

    @Override
    public boolean equals(Object o);
}
