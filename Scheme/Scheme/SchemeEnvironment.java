// Scheme language interpreter
// SExpression Representations
// Partially modified from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

// For vector
import java.util.*;

// Scheme Environment
// Implements a lookup table for scheme environments.
class SchemeEnvironment {

    ////
    //// Class Definitions
    ////

    public class SchemeBinding {
        public SExpression symbol;
        public SExpression value;
        public SchemeBinding(SExpression s, SExpression v) {
            symbol = s;
            value = v;
        }
        public SchemeBinding(String s1, String s2) {
            symbol = new Symbol(s1);
            value = new Symbol(s2);
        }
    }

    ////
    //// Data Members
    ////

    private Vector<SchemeBinding> bindings = new Vector<SchemeBinding>();

    ////
    //// Methods
    ////

    public static SchemeEnvironment DefaultEnvironment() {
        SchemeBinding[] arr =
            {new SchemeBinding("car","*car*"),
             new SchemeBinding("cdr","*cdr*"),
             new SchemeBinding("cons","*cons*"),
             new SchemeBinding("eq?","*eq?*"),
             new SchemeBinding("symbol?","*symbol?*"),
             new SchemeBinding("apply","*apply*")};
        return MakeSchemeEnvironment(arr);
    }

    public static SchemeEnvironment MakeSchemeEnvironment(SchemeBinding[] initBinds) {
        SchemeEnvironment e = new SchemeEnvironment();
        for(int i = 0; i < initBinds.length(); i++) {
            e.AddBinding(initBinds[i]);
        }
        return e;
    }

    public void AddBinding(SchemeBinding bind) {
        bindings.add(bind);
    }

    // Returns the value for a symbol
    // If no symbol is found, returns null
    public SExpression LookupValue(SExpression symbol) {
        SchemeBinding b = LookupBinding(symbol);
        if(b == null)
            return null;
        else
            return b.value;
    }

    // Returns the binding for a symbol
    // If a symbol isn't found, returns null
    public SchemeBinding LookupBinding(SExpression symbol) {
        for(int i = 0; i < bindings.size(); i++) {
            if(bindings.get(i).symbol.isEqual(symbol))
                return bindings.get(i);
        }
        return null;
    }

    public SExpression[] toArray() {
        SExpression[] arr = new SExpression[bindings.size()];
        for(int i = 0; i < bindings.size(); i++) {
            arr[i] = new Pair(bindings.get(i).symbol,
                              bindings.get(i).value);
        }
        return arr;
    }

}