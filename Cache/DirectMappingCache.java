package Cache;

// The Cache proper.
import Cache.*;

// For Vector and LinkedList
import java.util.*;

class DirectMappingCache<valueType, listType extends List> implements Cache<valueType,listType> {

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

	private Vector<Pair<int,valueType>> cacheTable = null;

	////
	//// Constructors
	////

	// Creates a Direct mapped cache with a particular cache size on a particular list
	public DirectMappingCache(int cacheSize, listType initialList) {
		cacheTable = new Vector<Pair<int,valueType>>(cacheSize,cacheSize);
		uncachedList = initialList;
	}

	////
	//// Methods
	////

	// Get a particular value
	public valueType get(int key) {
		if(inCache(key))
			return getFromCache(key);
		else {
			cacheValue(key);
			getFromCache(key).first;
		}
	}

	// Set a particular value
	public valueType set(int key, valueType newValue) {
		if(inCache(key))
			setToCache(key,newValue);
		else {
			cacheValue(key);
			setToCache(key,newValue);
		}
	}

	// Is a key in the cache?
	protected boolean inCache(int key) {
		return getFromCache(key).first == key;
	}

	// Cache a value that's not in the cache
	protected void cacheValue(int key) {
		// Writeback the cached value into the list
		uncachedList.set(getFromCache(key).first,getFromCache(key).second);

		// Get the new value from the list
		setToCache(key, uncachedList.get(key));
	}

	// Get specifically from the cache
	protected valueType getFromCache(int key) {
		return cacheTable.get(mapToCache(key));
	}

	// Set a value specifically in the cache
	protected void setToCache(int key, valueType newValue) {
		cacheTable.set(mapToCache(key), new Pair<int,valueType>(key,newValue));
	}

	// Prints the cache's contents
	public void printCache() {
		for(int i = 0; i < cacheTable.size(); i++) {
			System.out.print(cacheTable.get(i).first);
			System.out.print(" : ");
			System.out.println(cacheTable.get(i).second);
		}
	}

	// Maps a key into a cacke location
	protected boolean mapToCache(int key) {
		return key%cacheTable.size();
	}

}
