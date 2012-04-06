// Examples of various types of CPU caching methods
// For CS320 - Computer Architecture
//
// By Zeal Jagannatha

package Cache;

// The Cache proper.
import Cache.*;

// For Vector and LinkedList
import java.util.*;

abstract class Cache<valueType, listType extends List<valueType>> {

    ////
    //// Class Members
    ////

    // Simple Pair class
    protected class Pair<firstType,secondType> {
        //Data Members
        public firstType first;
        public secondType second;

        // Constructors
        public Pair() {
            // Do Nothing
        }
        public Pair(firstType f, secondType s) {
            first = f;
            second = s;
        }
    }

    ////
    //// Data Members
    ////

    // The uncached list
    protected listType uncachedList;

    ////
    //// Methods
    ////

    // Get a particular value
    public valueType get(Integer key) {
        if(!inCache(key))
            placeInCache(key);
        return getFromCache(key).second;
    }

    // Set a particular value
    public void set(Integer key, valueType newValue) {
        if(!inCache(key))
            placeInCache(key);
        setToCache(key,newValue);
    }

    // Is a key in the cache?
    protected boolean inCache(Integer key) {
        return getFromCache(key).first == key;
    }

    // Cache a value that's not in the cache
    protected abstract void placeInCache(Integer key);

    // Get specifically from the cache
    protected abstract Pair<Integer,valueType> getFromCache(Integer key);

    // Set a value specifically in the cache
    protected abstract void setToCache(Integer key, valueType newValue);

    // Prints the cache's contents
    public abstract void printCache();

}
