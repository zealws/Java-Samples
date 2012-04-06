package Cache;

// The Cache proper.
import Cache.*;

// For Vector and LinkedList
import java.util.*;

interface Cache<valueType, listType extends List> {

	////
	//// Data Members
	////

	// The uncached list
	protected listType uncachedList;

	////
	//// Methods
	////

	// Get a particular value
	public valueType get(int key);

	// Set a particular value
	public void set(int key, valueType newValue);

	// Is a key in the cache?
	protected boolean inCache(int key);

	// Cache a value that's not in the cache
	protected void cacheValue(int key);

	// Get specifically from the cache
	protected valueType getFromCache(int key);

	// Set a value specifically in the cache
	protected void setToCache(int key, valueType newValue);

	// Prints the cache's contents
	public void printCache();

}
