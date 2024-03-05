package deque;

import java.util.Iterator;

public class LinkedListDeque<Item> implements  Deque<Item>{
    private class Node {
        /*
        from left to right
         */
        Item val;
        Node leftNode;
        Node rightNode;
        public Node(){
            val = null;
            leftNode = null;
            rightNode = null;
        }
        public Node(Item value){
            this.val = value;
            leftNode = null;
            rightNode = null;
        }

        public Node(Item value, Node lnode, Node rnode){
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
    public void addFirst(Item element) {
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
    public void addLast(Item element) {
        Node left = sentinel.leftNode;
        Node right = sentinel;
        Node current = new Node(element, left, right);
        size += 1;
    }

    @Override
    public Item getFirst() {
        return sentinel.rightNode.val;
    }

    @Override
    public Item getLast() {
        return sentinel.leftNode.val;
    }

    @Override
    public Item removeFirst() {
        if (size == 0)return null;
        Node tempNode = sentinel.rightNode;
        sentinel.rightNode = tempNode.rightNode;
        tempNode.rightNode.leftNode = sentinel;
        size -= 1;
        return tempNode.val;
    }
    @Override
    public Item removeLast() {
        if (size == 0)return null;
        Node tempNode = sentinel.leftNode;
        sentinel.leftNode = tempNode.leftNode;
        tempNode.leftNode.rightNode = sentinel;
        size -= 1;
        return tempNode.val;
    }

    public Item getRecursive(int index){
        if (index < 0 || index >= this.size())return null;
        return getRecurHelper(sentinel.rightNode, index);
    }
    private Item getRecurHelper(Node currentNode, int counter){
        if (counter == 0)return currentNode.val;
        return getRecurHelper(currentNode.rightNode, counter - 1);
    }
    @Override
    public Item get(int index) {
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
            Item e1 = this.get(i);
            Item e2 = (Item) ((Deque<?>) o).get(i);
            if (!e1.equals(e2))return false;
        }
        return true;
    }

    private class LLDIterator implements Iterator<Item>{
        int posi;
        public LLDIterator(){
            posi = 0;
        }
        @Override
        public boolean hasNext() {
            return posi < size;
        }

        @Override
        public Item next() {
            return get(posi);
        }
    }
    public Iterator<Item> iterator(){
        return new LLDIterator();
    }

}
