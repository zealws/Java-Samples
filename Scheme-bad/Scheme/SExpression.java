// Scheme language interpreter
// SExpression Representations
// Shamelessly stolen/adopted from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

// SExpression class
// Handle class for all S-Expressions
public class SExpression {

    ////
    //// Data Members
    ////

    protected SExpressionNode myNode = null;

    ////
    //// Constructors
    ////

    // Creates an SExpression with a particular body node.
    protected SExpression(SExpressionNode initNode) {
        myNode = initNode;
    }

    // Creates an SExpression head with no body.
    public SExpression() {
        // Do Nothing
    }

    ////
    //// Methods
    ////

    // Returns the string version of the SExpression
    public String toString() {
        return myNode.toString();
    }

    // Returns true if the SExpression is the empty list
    public boolean isNull() {
        return myNode.isNull();
    }

    // Returns true if the SExpression is a symbol
    public boolean isSymbol() {
        return myNode.isSymbol();
    }

    // Returns true if the SExpression is a boolean
    public boolean isBoolean() {
        return myNode.isBoolean();
    }

    // Returns true if the SExpression is a pair
    public boolean isPair() {
        return myNode.isPair();
    }

    // Returns the head of a list structure.
    // Also the first item in a cons cell.
    public SExpression car() {
        return myNode.car();
    }

    // Returns the tail of a list structure.
    // Also the second item in a cons cell.
    public SExpression cdr() {
        return myNode.cdr();
    }

    // Compares to another SExpression
    public boolean compare(SExpression other) {
        boolean stringEq = toString() == other.toString();
        return
            (isNull() && other.isNull())
                || (stringEq && (isBoolean() && other.isBoolean()) || (isSymbol() && other.isSymbol()))
                || (myNode == other.myNode && isPair() && other.isPair());
    }

    // Compares to another SExpression
    public boolean isEq(SExpression other) {
        return compare(other);
    }

}