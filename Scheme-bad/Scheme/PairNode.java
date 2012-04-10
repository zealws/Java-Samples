// Scheme language interpreter
// SExpression Representations
// Shamelessly stolen/adopted from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

// PairNode class
// Provides the body for a cons cell expression
class PairNode extends SExpressionNode {

    ////
    //// Data Members
    ////

    // Either part of the pair
    private SExpression myCar;
    private SExpression myCdr;

    ////
    //// Constructor
    ////

    public PairNode(SExpression car, SExpression cdr) {
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