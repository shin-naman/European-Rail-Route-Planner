import org.junit.jupiter.api.Test;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import java.util.NoSuchElementException;

/**
 * This class extends the HashTableMap class to run submission checks on it.
 */
public class P212SubmissionChecker extends HashTableMap<Integer, Integer> {

  /**
   * Checks if hashtable resizes as expected.
   */
  @Test
  public void testTableResize() {
    HashTableMap<String, String> map = new HashTableMap<>(8);
    map.put("a", "1");
    map.put("b", "2");
    map.put("c", "3");
    map.put("d", "4");
    map.put("e", "5");

    // check capacity before resizing
    Assertions.assertEquals(8, map.getCapacity());   
 
    map.put("f", "6");

    // check capacity after resizing
    Assertions.assertEquals(16, map.getCapacity());
  }

  /**
   * Tests putting duplicate "a" keys into the HashTableMap and checks for IllegalArgumentExceptions
   * after the first insertion. Then checkes if calling get("a") returns the first value inserted.
   */
  @Test
  public void testDuplicateInsertions() {
    HashTableMap<String, Integer> map = new HashTableMap<>();
    map.put("a", 6);
    Assertions.assertThrows(
           IllegalArgumentException.class,
           () -> map.put("a", 7)
    );
    Assertions.assertThrows(
           IllegalArgumentException.class,
           () -> map.put("a", 8)
    );
    Assertions.assertEquals(6, map.get("a"));
  }
 
  /**
   * Tests the get method on a small HashTableMap with keys 1, 2, and 3.
   */
  @Test
  public void testGetOnSmallMap123() {
    HashTableMap<Integer, String> map = new HashTableMap<>();
    map.put(1, "one");
    map.put(2, "two");
    map.put(3, "three");
    Assertions.assertEquals("two", map.get(2));
  }

  /**
   * Tests that put() correctly stores key-value pairs, containsKey() detects them,
   * get() returns the correct value, and getSize() tracks the number of pairs.
   */
  @Test
  public void testPutGetContainsAndSize() {
    // create a new hash table
    HashTableMap<Integer, String> map = new HashTableMap<>();

    // add key-value pairs
    map.put(1, "one");
    map.put(2, "two");

    // check that the keys are stored
    Assertions.assertTrue(map.containsKey(1));
    Assertions.assertTrue(map.containsKey(2));

    // check that get returns the correct values
    Assertions.assertEquals("one", map.get(1));
    Assertions.assertEquals("two", map.get(2));

    // check that size updated correctly
    Assertions.assertEquals(2, map.getSize());
  }

  /**
   * Tests that inserting a duplicate key throws an IllegalArgumentException
   * and does not replace the original value.
   */
  @Test
  public void testDuplicateKeyInsertion() {
    // create a new hash table
    HashTableMap<Integer, String> map = new HashTableMap<>();

    // insert one key-value pair
    map.put(10, "ten");

    // confirm duplicate insertion throws an exception
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      map.put(10, "twenty");
    });

    // confirm original value is still stored
    Assertions.assertEquals("ten", map.get(10));
  }

  /**
   * Tests that remove() deletes a stored key-value pair, returns its value,
   * and updates the size correctly.
   */
  @Test
  public void testRemove() {
    // create a new hash table
    HashTableMap<Integer, String> map = new HashTableMap<>();

    // add some pairs
    map.put(1, "one");
    map.put(2, "two");

    // remove one key and check returned value
    Assertions.assertEquals("one", map.remove(1));

    // check that the key is gone
    Assertions.assertFalse(map.containsKey(1));

    // check that size decreased
    Assertions.assertEquals(1, map.getSize());

    // check that removing a missing key throws an exception
    Assertions.assertThrows(NoSuchElementException.class, () -> {
      map.remove(1);
    });
  }

  /**
   * Tests that clear() removes all key-value pairs without changing capacity,
   * and that getKeys() returns the stored keys before clearing.
   */
  @Test
  public void testClearGetKeysAndCapacity() {
    // create a new hash table
    HashTableMap<Integer, String> map = new HashTableMap<>(8);

    // add key-value pairs
    map.put(1, "one");
    map.put(2, "two");
    map.put(3, "three");

    // get the list of keys
    List<Integer> keys = map.getKeys();

    // check that all inserted keys are present
    Assertions.assertTrue(keys.contains(1));
    Assertions.assertTrue(keys.contains(2));
    Assertions.assertTrue(keys.contains(3));

    // clear the table
    map.clear();

    // check that size becomes 0
    Assertions.assertEquals(0, map.getSize());

    // check that capacity stays the same
    Assertions.assertEquals(8, map.getCapacity());

    // check that keys list is now empty
    Assertions.assertEquals(0, map.getKeys().size());
  }

  /**
   * Tests that the hash table resizes when the load factor reaches 75%,
   * and that all key-value pairs are still accessible after rehashing.
   */
  @Test
  public void testResizeAndRehashing() {
    // create small table with capacity 4
    HashTableMap<Integer, String> map = new HashTableMap<>(4);

    // add entries up to just before resize threshold
    map.put(1, "one");
    map.put(2, "two");

    // check starting capacity before triggering resize
    Assertions.assertEquals(4, map.getCapacity());

    // add 3rd entry that should trigger resize
    map.put(3, "three");

    // confirm capacity doubled
    Assertions.assertEquals(8, map.getCapacity());

    // confirm all values are still accessible after resizing
    Assertions.assertEquals("one", map.get(1));
    Assertions.assertEquals("two", map.get(2));
    Assertions.assertEquals("three", map.get(3));

    // confirm size is still correct
    Assertions.assertEquals(3, map.getSize());
  }
}
