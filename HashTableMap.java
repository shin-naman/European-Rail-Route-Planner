import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

    protected class Pair {

        public KeyType key;
        public ValueType value;

        public Pair(KeyType key, ValueType value) {
            this.key = key;
            this.value = value;
        }

    }

    protected LinkedList<Pair>[] table = null;
    private int size = 0;

    /**
     * Creates new hashtable with given capacity
     * @param capacity initial capacity of hashtable
     */
    @SuppressWarnings("unchecked")
    public HashTableMap(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be AT LEAST 1");
        }
        table = (LinkedList<Pair>[]) new LinkedList[capacity];
    }

    /**
     * Creates new hashtable with default capacity 8
     */
    public HashTableMap() {
        this(8);
    }

    /**
     * Adds a new key,value pair/mapping to this collection.
     * @param key the key of the key,value pair
     * @param value the value that key maps to (may be null)
     * @throws IllegalArgumentException if key already maps to a value without
     *         making any changes to the table
     * @throws NullPointerException if key is null
     */
    @Override
    public void put(KeyType key, ValueType value) throws IllegalArgumentException {
        if (key == null) {
            throw new NullPointerException("Key cannot be null!");
        }
        if (containsKey(key)) {
            throw new IllegalArgumentException("Key is a duplicate!");
        }
        if ((size + 1.0) / table.length >= 0.75) {
            resize();
        }

        int index = getIndex(key);

        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }

        table[index].add(new Pair(key, value));
        size++;
    }

    /**
     * Checks whether a key maps to a value in this collection.
     * @param key the key to check
     * @throws NullPointerException if key is null
     * @return true if the key maps to a value, and false is the key doesn't
     *         map to a value
     */
    @Override
    public boolean containsKey(KeyType key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null!");
        }

        int index = getIndex(key);

        if (table[index] == null) {
            return false;
        }

        for (Pair pair: table[index]) {
            if (pair.key.equals(key)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Retrieves the specific value that a key maps to.
     * @param key the key to look up
     * @return the value that key maps to
     * @throws NoSuchElementException when key is not stored in this collection
     * @throws NullPointerException if key is null
     */
    @Override
    public ValueType get(KeyType key) throws NoSuchElementException {
        if (key == null) {
            throw new NullPointerException("Key cannot be null!");
        }

        int index = getIndex(key);

        if (table[index] != null) {
            for (Pair pair: table[index]) {
                if (pair.key.equals(key)) {
                    return pair.value;
                }
            }
        }

        throw new NoSuchElementException("Key not found");
    }

    /**
     * Remove the mapping for a key from this collection.
     * @param key the key whose mapping to remove
     * @return the value that the removed key mapped to
     * @throws NoSuchElementException when key is not stored in this collection
     * @throws NullPointerException if key is null
     */
    @Override
    public ValueType remove(KeyType key) throws NoSuchElementException {
        if (key == null) {
            throw new NullPointerException("Key cannot be null!");
        }

        int index = getIndex(key);

        if (table[index] != null) {
            for (int i = 0; i < table[index].size(); i++) {
                Pair pair = table[index].get(i);
                if (pair.key.equals(key)) {
                    ValueType removed = pair.value;
                    table[index].remove(i);
                    size--;
                    return removed;
                }
            }
        }

        throw new NoSuchElementException("Key not found!");
    }

    /**
     * Removes all key,value pairs from this collection without changing the
     * capacity of the underlying array.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void clear() {
        table = (LinkedList<Pair>[]) new LinkedList[table.length];
        size = 0;
    }

    /**
     * Retrieves the number of keys stored in this collection.
     * @return the number of keys stored in this collection
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Retrieves this collection's capacity.
     * @return the size of the underlying array for this collection
     */
    @Override
    public int getCapacity() {
        return table.length;
    }

    /**
     * Retrieves this collection's keys.
     * @return a list of keys in the underlying array for this collection
     */
    @Override
    public List<KeyType> getKeys() {
        List<KeyType> keys = new LinkedList<>();

        for (LinkedList<Pair> box : table) {
            if (box != null) {
                for (Pair pair : box) {
                    keys.add(pair.key);
                }
            }
        }

        return keys;
    }

    /**
     * Private helper method that returns the index based off the key
     * @param key is the key we want to return the index of
     */
    private int getIndex(KeyType key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    /**
     * Private helper method that resizes the table when load factor
     * is greater than or equal to 0.75
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        LinkedList<Pair>[] oldTable = table;
        table = (LinkedList<Pair>[]) new LinkedList[oldTable.length * 2];
        size = 0;

        for (LinkedList<Pair> box : oldTable) {
            if (box != null) {
                for (Pair pair : box) {
                    put(pair.key, pair.value);
                }
            }
        }
    }
}
