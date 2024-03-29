// Scheme language interpreter
// SExpression Representations
// Partially modified from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

// Symbol class
// Represents a symbol expression
public class Symbol extends SExpression {

    ////
    //// Data Members
    ////

    // The symbol data
    private String myName;

    ////
    //// Constructors
    ////

    public Symbol(String name) {
        myName = name;
    }

    ////
    //// Methods
    ////

    // Converts an SExpressionNode to a string
    public String toString() {
        return myName;
    }

    // Converts an SExpressionNode to a string with type information
    public String toTypedString() {
        return "Symbol("+toString()+")";
    }

    // Is the SExpression a symbol?
    public boolean isSymbol() {
        return true;
    }

}