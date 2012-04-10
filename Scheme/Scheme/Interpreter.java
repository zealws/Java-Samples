// Scheme language interpreter
// Main Interpreter file
// Partially modified from Dr. Zaring's sample C++ code.

package Scheme;

import Scheme.*;

public class Interpreter {

    ////
    //// Main Interpreter Methods
    ////

    // Interprets an SExpression
    public SExpression interpret(SExpression e) {
        return schemeValue(e);
    }

    // Interprets a String
    // Identical return values as Interpret(SExpression)
    public SExpression interpret(String s) {
        return interpret(SExpression.fromString(s));
    }

    ////
    //// Interpreter Bulk
    ////

    // Returns the result of evaluating expr in the default environment.
    // If an error occurs, returns null.
    private SExpression schemeValue(SExpression expr) {
        return exprValue(expr, defaultEnvironment());
    }

    // Returns the result of evaluating expr in env.
    // If an error occurs, returns null.
    private SExpression exprValue(SExpression expr, SExpression env) {
        if(expr.isEqual(new SBoolean(true))) {
            // #t expression is self-evaluating
            return new SBoolean(true);
        }
        else if(expr.isEqual(new SBoolean(false))) {
            // #f expression is self-evaluating
            return new SBoolean(false);
        }
        else if(expr.isEqual(new Null())) {
            // Null expression is self-evaluating
            return new Null();
        }
        else if(expr.isSymbol()) {
            // Lookup a symbol in the environment
            return symValue(expr, env);
        }
        else if(expr.isPair()) {
            // Pair
            SExpression car = expr.car();
            SExpression cdr = expr.cdr();
            if(car.isEqual(new Symbol("quote"))) {
                // Literal expression
                return cdr.car();
            }
            else if(car.isEqual(new Symbol("cond"))) {
                // Perform a cond expression
                return condValue(cdr,env);
            }
            else if(car.isEqual(new Symbol("lambda"))) {
                // Create a closure
                return cons(new Symbol("*closure*"),
                            cons(cdr.car(),
                                 cons(cdr.cdr().car(),
                                      env)));
            }
            else {
                // Application
                return appValue(exprValue(expr.car(),env),
                                exprListValue(expr.cdr(),env),
                                env);
            }
        }
        else {
            System.err.println("Unreachable situation reached...");
            System.err.println(expr.formattedString());
            return null;
        }
    }

    // Returns a list of the values of the elements of a list.
    private SExpression exprListValue(SExpression exprList, SExpression env) {
        if(exprList.isNull())
            return new Null();
        else
            return cons(exprValue(exprList.car(),env),exprListValue(exprList.cdr(),env));
    }

    // Returns the value of a symbol bound in an environment
    private SExpression symValue(SExpression sym, SExpression env) {
        if(env.isNull()) {
            System.err.print("Symbol not found: ");
            System.err.println(sym.formattedString());
            return null;
        }
        else if(sym.isEqual(env.car().car()))
            return env.car().cdr();
        else
            return symValue(sym,env.cdr());
    }

    // Returns the value of a cond expression
    private SExpression condValue(SExpression clauses, SExpression env) {
        if(clauses.car().car().isEqual(new Symbol("else")))
            return exprValue(clauses.car().cdr().car(),env);
        else if(exprValue(clauses.car().car(),env).isEqual(new SBoolean(false)))
            return condValue(clauses.cdr(),env);
        else
            return exprValue(clauses.car().cdr().car(),env);
    }

    private SExpression appValue(SExpression rator, SExpression randList, SExpression env) {
        if(rator.isSymbol()) {
            // Application of a built-in
            if(rator.isEqual(new Symbol("*car*"))) {
                return randList.car().car();
            }
            else if(rator.isEqual(new Symbol("*cdr*"))) {
                return randList.car().cdr();
            }
            else if(rator.isEqual(new Symbol("*cons*"))) {
                return cons(randList.car(),randList.cdr().car());
            }
            else if(rator.isEqual(new Symbol("*eq?*"))) {
                if(randList.car().isEqual(randList.cdr().car()))
                    return new SBoolean(true);
                else
                    return new SBoolean(false);
            }
            else if(rator.isEqual(new Symbol("*symbol?*"))) {
                if(randList.car().isSymbol())
                    return new SBoolean(true);
                else
                    return new SBoolean(false);
            }
            else if(rator.isEqual(new Symbol("*apply*"))) {
                return appValue(randList.car(),randList.cdr().car(),env);
            }
            else {
                System.err.println("I don't know what this is...");
                System.err.println(rator.formattedString());
                return null;
            }
        }
        else if(rator.car().isEqual(new Symbol("*closure*"))) {
            // Application of a closure
            return exprValue(rator.cdr().cdr().car(),
                             augmentedEnv(rator.cdr().car(),
                                          randList,
                                          rator.cdr().cdr().cdr()));
        }
        else {
            System.err.println("I don't know what this is...");
            System.err.println(rator.formattedString());
            return null;
        }
    }

    private SExpression augmentedEnv(SExpression symList, SExpression randList, SExpression env) {
        if(symList.isNull())
            return env;
        else
            return cons(cons(symList.car(),randList.car()),
                        augmentedEnv(symList.cdr(),
                                     randList.cdr(),
                                     env));
    }

    ////
    //// Herlper Functions and primitives
    ////

    // Cons primitive
    private static SExpression cons(SExpression head, SExpression tail) {
        return new Pair(head, tail);
    }

    // Returns the default environment
    private static SExpression defaultEnvironment() {
        return SExpression.fromString("((car . *car*) (cdr . *cdr*) (cons . *cons*) (eq? . *eq?*) (symbol? . *symbol?*) (apply . *apply*))");
    }

}