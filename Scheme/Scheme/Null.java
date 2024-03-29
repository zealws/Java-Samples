// Scheme language interpreter
// SExpression Representations
// Partially modified from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

// Null class
// Represents a null value or an empty list
public class Null extends SExpression {

    ////
    //// Methods
    ////

    // Converts an SExpressionNode to a string
    public String toString() {
        return "()";
    }

    // Converts an SExpressionNode to a string with type information
    public String toTypedString() {
        return "Null"+toString();
    }

    // Is the SExpression the empty list?
    public boolean isNull() {
        return true;
    }

}