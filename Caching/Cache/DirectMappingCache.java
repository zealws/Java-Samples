// Examples of various types of CPU caching methods
// For CS320 - Computer Architecture
//
// By Zeal Jagannatha

package Cache;

// The Cache proper.
import Cache.*;

// For Vector and LinkedList
import java.util.*;

public class DirectMappingCache<valueType, listType extends List<valueType>> extends Cache<valueType,listType> {

    ////
    //// Data Members
    ////

    private Vector<Pair<Integer,valueType>> cacheTable = null;

    ////
    //// Constructors
    ////

    // Creates a Direct mapped cache with a particular cache size on a particular list
    public DirectMappingCache(int cacheSize, listType initialList) {
        // Create the cacheTable
        cacheTable = new Vector<Pair<Integer,valueType>>(cacheSize);
        cacheTable.setSize(cacheSize);

        // Setup the uncached list
        uncachedList = initialList;

        // Start by reading into the cache the first several values so the cache is full.
        for(int i = 0; i < cacheTable.size(); i++) {
            placeInCache(i);
        }
    }

    ////
    //// Methods
    ////

    // Get a particular value
    //public valueType get(Integer key);
    // Inherited from Cache

    // Set a particular value
    //public valueType set(Integer key, valueType newValue);
    // Inherited from Cache

    // Is a key in the cache?
    //protected boolean inCache(Integer key);
    // Inherited from Cache

    // Cache a value that's not in the cache
    protected void placeInCache(Integer key) {
        // Writeback the thing we're kicking out
        writeback(key);
        // Get the new value from the list
        setToCache(key, uncachedList.get(key));
    }

    // Get specifically from the cache
    protected Pair<Integer,valueType> getFromCache(Integer key) {
        return cacheTable.get(mapToCache(key));
    }

    // Set a value specifically in the cache
    protected void setToCache(Integer key, valueType newValue) {
        cacheTable.set(mapToCache(key), new Pair<Integer,valueType>(key,newValue));
    }

    // Prints the cache's contents
    public void printCache() {
        for(Integer i = 0; i < cacheTable.size(); i = i+1) {
            Pair<Integer,valueType> curr = cacheTable.get(i);
            System.out.print(curr.first);
            System.out.print(":");
            System.out.print(curr.second);
            System.out.print(" ");
        }
        System.out.println();
    }

    protected void writeback(Integer key) {
        if(getFromCache(key) != null) {
            // Writeback the cached value into the list
            uncachedList.set(getFromCache(key).first,getFromCache(key).second);
        }
    }

    public void writebackAll() {
        for(int i = 0; i < cacheTable.size(); i++) {
            writeback(i);
        }
    }

    // Maps a key into a cacke location
    protected Integer mapToCache(Integer key) {
        return key%cacheTable.size();
    }

}
