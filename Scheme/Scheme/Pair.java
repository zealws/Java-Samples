// Scheme language interpreter
// SExpression Representations
// Partially modified from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

// Pair class
// Represents a cons cells
public class Pair extends SExpression {

    ////
    //// Data Members
    ////

    // Either part of the pair
    private SExpression myCar;
    private SExpression myCdr;

    ////
    //// Constructor
    ////

    public Pair(SExpression car, SExpression cdr) {
        myCar = car;
        myCdr = cdr;
    }

    ////
    //// Methods
    ////

    // Converts an PairNode to a string
    public String toString() {
        return "(" + myCar.toString() + " . " + myCdr.toString() + ")";
    }

    // Converts an SExpressionNode to a string with type information
    public String toTypedString() {
        return "Pair"+toString();
    }

    // Is the SExpression a pair?
    public boolean isPair() {
        return true;
    }

    // Returns the head
    public SExpression car(){
        return myCar;
    }

    // Returns the tail
    public SExpression cdr() {
        return myCdr;
    }

}