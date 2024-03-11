package bstmap;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{
    //here are methods do not need to implement.
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
//        if (this.root == null) {
//            return null;
//        }
//        btNode parentNode = root.findParentNode(key);
//        btNode foundNode = parentNode.leftNode;
//        if (foundNode != null && foundNode.key.equals(key)) {
//            parentNode.leftNode = null
//        }

    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    private class BSTIterator implements Iterator<K> {
        List<K> lst;
        int posi;
        public BSTIterator(){
            posi = 0;
            if (root == null) {
                lst = new ArrayList<>();
                return;
            }
            lst = root.depthSearchKey();

        }
        @Override
        public boolean hasNext() {
            return posi < lst.size();
        }

        @Override
        public K next() {
            posi += 1;
            return lst.get(posi - 1);
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTIterator();
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        if (this.root == null) {
            return set;
        }
        List<K> lst = this.root.depthSearchKey();
        for (int i = 0; i < lst.size(); i++) {
            set.add(lst.get(i));
        }
        return set;
    }

    //here are the methods to be implemented.

    private class btNode{
        K key;
        V val;
        public btNode parentNode;
        public btNode leftNode;
        public btNode rightNode;

        public btNode(K key, V value){
            this.key = key;
            this.val = value;
            this.parentNode = null;
            this.leftNode = null;
            this.rightNode = null;
        }
        public void addToLeft(btNode node){
//            if (this.leftNode != null) throw new RuntimeException("add to an existing left leaf!\n");
            this.leftNode = node;
            node.parentNode = this;
        }
        public void addToRight(btNode node){
//            if (this.rightNode != null) throw new RuntimeException("add to an existing right leaf!\n");
            this.rightNode = node;
            node.parentNode = this;
        }

        public btNode findParentNode(K keyToFind){
            if (this.key.compareTo(keyToFind) == 0) {
                return this.parentNode;
            }
            btNode nextNode = this.leftNode;
            if (this.key.compareTo(keyToFind) < 0) {
                nextNode = this.rightNode;
            }
            if (nextNode == null) {
                return this;
            }
            return nextNode.findParentNode(keyToFind);
        }
        public btNode findNode(K keyToFind){
            if (this.key.compareTo(keyToFind) == 0) {
                return this;
            }
            btNode nextNode = this.leftNode;
            if (this.key.compareTo(keyToFind) < 0) {
                nextNode = this.rightNode;
            }
            if (nextNode == null) {
                return null;
            }
            return nextNode.findNode(keyToFind);
        }

        public List<K> depthSearchKey(){
            ArrayList<K> lst = new ArrayList<>();
            if (this.leftNode != null) {
                lst.addAll(this.leftNode.depthSearchKey());
            }
            lst.add(this.key);
            if (this.rightNode != null) {
                lst.addAll(this.rightNode.depthSearchKey());
            }
            return lst;
        }

    }
    private btNode root;
    private int size;

    public BSTMap(){
        this.root = null;
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public V get(K key) {
        if (root == null) {
            return null;
        }
        btNode foundNode = root.findNode(key);
        if (foundNode == null) return null;
        return foundNode.val;
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        size += 1;
        btNode toAddNode = new btNode(key, value);
        if (root == null) {
            this.root = toAddNode;
            return;
        }
        btNode parentNode = root.findParentNode(key);
        if (parentNode.key.compareTo(key) > 0) {
            parentNode.addToLeft(toAddNode);
        } else {
            parentNode.addToRight(toAddNode);
        }

    }

    @Override
    public boolean containsKey(K key) {
        if (this.root == null){
            return false;
        }
        btNode foundNode = this.root.findNode(key);
        return foundNode != null;
    }
}
