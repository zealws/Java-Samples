// Scheme language interpreter
// SExpression Representations
// Shamelessly stolen/adopted from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

// SBooleanNode class
// Provides the body for a boolean expression
// So named to avoid conflicts with the built-in Boolean class.
class SBooleanNode extends SExpressionNode {

    ////
    //// Data Members
    ////

    private boolean myValue;

    ////
    //// Constructors
    ////

    public SBooleanNode(boolean value) {
        myValue = value;
    }

    ////
    //// Methods
    ////

    // Converts an SExpressionNode to a string
    public String toString() {
        if(myValue)
            return "#t";
        else
            return "#f";
    }

    // Is the SExpression a boolean?
    public boolean isBoolean() {
        return true;
    }

}