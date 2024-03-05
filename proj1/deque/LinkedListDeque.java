package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T>{
    private class Node {
        /*
        from left to right
         */
        T val;
        Node leftNode;
        Node rightNode;
        public Node(){
            val = null;
            leftNode = null;
            rightNode = null;
        }
        public Node(T value){
            this.val = value;
            leftNode = null;
            rightNode = null;
        }

        public Node(T value, Node lnode, Node rnode){
            this.val = value;
            this.rightNode = rnode;
            rnode.leftNode = this;
            this.leftNode = lnode;
            leftNode.rightNode = this;
        }
    }

    Node sentinel;
    int size;
//    private final Class<Item> type;
    public LinkedListDeque(){
        sentinel = new Node();
        sentinel.leftNode = sentinel;
        sentinel.rightNode = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T element) {
        Node left = sentinel;
        Node right = sentinel.rightNode;
        Node current = new Node(element, left, right);
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void addLast(T element) {
        Node left = sentinel.leftNode;
        Node right = sentinel;
        Node current = new Node(element, left, right);
        size += 1;
    }

    @Override
    public T getFirst() {
        return sentinel.rightNode.val;
    }

    @Override
    public T getLast() {
        return sentinel.leftNode.val;
    }

    @Override
    public T removeFirst() {
        if (size == 0)return null;
        Node tempNode = sentinel.rightNode;
        sentinel.rightNode = tempNode.rightNode;
        tempNode.rightNode.leftNode = sentinel;
        size -= 1;
        return tempNode.val;
    }
    @Override
    public T removeLast() {
        if (size == 0)return null;
        Node tempNode = sentinel.leftNode;
        sentinel.leftNode = tempNode.leftNode;
        tempNode.leftNode.rightNode = sentinel;
        size -= 1;
        return tempNode.val;
    }

    public T getRecursive(int index){
        if (index < 0 || index >= this.size())return null;
        return getRecurHelper(sentinel.rightNode, index);
    }
    private T getRecurHelper(Node currentNode, int counter){
        if (counter == 0)return currentNode.val;
        return getRecurHelper(currentNode.rightNode, counter - 1);
    }
    @Override
    public T get(int index) {
        if (index < 0 || index >= this.size())return null;
        Node currentNode = sentinel.rightNode;
        while (index > 0){
            currentNode = currentNode.rightNode;
            index--;
        }
        return currentNode.val;
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

    private class LLDIterator implements Iterator<T>{
        int posi;
        public LLDIterator(){
            posi = 0;
        }
        @Override
        public boolean hasNext() {
            return posi < size;
        }

        @Override
        public T next() {
            T element = get(posi);
            posi += 1;
            return element;
        }
    }
    public Iterator<T> iterator(){
        return new LLDIterator();
    }

}
