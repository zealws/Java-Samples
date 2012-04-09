// Scheme language interpreter
// SExpression Representations
// Shamelessly stolen/adopted from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

// Symbol class
// Representation of a symbol object
public class Symbol extends SExpression {

    ////
    //// Constructors
    ////

    // Creates an symbol with a particular body node.
    protected Symbol(SymbolNode initNode) {
        super(initNode);
    }

    // Creates a named symbol
    public Symbol(String name) {
        super(new SymbolNode(name));
    }


}