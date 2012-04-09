// Scheme language interpreter
// SExpression Representations
// Shamelessly stolen/adopted from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

// SymbolNode class
// Provides the body for an Symbol expression
class SymbolNode extends SExpressionNode {

    ////
    //// Data Members
    ////

    // The symbol data
    private String myName;

    ////
    //// Constructors
    ////

    SymbolNode(String name) {
        myName = name;
    }

    ////
    //// Methods
    ////

    // Converts an SExpressionNode to a string
    public String toString() {
        return myName;
    }

    // Is the SExpression a symbol?
    public boolean isSymbol() {
        return true;
    }

}