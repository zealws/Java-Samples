// Scheme language interpreter
// SExpression Representations
// Shamelessly stolen/adopted from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

// NullNode class
// Provides the body for a null expression
class NullNode extends SExpressionNode {

    ////
    //// Methods
    ////

    // Converts an SExpressionNode to a string
    public String toString() {
        return "()";
    }

    // Is the SExpression the empty list?
    public boolean isNull() {
        return true;
    }

}