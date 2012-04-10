// Scheme language interpreter
// SExpression Representations
// Shamelessly stolen/adopted from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

// Null class
// Represents a null value or an empty list
public class Null extends SExpression {

    ////
    //// Constructors
    ////

    // Creates a Null with a particular body
    private Null(NullNode nodePointer) {
        super(nodePointer);
    }

    // Creates a Null with a null body
    public Null() {
        super(new NullNode());
    }

}