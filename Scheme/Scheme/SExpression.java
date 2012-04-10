// Scheme language interpreter
// SExpression Representations
// Partially modified from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

// SExpression class
// Abstract base class for all S-Expressions
public abstract class SExpression {

    ////
    //// Sub-class definition
    ////

    private static class ExpressionPosPair {
        public ExpressionPosPair(int string, SExpression expression) {
            pos = string;
            exp = expression;
        }
        public ExpressionPosPair(SExpression expression, int string) {
            pos = string;
            exp = expression;
        }
        public int pos;
        public SExpression exp;
    }

    ////
    //// Methods
    ////

    // Converts an SExpressionNode to a string
    public abstract String toString();

    // Converts an SExpressionNode to a string with type information
    public abstract String toTypedString();

    // Is the SExpression the empty list?
    public boolean isNull() {
        return false;
    }

    // Is the SExpression a symbol?
    public boolean isSymbol() {
        return false;
    }

    // Is the SExpression a boolean?
    public boolean isBoolean() {
        return false;
    }

    // Is the SExpression a pair?
    public boolean isPair() {
        return false;
    }

    // Returns the head of a list
    public SExpression car() {
        System.err.print("Warning: Taking car of non-pair thing: ");
        System.err.println(toTypedString());
        StackTraceElement[] stackTraceElements =
            Thread.currentThread().getStackTrace();
        for (int i=2 ; i<stackTraceElements.length; i++) {
            StackTraceElement ste = stackTraceElements[i];
            String classname = ste.getClassName();
            String methodName = ste.getMethodName();
            int lineNumber = ste.getLineNumber();
            System.out.println("\tat "+classname+"."+methodName+":"+lineNumber);
        }
        return null;
    }

    // Returns the tail of a list
    public SExpression cdr() {
        System.err.print("Warning: Taking cdr of non-pair thing: ");
        System.err.println(toTypedString());
        StackTraceElement[] stackTraceElements =
            Thread.currentThread().getStackTrace();
        for (int i=2 ; i<stackTraceElements.length; i++) {
            StackTraceElement ste = stackTraceElements[i];
            String classname = ste.getClassName();
            String methodName = ste.getMethodName();
            int lineNumber = ste.getLineNumber();
            System.out.println("\tat "+classname+"."+methodName+":"+lineNumber);
        }
        return null;
    }

    public boolean isEqual(SExpression other) {
        return toString().equals(other.toString());
    }

    ////
    //// Formatted output to a String
    ////

    // Nicely formats an SExpression
    public String formattedString() {
        return formattedString(this);
    }

    // Nicely formats an SExpression
    protected static String formattedString(SExpression e) {
        if(e.isPair())
            return "(" + formattedList(e) + ")";
        else
            return e.toString();
    }

    // Nicely formats a proper list
    protected static String formattedList(SExpression l) {
        if(l.cdr().isNull())
            return formattedString(l.car());
        else if(l.cdr().isPair())
            return formattedString(l.car()) + " " + formattedList(l.cdr());
        else
            return formattedString(l.car()) + " . " + formattedString(l.cdr());
    }

    ////
    //// Parsing from a String
    ////

    // Returns an SExpression parsed from a string
    // If more than one expression is present in s, only the first is returned.
    // If no expressions are present in s, null is returned.
    public static SExpression fromString(String s) {
        ExpressionPosPair p = nextSExpression(s,0);
        if(p == null)
            return null;
        else
            return p.exp;
    }

    // Parses exactly 1 SExpression and returns a pair of the resulting
    // SExpression and the index of the next usable character within s.
    // If s contains no SExpressions, returns null.
    // If all of s is an SExpression, s.length() is returned as the next usable index.
    private static ExpressionPosPair nextSExpression(String s, int startPlace) {
        int i;

        // Filter through, ignoring whitespace
        boolean stop = false;
        for(i = startPlace; !stop; i++) {
            if(i >= s.length() || i < 0) {
                // Should have gotten an SExpression where we got only whitespace
                System.err.print("Warning: nextSExpression expected SExpression but got whitespace: ");
                System.err.println(i);
                return null;
            }
            else if(s.charAt(i) == ' ' || s.charAt(i) == '\t' || s.charAt(i) == '\n') {
                // Do Nothing
            }
            else {
                stop = true;
                i--;
            }
        }

        /*
        System.out.println("Parsing: ");
        System.out.println(s);
        for(int j = 0; j < i; j++) {
            System.out.print(" ");
        }
        System.out.println("^");
        */

        // i is the position of the first non-whitespace character

        if(s.charAt(i) == '\'') {
            // Quoted expression
            ExpressionPosPair p = nextSExpression(s,i+1);
            return new ExpressionPosPair(new Pair(new Symbol("quote"), new Pair(p.exp, new Null())),
                                         p.pos);
        }
        else if(s.charAt(i) == '(') {
            // Read in a list
            return nextList(s,i+1);
        }
        else if(s.charAt(i) == '#') {
            // We found a boolean literal
            if(s.charAt(i+1) == 't') {
                return new ExpressionPosPair(new SBoolean(true),i+2);
            }
            else {
                return new ExpressionPosPair(new SBoolean(false),i+2);
            }
        }
        else {
            // Must be a symbol

            // Look for the next identifier-invalid character
            int nextSpace = i+1;
            // Filter through, ignoring whitespace
            stop = false;
            for(; !stop && nextSpace < s.length(); nextSpace++) {
                if (s.charAt(nextSpace) == ' ' ||
                    s.charAt(nextSpace) == '\t' ||
                    s.charAt(nextSpace) == '\n' ||
                    s.charAt(nextSpace) == '(' ||
                    s.charAt(nextSpace) == ')') {
                    stop = true;
                    nextSpace--;
                }
            }
            // Return the expression up to the next space index, with the next space as the next valid position.
            return new ExpressionPosPair(new Symbol(s.substring(i,nextSpace)),nextSpace);
        }
    }

    // Helper function to nextSExpression, return values are identical,
    // only reads in specifically proper lists.
    private static ExpressionPosPair nextList(String s, int startPlace) {
        // startPlace contains the position after the '('

        // Start by discarding whitespace again
        int i;
        boolean stop = false;
        for(i = startPlace; !stop; i++) {
            if(i >= s.length() || i < 0) {
                // Should have gotten an SExpression where we got only whitespace
                System.err.print("Warning: nextList expected SExpression but got whitespace: ");
                System.err.println(i);
                return null;
            }
            else if(s.charAt(i) == ' ' || s.charAt(i) == '\t' || s.charAt(i) == '\n') {
                // Do Nothing
            }
            else {
                stop = true;
                i--;
            }
        }

        if(s.charAt(i) == ')') {
            // Empty list
            return new ExpressionPosPair(new Null(),i+1);
        }
        else if(s.charAt(i) == '.'){
            // Strict Pair
            ExpressionPosPair p = nextSExpression(s,i+1);

            // Now we should be waiting for a ')', so discard all the whitespace.
            int j;
            stop = false;
            for(j = p.pos; !stop; j++) {
                if(j >= s.length() || j < 0) {
                    // Should have gotten an SExpression where we got only whitespace
                    System.err.print("Warning: nextList2 expected SExpression but got whitespace: ");
                    System.err.println(j);
                    return null;
                }
                else if(s.charAt(j) == ' ' || s.charAt(j) == '\t' || s.charAt(j) == '\n') {
                    // Do Nothing
                }
                else {
                    stop = true;
                    i--;
                }
            }

            // j holds the position of the next usable character after the SExpression

            return new ExpressionPosPair(p.exp,j);
        }
        else {
            // Proper List
            ExpressionPosPair head;
            ExpressionPosPair tail;

            // Start searching at i for the head
            head = nextSExpression(s,i);
            // Start at the next place for the tail
            tail = nextList(s,head.pos);

            return new ExpressionPosPair(new Pair(head.exp,tail.exp),tail.pos);
        }
    }

}