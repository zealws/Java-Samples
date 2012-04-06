// Examples of various types of CPU caching methods
// For CS320 - Computer Architecture
//
// By Zeal Jagannatha

// Import the Cache library.
import Cache.*;

// Import Vector and LinkedList
import java.util.*;

class Main {

    public static void main(String[] args) {

        Vector<Integer> l = new Vector<Integer>(20);
        l.setSize(20);
        for(int i = 0; i < 20; i++) {
            l.set(i,new Integer(20-i));
        }

        System.out.println("Initial List:");
        for(int i = 0; i < l.size(); i++) {
            System.out.print(l.get(i));
            System.out.print(" ");
        }
        System.out.println();
        System.out.println();

        DirectMappingCache<Integer,Vector<Integer>> c = new DirectMappingCache<Integer,Vector<Integer>>(10,l);

        System.out.println("Altering values:");
        System.out.println("l[0] := 105");
        c.set(0,105);
        System.out.println("l[1] := 357");
        c.set(1,357);
        System.out.println("l[2] := 951");
        c.set(2,951);
        System.out.println("l[3] := 159");
        c.set(3,159);
        System.out.println("l[4] := 753");
        c.set(4,753);
        System.out.println("l[5] := 369");
        c.set(5,369);
        System.out.println("l[6] := 258");
        c.set(6,258);
        System.out.println("l[7] := 147");
        c.set(7,147);
        System.out.println("l[8] := 789");
        c.set(8,789);
        System.out.println("l[9] := 456");
        c.set(9,456);
        System.out.println("Result:");
        c.printCache();
        System.out.println();

        System.out.println("Altering values:");
        System.out.println("l[10] := 123");
        c.set(10,123);
        System.out.println("Result:");
        c.printCache();
        System.out.println();

        System.out.println("Resulting List");
        for(int i = 0; i < l.size(); i++) {
            System.out.print(l.get(i));
            System.out.print(" ");
        }
        System.out.println();
        System.out.println();

        System.out.println("Writing back all results.");
        c.writebackAll();
        System.out.println("Resulting List");
        for(int i = 0; i < l.size(); i++) {
            System.out.print(l.get(i));
            System.out.print(" ");
        }
        System.out.println();
        System.out.println();
    }

}
