// Scheme language interpreter
// SExpression Representations
// Partially modified from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

// Boolean class
// Represents a boolean expression
// So named to avoid conflicts with the built-in Boolean class.
public class SBoolean extends SExpression {

    ////
    //// Data Members
    ////

    private boolean myValue;

    ////
    //// Constructors
    ////

    public SBoolean(boolean value) {
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

    // Converts an SExpressionNode to a string with type information
    public String toTypedString() {
        return "Boolean("+toString()+")";
    }

    // Is the SExpression a boolean?
    public boolean isBoolean() {
        return true;
    }

}