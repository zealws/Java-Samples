// I'm using the notation from my Database notes, so it might be kinda confusing.

// For Vector and LinkedList
import java.util.*;

// This particular class implements a Dynamic Hash table via Incremental Rehashing
// Linear probing is used to resolve collisions.
class DynamicHashTable<valueType> {

    ////
    //// Data Members
    ////

    // The primary table
    private Vector<Pair<Integer,valueType>> primary = null;

    // The secondary table
    private Vector<Pair<Integer,valueType>> secondary = null;

    // The highwater mark
    private int highwaterMark = 0;

    // The load
    private int load = 0;

    // The load boundary
    private final int loadConstant;

    ////
    //// Constructors
    ////

    // Creates a DynamicHashTable with a given load constant and size
    // If a load constant of 1 is given, the result is the equivalent of static hashing.
    // I.E. an exception is thrown as soon as a value is inserted into a full table.
    public DynamicHashTable(int initLoadConstant, int initSize) {
        loadConstant = initLoadConstant;
        primary = new Vector<Pair<Integer,valueType>>(initSize);
        primary.setSize(initSize);
        secondary = new Vector<Pair<Integer,valueType>>(initSize*initLoadConstant);
        secondary.setSize(initSize*initLoadConstant);
    }

    ////
    //// Methods
    ////

    // Get a particular key's value in the hash
    public valueType get(Integer key) {
        Integer target = findKey(key,primary);
        if(target == null) {
            return null;
        }
        return primary.get(findKey(key,primary)).second;
    }

    // Add a value to the hash
    public void set(Integer key, valueType newValue) {
        Integer bucket = findKeyOrEmpty(key,primary);
        if(primary.get(bucket) == null) {
            load += 1;
        }

        // Are we still below the load capacity
        if(load < primary.size()/new Double(loadConstant)) {
            primary.set(bucket,new Pair<Integer,valueType>(key,newValue));
        }

        else {
            // Start using the secondary table
            primary.set(bucket,new Pair<Integer,valueType>(key,newValue));

            if(bucket < highwaterMark) {
                secondary.set(findKeyOrEmpty(key,secondary),new Pair<Integer,valueType>(key,newValue));
            }

            for(int i = 0; i < loadConstant; i++) {
                if(highwaterMark < primary.size() && primary.get(highwaterMark) != null) {
                    Pair<Integer,valueType> rehash = primary.get(highwaterMark);

                    secondary.set(findKeyOrEmpty(rehash.first,secondary),rehash);
                    highwaterMark += 1;
                }
            }
            if(highwaterMark+loadConstant >= primary.size()) {
                // This means we're done with the primary table
                // So we can discard it and make the secondary our new primary
                System.out.println("Switching tables...");
                System.out.print("Load: ");
                System.out.print(loadPercent());
                System.out.print(" => ");
                load = 2*primary.size()/loadConstant;
                primary = secondary;
                secondary = new Vector<Pair<Integer,valueType>>(primary.size()*2);
                secondary.setSize(primary.size()*2);
                highwaterMark = 0;
                System.out.println(loadPercent());
            }
        }

    }

    // Maps a key into the primary table
    private Integer map(Integer key, Vector<Pair<Integer,valueType>> table) {
        return key%table.size();
    }

    // Gets a bucket number from a table that matches key
    private Integer findKey(Integer key, Vector<Pair<Integer,valueType>> table) {
        Integer bucket = map(key,table);
        while(primary.get(bucket) != null && primary.get(bucket).first != key) {
            bucket = bucket+1;
            bucket = bucket%table.size();
            if(key == bucket) {
                // Not found
                return null;
            }
        }
        if(primary.get(bucket) == null) {
            // Also not found
            return null;
        }
        // Found!
        return bucket;
    }

    // Gets a bucket number from a table
    private Integer findKeyOrEmpty(Integer key, Vector<Pair<Integer,valueType>> table) {
        Integer bucket = map(key,table);
        while(table.get(bucket) != null && table.get(bucket).first != key) {
            System.out.println("blah");
            bucket = bucket+1;
            bucket = bucket%table.size();
            if(key == bucket) {
                // Not found
                throw new IndexOutOfBoundsException();
            }
        }
        return bucket;
    }

    // Returns the load, as a percentage
    public double loadPercent() {
        return new Double(load) / primary.size();
    }

    // Prints to System.out the contents of the primary table
    public void print() {
        for(int i = 0; i < primary.size(); i++) {
            Pair<Integer,valueType> p = primary.get(i);
            System.out.print(i);
            System.out.print(": ");
            if(p != null) {
                System.out.print(p.first);
                System.out.print(" => ");
                System.out.println(p.second);
            }
            else
                System.out.println("null");
        }
    }

}