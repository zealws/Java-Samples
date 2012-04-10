// Scheme language interpreter
// SExpression Representations
// Shamelessly stolen/adopted from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

// Boolean class
// Handle class for boolean expressions
// So named to avoid conflicts with the built-in Boolean class.
public class SBoolean extends SExpression {

    ////
    //// Constructors
    ////

    // Creates an SExpression with a particular body node.
    protected SBoolean(SBooleanNode initNode) {
        super(initNode);
    }

    // Creates an SExpression head with no body.
    public SBoolean(boolean value) {
        super(new SBooleanNode(value));
    }

}