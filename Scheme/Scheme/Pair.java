// Scheme language interpreter
// SExpression Representations
// Shamelessly stolen/adopted from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

// Pair class
// Handle class for all cons cells
public class Pair extends SExpression {

    ////
    //// Constructors
    ////

    protected Pair(PairNode node) {
        super(node);
    }

    public Pair(SExpression car, SExpression cdr) {
        super(new PairNode(car,cdr));
    }

}