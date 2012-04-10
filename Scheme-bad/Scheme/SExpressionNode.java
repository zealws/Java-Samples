// Scheme language interpreter
// SExpression Representations
// Shamelessly stolen/adopted from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

// SExpression node class
// Provides the body for an SExpression head
abstract class SExpressionNode {

    ////
    //// Methods
    ////

    // Converts an SExpressionNode to a string
    public abstract String toString();

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
        System.out.print("Taking car of non-pair thing:");
        System.out.println(toString());
        return new SExpression();
    }

    // Returns the tail of a list
    public SExpression cdr() {
        System.out.print("Taking cdr of non-pair thing:");
        System.out.println(toString());
        return new SExpression();
    }

}