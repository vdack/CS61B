package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int capacity;
    private int size;
    private double loadFactor;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        size = 0;
        capacity = 128;
        loadFactor = Double.POSITIVE_INFINITY;
        buckets = createTable(capacity);
    }

    public MyHashMap(int initialSize) {
        size = 0;
        capacity = initialSize;
        loadFactor = Double.POSITIVE_INFINITY;
        buckets = createTable(capacity);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        size = 0;
        capacity = initialSize;
        loadFactor = maxLoad;
        buckets = createTable(capacity);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new HashSet<Node>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!


    @Override
    public int size() {
        return size;
    }


    private Node findKeyValueInBucket(K key, Collection<Node> bucket){
        for (Node node: bucket) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }
    private Collection<Node> getBucket(K key) {
        int hashCode = (key.hashCode() % capacity + capacity) % capacity;
        Collection<Node> bucket = buckets[hashCode];
        return bucket;
    }
    @Override
    public boolean containsKey(K key) {
        Collection<Node> bucket = getBucket(key);
        if (bucket == null) {
            return false;
        }
        Node kvNode = findKeyValueInBucket(key, bucket);
        if (kvNode == null) {
            return false;
        }
        return true;
    }

    @Override
    public V get(K key) {
        Collection<Node> bucket = getBucket(key);
        if (bucket == null) {
            return null;
        }
        Node kvNode = findKeyValueInBucket(key, bucket);
        if (kvNode == null) {
            return null;
        }
        return kvNode.value;
    }

    private void resize(){
        capacity *= 2;
        Collection<Node>[] largerTable = createTable(capacity);
        for (Collection<Node> bucket: buckets){
            if (bucket == null) {
                continue;
            }
            for (Node node: bucket){
                int hashCode = node.key.hashCode() % capacity;
                if (largerTable[hashCode] == null) {
                    largerTable[hashCode] = createBucket();
                }
                largerTable[hashCode].add(node);
            }
        }
        this.buckets = largerTable;
    }
    @Override
    public void put(K key, V value) {
        int hashCode = (key.hashCode() % capacity + capacity) % capacity;
        Collection<Node> bucket = buckets[hashCode];
        if (bucket == null) {
            buckets[hashCode] = createBucket();
            bucket = buckets[hashCode];
        }
        Node addedNode = createNode(key, value);
        if (findKeyValueInBucket(key, bucket) != null) {
            Collection<Node> updatedBucket = createBucket();
            for (Node node : bucket) {
                if (node.key.equals(key)) {
                    updatedBucket.add(addedNode);
                } else {
                    updatedBucket.add(node);
                }
                buckets[hashCode] = updatedBucket;
            }
            return;
        }
        bucket.add(createNode(key, value));
        size += 1;
        if (((double) size / (double)capacity) > loadFactor) {
            resize();
        }
    }

    @Override
    public void clear() {
        this.buckets = createTable(capacity);
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for (Collection<Node> bucket : buckets) {
            if (bucket == null) {
                continue;
            }
            for (Node node : bucket) {
                set.add(node.key);
            }
        }
        return set;
    }

    private class MHMIterator implements Iterator<K> {
        int bucketsPosi;
        int innerPosi;
        ArrayList<K> currentKeys;
        public MHMIterator(){
            bucketsPosi = 0;
            innerPosi = 0;
            currentKeys = null;
            while (bucketsPosi < buckets.length) {
                if (buckets[bucketsPosi] != null && buckets[bucketsPosi].size() > 0) {
                    currentKeys = new ArrayList<>();
                    for (Node node : buckets[bucketsPosi]) {
                        currentKeys.add(node.key);
                    }
                }
                bucketsPosi += 1;
            }
        }

        @Override
        public boolean hasNext() {
            return currentKeys != null;
        }

        @Override
        public K next() {
            if (currentKeys == null) {
                return null;
            }
            K key = currentKeys.get(innerPosi);
            innerPosi += 1;
            if (innerPosi == currentKeys.size()){
                innerPosi = 0;
                currentKeys = null;
            }
            bucketsPosi += 1;
            while (bucketsPosi < buckets.length) {
                if (buckets[bucketsPosi] != null && buckets[bucketsPosi].size() > 0) {
                    currentKeys = new ArrayList<>();
                    for (Node node : buckets[bucketsPosi]) {
                        currentKeys.add(node.key);
                    }
                }
                bucketsPosi += 1;
            }
            return key;
        }
    }
    @Override
    public Iterator<K> iterator() {
        return new MHMIterator();
    }

    @Override
    public V remove(K key) {
        int hashCode = (key.hashCode() % capacity + capacity) % capacity;
        Collection<Node> bucket = buckets[hashCode];
        if (bucket == null) {
            return null;
        }
        V value = null;
        Collection<Node> updateBucket = createBucket();
        for (Node node : bucket) {
            if (node.key == key) {
                value = node.value;
                continue;
            }
            updateBucket.add(node);
        }
        if (updateBucket.size() == 0){
            buckets[hashCode] = null;
        } else {
            buckets[hashCode] = updateBucket;
        }
        return value;
    }

    @Override
    public V remove(K key, V value) {
        Collection<Node> bucket = getBucket(key);
        Node node = findKeyValueInBucket(key, bucket);
        if (node.value == value) {
            return remove(key);
        }
        return null;
    }
}
