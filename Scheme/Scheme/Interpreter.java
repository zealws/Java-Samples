// Scheme language interpreter
// SExpression Representations
// Shamelessly stolen/adopted from Dr. Zaring's sample C++ code.

package Scheme;

// The scheme proper
import Scheme.*;

import java.io.*;

// The heart of the interpreter
// This class is basically a conglomeration of files for evaluating scheme code.
// It might not make much sense, since we've done all sorts of crap to make it
// implementable without continuations...
public class Interpreter {

    ////
    //// Scheme Literals
    ////

    private SExpression carTagLiteral = new Symbol("*car*");
    private SExpression cdrTagLiteral = new Symbol("*cdr*");
    private SExpression consTagLiteral = new Symbol("*cons*");
    private SExpression isEqTagLiteral = new Symbol("*eq?*");
    private SExpression isSymbolTagLiteral = new Symbol("*symbol?*");
    private SExpression applyTagLiteral = new Symbol("*apply*");
    private SExpression callCcTagLiteral = new Symbol("*call/cc*");

    private SExpression falseLiteral = new SBoolean(false);
    private SExpression trueLiteral = new SBoolean(true);

    private SExpression nullListLiteral = new Null();

    private SExpression quoteLiteral = new Symbol("quote");

    private SExpression condLiteral = new Symbol("cond");
    private SExpression elseLiteral = new Symbol("else");

    private SExpression lambdaLiteral = new Symbol("lambda");
    private SExpression closureTagLiteral = new Symbol("*closure*");

    private SExpression continuationTagLiteral = new Symbol("*continuation*");

    private SExpression primitiveEnv =
        cons(
            cons(new Symbol("car"), carTagLiteral),
            cons(
                cons(new Symbol("cdr"), cdrTagLiteral),
                cons(
                    cons(new Symbol("cons"), consTagLiteral),
                    cons(
                        cons(new Symbol("eq?"), isEqTagLiteral),
                        cons(
                            cons(new Symbol("symbol?"), isSymbolTagLiteral),
                            cons(
                                cons(new Symbol("apply"), applyTagLiteral),
                                cons(
                                    cons(new Symbol("call/cc"), callCcTagLiteral),
                                    nullListLiteral)))))));


    private SExpression cidTagLiteral = new Symbol("*cid*");
    private SExpression c1TagLiteral = new Symbol("*c1*");
    private SExpression c2TagLiteral = new Symbol("*c2*");
    private SExpression c3TagLiteral = new Symbol("*c3*");
    private SExpression c4TagLiteral = new Symbol("*c4*");
    private SExpression c5TagLiteral = new Symbol("*c5*");
    private SExpression c6TagLiteral = new Symbol("*c6*");
    private SExpression c7TagLiteral = new Symbol("*c7*");

    ////
    //// Interpreter body
    ////

    private SExpression schemeValue(SExpression expr) {
        // Zaring's code actually used functionToUse ='s, which I neither could nor wanted to replicate in java.
        // However, to simulate the same optimized non-stack-growing behaviour, I decided to do this, rather
        // than have a whole bunch of function calls.

        final int exprValue = 0;
        final int exprListValue = 1;
        final int symValue = 2;
        final int condValue = 3;
        final int appValue = 4;
        final int augmentedEnv = 5;
        final int applyC = 6;

        SExpression clauses = null;
        SExpression env = primitiveEnv;
        SExpression exprList = null;
        SExpression C = cons(cidTagLiteral,nullListLiteral);
        SExpression randList = null;
        SExpression rator = null;
        SExpression sym = null;
        SExpression symList = null;
        SExpression answer = null;

        int functionToUse = exprValue;

        while(true) {
            switch(functionToUse) {
                case exprValue:
                    if (isEq(expr, trueLiteral).isEq(trueLiteral)) {
                        answer = trueLiteral;
                        functionToUse = applyC;
                    }
                    else if (isEq(expr, falseLiteral).isEq(trueLiteral)) {
                        answer = falseLiteral;
                        functionToUse = applyC;
                    }
                    else if (isEq(expr, nullListLiteral).isEq(trueLiteral)) {
                        answer = nullListLiteral;
                        functionToUse = applyC;
                    }
                    else if (isSymbol(expr).isEq(trueLiteral)) {
                        sym = expr;
                        functionToUse = symValue;
                    }
                    else if (isEq(expr.car(), quoteLiteral).isEq(trueLiteral)) {
                        answer = expr.cdr().car();
                        functionToUse = applyC;
                    }
                    else if (isEq(expr.car(), condLiteral).isEq(trueLiteral)) {
                        clauses = expr.cdr();
                        functionToUse = condValue;
                    }
                    else if (isEq(expr.car(), lambdaLiteral).isEq(trueLiteral)) {
                        answer = cons(closureTagLiteral, cons(expr.cdr().car(), cons(expr.cdr().cdr().car(), cons(env, nullListLiteral))));
                        functionToUse = applyC;
                    }
                    else {
                        C = makeC1(expr, env, C);
                        expr = expr.car();
                        functionToUse = exprValue;
                    }
                    break;
                case exprListValue:
                    if (isEq(exprList, nullListLiteral).isEq(trueLiteral)) {
                        answer = nullListLiteral;
                        functionToUse = applyC;
                    }
                    else {
                        C = makeC3(exprList, env, C);
                        expr = exprList.car();
                        functionToUse = exprValue;
                    }
                    break;
                case symValue:
                    if (isEq(env.car().car(), sym).isEq(trueLiteral)) {
                        answer = env.cdr().car();
                        functionToUse = applyC;
                    }
                    else {
                        env = env.cdr();
                        functionToUse = symValue;
                    }
                    break;
                case condValue:
                    if (isEq(clauses.car().car(), elseLiteral).isEq(trueLiteral)) {
                        expr = clauses.car().cdr().car();
                        functionToUse = exprValue;
                    }
                    else {
                        C = makeC5(clauses, env, C);
                        expr = clauses.car().car();
                        functionToUse = exprValue;
                    }
                    break;
                case appValue:
                    if (isSymbol(rator).isEq(trueLiteral))
                        if (isEq(rator, carTagLiteral).isEq(trueLiteral)) {
                            answer = randList.car().car();
                            functionToUse = applyC;
                        }
                        else if (isEq(rator, cdrTagLiteral).isEq(trueLiteral)) {
                            answer = randList.car().cdr();
                            functionToUse = applyC;
                        }
                    else if (isEq(rator, consTagLiteral).isEq(trueLiteral)) {
                            answer = cons(randList.car(), randList.cdr().car());
                            functionToUse = applyC;
                        }
                        else if (isEq(rator, isEqTagLiteral).isEq(trueLiteral)) {
                            answer = isEq(randList.car(), randList.cdr().car());
                            functionToUse = applyC;
                        }
                        else if (isEq(rator, isSymbolTagLiteral).isEq(trueLiteral)) {
                            answer = isSymbol(randList.car());
                            functionToUse = applyC;
                        }
                        else if (isEq(rator, applyTagLiteral).isEq(trueLiteral)) {
                            rator = randList.car();
                            randList = randList.cdr().car();
                            functionToUse = appValue;
                        }
                    else if (isEq(rator, callCcTagLiteral).isEq(trueLiteral)) {
                            rator = randList.car();
                            randList = cons(cons(continuationTagLiteral, cons(C, nullListLiteral)), nullListLiteral);
                            functionToUse = appValue;
                        }
                        else
                            ;
                    else if (isEq(rator.car(), closureTagLiteral).isEq(trueLiteral)) {
                        C = makeC6(rator, C);
                        symList = rator.cdr().car();
                        env = rator.cdr().cdr().cdr().car();
                        functionToUse = augmentedEnv;
                    }
                    else if (isEq(rator.car(), continuationTagLiteral).isEq(trueLiteral)) {
                        C = rator.cdr().car();
                        answer = randList.car();
                        functionToUse = applyC;
                    }
                    break;
                case augmentedEnv:
                    if (isEq(symList, nullListLiteral).isEq(trueLiteral)) {
                        answer = env;
                        functionToUse = applyC;
                    }
                    else {
                        C = makeC7(symList, randList, C);
                        symList = symList.cdr();
                        randList = randList.cdr();
                        functionToUse = augmentedEnv;
                    }
                    break;
                case applyC:
                    if (isEq(isCid(C), trueLiteral).isEq(trueLiteral))
                        return answer;
                    else if (isEq(isC1(C), trueLiteral).isEq(trueLiteral)) {
                        exprList = C1ToExpr(C).cdr();
                        env = C1ToEnv(C);
                        C = makeC2(answer, C1ToEnv(C), C1ToC(C));
                        functionToUse = exprListValue;
                    }
                    else if (isEq(isC2(C), trueLiteral).isEq(trueLiteral)) {
                        rator = C2ToAnswer(C);
                        randList = answer;
                        env = C2ToEnv(C);
                        C = C2ToC(C);
                        functionToUse = appValue;
                    }
                    else if (isEq(isC3(C), trueLiteral).isEq(trueLiteral)) {
                        exprList = C3ToExprList(C).cdr();
                        env = C3ToEnv(C);
                        C = makeC4(answer, C3ToC(C));
                        functionToUse = exprListValue;
                    }
                    else if (isEq(isC4(C), trueLiteral).isEq(trueLiteral)) {
                        answer = cons(C4ToAnswer(C), answer);
                        C = C4ToC(C);
                        functionToUse = applyC;
                    }
                    else if (isEq(isC5(C), trueLiteral).isEq(trueLiteral))
                        if (isEq(answer, falseLiteral).isEq(trueLiteral)) {
                            clauses = C5ToClauses(C).cdr();
                            env = C5ToEnv(C);
                            C = C5ToC(C);
                            functionToUse = condValue;
                        }
                        else {
                            expr = C5ToClauses(C).car().car().car();
                            env = C5ToEnv(C);
                            C = C5ToC(C);
                            functionToUse = exprValue;
                        }
                    else if (isEq(isC6(C), trueLiteral).isEq(trueLiteral)) {
                        expr = C6ToRator(C).cdr().cdr().car();
                        env = answer;
                        C = C6ToC(C);
                        functionToUse = exprValue;
                    }
                    else if (isEq(isC7(C), trueLiteral).isEq(trueLiteral)) {
                        answer = cons(cons(C7ToSymList(C).car(), C7ToRandList(C).car()), answer);
                        C = C7ToC(C);
                        functionToUse = applyC;
                    }
                    break;
            }
        }
    }

    ////
    //// Output Helper Functions
    ////

    public String toPrettyString(SExpression E) {
        if(E.isPair()) {
            return "(" + listContentsToPrettyString(E) + ")";
        }
        else
            return E.toString();
    }
    public String listContentsToPrettyString(SExpression list) {
        if(list.cdr().isNull())
            return toPrettyString(list.car());
        else if(list.cdr().isPair())
            return toPrettyString(list.car()) + " " + listContentsToPrettyString(list.cdr());
        else
            return toPrettyString(list.car()) + " . " + toPrettyString(list.cdr());
    }

    ////
    //// Input Parsers
    ////

    public SExpression readSExpression(CustomInputStream str) throws IOException {
        char ch = str.readChar();
        while(ch == ' ' || ch == '\t' || ch == '\n') {
            ch = str.readChar();
        }

        if(ch == '\'') {
            return cons(quoteLiteral, cons(readSExpression(str),nullListLiteral));
        }
        else if(ch == '(')
            return readList(str);
        else if(ch == '#')
            return readBoolean(str);
        else {
            str.putback(ch);
            return readSymbol(str);
        }
    }
    public SExpression readList(CustomInputStream str) throws IOException {
        char ch = str.readChar();
        while(ch == ' ' || ch == '\t' || ch == '\n') {
            ch = str.readChar();
        }

        if (ch == ')')

            return nullListLiteral;

        else if (ch == '.') {

            SExpression toReturn = readSExpression(str);

            ch = str.readChar();
            while(ch == ' ' || ch == '\t' || ch == '\n') {
                ch = str.readChar();
            }

            return toReturn;

        }
        else {

            str.putback(ch);
            SExpression headSExpression = readSExpression(str);
            SExpression tailSExpression = readList(str);

            return new Pair(headSExpression, tailSExpression);

        }
    }
    public SExpression readBoolean(CustomInputStream str) throws IOException {
        char ch = str.readChar();
        if (ch == 't')
            return trueLiteral;
        else
            return falseLiteral;
    }
    public SExpression readSymbol(CustomInputStream str) throws IOException {
        char ch = str.readChar();
        String newString = new String();

        while(ch != ' ' && ch != '\t' && ch != '\n' && ch != '(' && ch != ')') {
            newString += ch;
            ch = str.readChar();
        }

        str.putback(ch);

        return new Symbol(newString);
    }

    ////
    //// The 'main' function of the interpreter
    ////

    public void runInterpreter() {
        while(true) {
            System.out.print("scheme>> ");
            SExpression e;
            try {
                 e = readSExpression(new CustomInputStream());
            }
            catch(java.io.IOException c) {
                System.out.println("I/O error");
                return;
            }
            System.out.print("Str: ");
            System.out.println(e.toString());
            System.out.print("Exp: ");
            System.out.println(toPrettyString(e));
            System.out.print("Res: ");
            System.out.println(toPrettyString(schemeValue(e)));
        }
    }


    ////
    //// Helper Functions
    ////

    // In C++ this was a whole bunch of #define macros,
    // That's why it looks so sloppy.
    public SExpression cons(SExpression s1, SExpression s2) {
        return new Pair(s1, s2);
    }

    public SExpression isEq(SExpression s1, SExpression s2) {
        if(s1.compare(s2))
            return trueLiteral;
        else
            return falseLiteral;
    }

    public SExpression isSymbol(SExpression s1) {
        if(s1.isSymbol())
            return trueLiteral;
        else
            return falseLiteral;
    }

    public SExpression makeCid() {
        return cons(cidTagLiteral, nullListLiteral);
    }
    public SExpression isCid(SExpression C) {
        return isEq(C.car(), cidTagLiteral);
    }
    public SExpression makeC1(SExpression expr, SExpression env, SExpression C) {
        return cons(c1TagLiteral, cons(expr, cons(env, cons(C, nullListLiteral))));
    }
    public SExpression isC1(SExpression C) {
        return isEq(C.car(), c1TagLiteral);
    }
    public SExpression C1ToExpr(SExpression C) {
        return C.cdr().car();
    }
    public SExpression C1ToEnv(SExpression C) {
        return C.cdr().cdr().car();
    }
    public SExpression C1ToC(SExpression C) {
        return C.cdr().cdr().cdr().car();
    }
    public SExpression makeC2(SExpression answer, SExpression env, SExpression C) {
        return cons(c2TagLiteral, cons(answer, cons(env, cons(C, nullListLiteral))));
    }
    public SExpression isC2(SExpression C) {
        return isEq(C.car(), c2TagLiteral);
    }
    public SExpression C2ToAnswer(SExpression C) {
        return C.cdr().car();
    }
    public SExpression C2ToEnv(SExpression C) {
        return C.cdr().cdr().car();
    }
    public SExpression C2ToC(SExpression C) {
        return C.cdr().cdr().cdr().car();
    }

    public SExpression makeC3(SExpression exprList, SExpression env, SExpression C) {
        return cons(c3TagLiteral, cons(exprList, cons(env, cons(C, nullListLiteral))));
    }
    public SExpression isC3(SExpression C) {
        return isEq(C.car(), c3TagLiteral);
    }
    public SExpression C3ToExprList(SExpression C) {
        return C.cdr().car();
    }
    public SExpression C3ToEnv(SExpression C) {
        return C.cdr().cdr().car();
    }
    public SExpression C3ToC(SExpression C) {
        return C.cdr().cdr().cdr().car();
    }

    public SExpression makeC4(SExpression answer, SExpression C) {
        return cons(c4TagLiteral, cons(answer, cons(C, nullListLiteral)));
    }
    public SExpression isC4(SExpression C) {
        return isEq(C.car(), c4TagLiteral);
    }
    public SExpression C4ToAnswer(SExpression C) {
        return C.cdr().car();
    }
    public SExpression C4ToC(SExpression C) {
        return C.cdr().cdr().car();
    }

    public SExpression makeC5(SExpression clauses, SExpression env, SExpression C) {
        return cons(c5TagLiteral, cons(clauses, cons(env, cons(C, nullListLiteral))));
    }
    public SExpression isC5(SExpression C) {
        return isEq(C.car(), c5TagLiteral);
    }
    public SExpression C5ToClauses(SExpression C) {
        return C.cdr().car();
    }
    public SExpression C5ToEnv(SExpression C) {
        return C.cdr().cdr().car();
    }
    public SExpression C5ToC(SExpression C) {
        return C.cdr().cdr().cdr().car();
    }

    public SExpression makeC6(SExpression rator, SExpression C) {
        return cons(c6TagLiteral, cons(rator, cons(C, nullListLiteral)));
    }
    public SExpression isC6(SExpression C) {
        return isEq(C.car(), c6TagLiteral);
    }
    public SExpression C6ToRator(SExpression C) {
        return C.cdr().car();
    }
    public SExpression C6ToC(SExpression C) {
        return C.cdr().cdr().car();
    }

    public SExpression makeC7(SExpression symList, SExpression randList, SExpression C) {
        return cons(c7TagLiteral,
                    cons(symList,
                         cons(randList,
                              cons(C, nullListLiteral))));
    }
    public SExpression isC7(SExpression C) {
        return isEq(C.car(), c7TagLiteral);
    }
    public SExpression C7ToSymList(SExpression C) {
        return C.cdr().car();
    }
    public SExpression C7ToRandList(SExpression C) {
        return C.cdr().cdr().car();
    }
    public SExpression C7ToC(SExpression C) {
        return C.cdr().cdr().cdr().car();
    }

}