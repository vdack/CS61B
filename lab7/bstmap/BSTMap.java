package bstmap;

import edu.princeton.cs.algs4.StdRandom;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{
    //here are methods do not need to implement.
    @Override
    public V remove(K key) {
//        throw new UnsupportedOperationException();
        if (this.size == 0) {
            return null;
        }
        if (this.size == 1) {
            V removedValue = this.root.val;
            this.root = null;
            this.size = 0;
            return removedValue;
        }
        btNode parentNode = root.findParentNode(key);
        btNode foundNode;
        V removedValue;
        if (parentNode == null) {//remove root
            foundNode = root;
            removedValue = foundNode.val;
            root.switchKeyVal(foundNode.moveRandomLeaf());
            root.modifyNode();

        } else if (parentNode.leftNode != null && parentNode.leftNode.key.equals(key)) {//is left node
            foundNode = parentNode.leftNode;
            removedValue = foundNode.val;
            if (foundNode.isLeaf()) {
                parentNode.leftNode = null;
            } else {
                parentNode.leftNode.switchKeyVal(foundNode.moveRandomLeaf());
                parentNode.leftNode.modifyNode();
            }

        } else if (parentNode.rightNode != null && parentNode.rightNode.key.equals(key)) {//is right node
            foundNode = parentNode.rightNode;
            removedValue = foundNode.val;
            if (foundNode.isLeaf()) {
                parentNode.rightNode = null;
            } else {
                parentNode.rightNode.switchKeyVal(foundNode.moveRandomLeaf());
                parentNode.rightNode.modifyNode();
            }
        } else {
            return null;
        }
        size -= 1;
        return removedValue;
    }

    @Override
    public V remove(K key, V value) {
        if (this.size == 0) {
            return null;
        }
        if (this.size == 1) {
            V removedValue = this.root.val;
            this.root = null;
            this.size = 0;
            return removedValue;
        }
        if (value.equals(this.get(key))) {
            return remove(key);
        }
        return null;
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

        private boolean isLeaf(){
            return this.leftNode == null && this.rightNode == null;
        }
        public btNode moveRandomLeaf(){
            if (this.isLeaf()) {
                if (this.parentNode.leftNode != null && this.parentNode.leftNode.key.equals(this.key)) {
                    this.parentNode.leftNode = null;
                } else {
                    this.parentNode.rightNode = null;
                }
                return this;
            }
            if (this.leftNode == null) {
                return this.rightNode.moveRandomLeaf();
            }
            if (this.rightNode == null) {
                return this.leftNode.moveRandomLeaf();
            }
            int r = StdRandom.uniform(2);
            if (r == 0) {
                return this.leftNode.moveRandomLeaf();
            } else {
                return this.rightNode.moveRandomLeaf();
            }
        }

        public void switchKeyVal(btNode anotherNode) {
            K k = anotherNode.key;
            V v = anotherNode.val;
            anotherNode.key = this.key;
            anotherNode.val = this.val;
            this.key = k;
            this.val = v;
        }
        public void modifyNode(){
            if (leftNode != null && key.compareTo(leftNode.key) <= 0) {
                this.switchKeyVal(leftNode);
                leftNode.modifyNode();
            } else if (rightNode != null && key.compareTo(rightNode.key) >= 0) {
                this.switchKeyVal(rightNode);
                rightNode.modifyNode();
            }
        }

        public String toStringInOrder(){
            String lstr = "LEAF";
            String rstr = "LEAF";
            if (leftNode != null) {
                lstr = leftNode.toStringInOrder();
            }
            if (rightNode != null) {
                rstr = rightNode.toStringInOrder();
            }
            return "Node: (key: " + key.toString() + ") (value: " + val.toString() + ") (left: " + lstr + ") (right: " + rstr + ")";
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

    public void printInOrder(){
        if (this.root == null) {
            System.out.println("");
        }
        System.out.println(this.root.toStringInOrder());
    }
}
