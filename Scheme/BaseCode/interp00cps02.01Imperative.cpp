//
// A CPS Interpreter for Scheme, C++ Version 02.01
//
// (based on a translation of interp00cps02.ss into iterative C++)
// (no garbage collection/storage management is done in this version)
// (some support-routines use non-tail recursion)
//


#include <iostream>
#include <string>


#include "sExpression.h"


//
// Function Declarations
//


sExpression schemeValue(sExpression expr);
sExpression exprValue(sExpression expr, sExpression env, sExpression C);
sExpression exprListValue(sExpression exprList, sExpression env, sExpression C);
sExpression symValue(sExpression sym, sExpression env, sExpression C);
sExpression condValue(sExpression clauses, sExpression env, sExpression C);
sExpression appValue(sExpression rator, sExpression randList, sExpression env, sExpression C);
sExpression augmentedEnv(sExpression symList, sExpression randList, sExpression env, sExpression C);
sExpression applyC(sExpression C, sExpression answer);


std::string toPrettyString(sExpression E);
std::string listContentsToPrettyString(sExpression list);
void readSExpression(sExpression & E);
void readList(sExpression & E);
void readBoolean(sExpression & E);
void readSymbol(sExpression & E);


//
// Macros for Scheme Primitives
//


#define carPrimitive(s) ((s).car())
#define cdrPrimitive(s) ((s).cdr())

#define caarPrimitive(s) (carPrimitive(carPrimitive(s)))
#define cadrPrimitive(s) (carPrimitive(cdrPrimitive(s)))
#define cdarPrimitive(s) (cdrPrimitive(carPrimitive(s)))
#define cadarPrimitive(s) (carPrimitive(cdrPrimitive(carPrimitive(s))))
#define caddrPrimitive(s) (carPrimitive(cdrPrimitive(cdrPrimitive(s))))
#define cadddrPrimitive(s) (carPrimitive(cdrPrimitive(cdrPrimitive(cdrPrimitive(s)))))

#define consPrimitive(s1, s2) (pair((s1), (s2)))

#define isSymbolPrimitive(s) ((s).isSymbol() ? trueLiteral : falseLiteral)
#define isEqPrimitive(s1, s2) ((s1).isEq(s2) ? trueLiteral : falseLiteral)


//
// Macros for Continuation-Aggregate ADT's
//


#define makeCid() (consPrimitive(CidTagLiteral, nullListLiteral))
#define isCid(C) (isEqPrimitive(carPrimitive(C), CidTagLiteral))

#define makeC1(expr, env, C) (consPrimitive(C1TagLiteral, consPrimitive((expr), consPrimitive((env), consPrimitive((C), nullListLiteral)))))
#define isC1(C) (isEqPrimitive(carPrimitive(C), C1TagLiteral))
#define C1ToExpr(C) (cadrPrimitive(C))
#define C1ToEnv(C) (caddrPrimitive(C))
#define C1ToC(C) (cadddrPrimitive(C))

#define makeC2(answer, env, C) (consPrimitive(C2TagLiteral, consPrimitive((answer), consPrimitive((env), consPrimitive((C), nullListLiteral)))))
#define isC2(C) (isEqPrimitive(carPrimitive(C), C2TagLiteral))
#define C2ToAnswer(C) (cadrPrimitive(C))
#define C2ToEnv(C) (caddrPrimitive(C))
#define C2ToC(C) (cadddrPrimitive(C))

#define makeC3(exprList, env, C) (consPrimitive(C3TagLiteral, consPrimitive((exprList), consPrimitive((env), consPrimitive((C), nullListLiteral)))))
#define isC3(C) (isEqPrimitive(carPrimitive(C), C3TagLiteral))
#define C3ToExprList(C) (cadrPrimitive(C))
#define C3ToEnv(C) (caddrPrimitive(C))
#define C3ToC(C) (cadddrPrimitive(C))

#define makeC4(answer, C) (consPrimitive(C4TagLiteral, consPrimitive((answer), consPrimitive((C), nullListLiteral))))
#define isC4(C) (isEqPrimitive(carPrimitive(C), C4TagLiteral))
#define C4ToAnswer(C) (cadrPrimitive(C))
#define C4ToC(C) (caddrPrimitive(C))

#define makeC5(clauses, env, C) (consPrimitive(C5TagLiteral, consPrimitive((clauses), consPrimitive((env), consPrimitive((C), nullListLiteral)))))
#define isC5(C) (isEqPrimitive(carPrimitive(C), C5TagLiteral))
#define C5ToClauses(C) (cadrPrimitive(C))
#define C5ToEnv(C) (caddrPrimitive(C))
#define C5ToC(C) (cadddrPrimitive(C))

#define makeC6(rator, C) (consPrimitive(C6TagLiteral, consPrimitive((rator), consPrimitive((C), nullListLiteral))))
#define isC6(C) (isEqPrimitive(carPrimitive(C), C6TagLiteral))
#define C6ToRator(C) (cadrPrimitive(C))
#define C6ToC(C) (caddrPrimitive(C))

#define makeC7(symList, randList, C) (consPrimitive(C7TagLiteral, consPrimitive((symList), consPrimitive((randList), consPrimitive((C), nullListLiteral)))))
#define isC7(C) (isEqPrimitive(carPrimitive(C), C7TagLiteral))
#define C7ToSymList(C) (cadrPrimitive(C))
#define C7ToRandList(C) (caddrPrimitive(C))
#define C7ToC(C) (cadddrPrimitive(C))


//
// Scheme Literals
//


sExpression carTagLiteral = symbol("*car*");
sExpression cdrTagLiteral = symbol("*cdr*");
sExpression consTagLiteral = symbol("*cons*");
sExpression isEqTagLiteral = symbol("*eq?*");
sExpression isSymbolTagLiteral = symbol("*symbol?*");
sExpression applyTagLiteral = symbol("*apply*");
sExpression callCcTagLiteral = symbol("*call/cc*");


sExpression falseLiteral = boolean(false);
sExpression trueLiteral = boolean(true);


sExpression nullListLiteral = null();


sExpression quoteLiteral = symbol("quote");


sExpression condLiteral = symbol("cond");
sExpression elseLiteral = symbol("else");


sExpression lambdaLiteral = symbol("lambda");
sExpression closureTagLiteral = symbol("*closure*");


sExpression continuationTagLiteral = symbol("*continuation*");


sExpression primitiveEnv =
    consPrimitive(
        consPrimitive(symbol("car"), carTagLiteral),
        consPrimitive(
            consPrimitive(symbol("cdr"), cdrTagLiteral),
            consPrimitive(
                consPrimitive(symbol("cons"), consTagLiteral),
                consPrimitive(
                    consPrimitive(symbol("eq?"), isEqTagLiteral),
                    consPrimitive(
                        consPrimitive(symbol("symbol?"), isSymbolTagLiteral),
                        consPrimitive(
                            consPrimitive(symbol("apply"), applyTagLiteral),
                            consPrimitive(
                                consPrimitive(symbol("call/cc"), callCcTagLiteral),
                                nullListLiteral)))))));


sExpression CidTagLiteral = symbol("*cid*");
sExpression C1TagLiteral = symbol("*c1*");
sExpression C2TagLiteral = symbol("*c2*");
sExpression C3TagLiteral = symbol("*c3*");
sExpression C4TagLiteral = symbol("*c4*");
sExpression C5TagLiteral = symbol("*c5*");
sExpression C6TagLiteral = symbol("*c6*");
sExpression C7TagLiteral = symbol("*c7*");


//
// Interpreter
//


sExpression schemeValue(sExpression expr) {

    sExpression clauses, env, exprList, C, randList, rator, sym, symList, answer;

    expr = expr;
    env = primitiveEnv;
    C = makeCid();
    goto exprValue;

    exprValue:

        if (isEqPrimitive(expr, trueLiteral).isEq(trueLiteral)) {
            C = C;
            answer = trueLiteral;
            goto applyC;
            }
        else if (isEqPrimitive(expr, falseLiteral).isEq(trueLiteral)) {
            C = C;
            answer = falseLiteral;
            goto applyC;
            }
        else if (isEqPrimitive(expr, nullListLiteral).isEq(trueLiteral)) {
            C = C;
            answer = nullListLiteral;
            goto applyC;
            }
        else if (isSymbolPrimitive(expr).isEq(trueLiteral)) {
            sym = expr;
            env = env;
            C = C;
            goto symValue;
            }
        else if (isEqPrimitive(carPrimitive(expr), quoteLiteral).isEq(trueLiteral)) {
            C = C;
            answer = cadrPrimitive(expr);
            goto applyC;
            }
        else if (isEqPrimitive(carPrimitive(expr), condLiteral).isEq(trueLiteral)) {
            clauses = cdrPrimitive(expr);
            env = env;
            C = C;
            goto condValue;
            }
        else if (isEqPrimitive(carPrimitive(expr), lambdaLiteral).isEq(trueLiteral)) {
            C = C;
            answer = consPrimitive(closureTagLiteral, consPrimitive(cadrPrimitive(expr), consPrimitive(caddrPrimitive(expr), consPrimitive(env, nullListLiteral))));
            goto applyC;
            }
        else {
            C = makeC1(expr, env, C);
            expr = carPrimitive(expr);
            env = env;
            goto exprValue;
            }

    exprListValue:

        if (isEqPrimitive(exprList, nullListLiteral).isEq(trueLiteral)) {
            C = C;
            answer = nullListLiteral;
            goto applyC;
            }
        else {
            C = makeC3(exprList, env, C);
            expr = carPrimitive(exprList);
            env = env;
            goto exprValue;
            }

    symValue:

        if (isEqPrimitive(caarPrimitive(env), sym).isEq(trueLiteral)) {
            C = C;
            answer = cdarPrimitive(env);
            goto applyC;
            }
        else {
            sym = sym;
            env = cdrPrimitive(env);
            C = C;
            goto symValue;
            }

    condValue:

        if (isEqPrimitive(caarPrimitive(clauses), elseLiteral).isEq(trueLiteral)) {
            expr = cadarPrimitive(clauses);
            env = env;
            C = C;
            goto exprValue;
            }
        else {
            C = makeC5(clauses, env, C);
            expr = caarPrimitive(clauses);
            env = env;
            goto exprValue;
            }

    appValue:

        if (isSymbolPrimitive(rator).isEq(trueLiteral))
            if (isEqPrimitive(rator, carTagLiteral).isEq(trueLiteral)) {
                C = C;
                answer = caarPrimitive(randList);
                goto applyC;
                }
            else if (isEqPrimitive(rator, cdrTagLiteral).isEq(trueLiteral)) {
                C = C;
                answer = cdarPrimitive(randList);
                goto applyC;
                }
           else if (isEqPrimitive(rator, consTagLiteral).isEq(trueLiteral)) {
                C = C;
                answer = consPrimitive(carPrimitive(randList), cadrPrimitive(randList));
                goto applyC;
                }
            else if (isEqPrimitive(rator, isEqTagLiteral).isEq(trueLiteral)) {
                C = C;
                answer = isEqPrimitive(carPrimitive(randList), cadrPrimitive(randList));
                goto applyC;
                }
            else if (isEqPrimitive(rator, isSymbolTagLiteral).isEq(trueLiteral)) {
                C = C;
                answer = isSymbolPrimitive(carPrimitive(randList));
                goto applyC;
                }
            else if (isEqPrimitive(rator, applyTagLiteral).isEq(trueLiteral)) {
                rator = carPrimitive(randList);
                randList = cadrPrimitive(randList);
                env = env;
                C = C;
                goto appValue;
                }
           else if (isEqPrimitive(rator, callCcTagLiteral).isEq(trueLiteral)) {
                rator = carPrimitive(randList);
                randList = consPrimitive(consPrimitive(continuationTagLiteral, consPrimitive(C, nullListLiteral)), nullListLiteral);
                env = env;
                C = C;
                goto appValue;
                }
            else
                ;
        else if (isEqPrimitive(carPrimitive(rator), closureTagLiteral).isEq(trueLiteral)) {
            C = makeC6(rator, C);
            symList = cadrPrimitive(rator);
            randList = randList;
            env = cadddrPrimitive(rator);
            goto augmentedEnv;
            }
        else if (isEqPrimitive(carPrimitive(rator), continuationTagLiteral).isEq(trueLiteral)) {
            C = cadrPrimitive(rator);
            answer = carPrimitive(randList);
            goto applyC;
            }

    augmentedEnv:

        if (isEqPrimitive(symList, nullListLiteral).isEq(trueLiteral)) {
            C = C;
            answer = env;
            goto applyC;
            }
        else {
            C = makeC7(symList, randList, C);
            symList = cdrPrimitive(symList);
            randList = cdrPrimitive(randList);
            env = env;
            goto augmentedEnv;
            }

    applyC:

        if (isEqPrimitive(isCid(C), trueLiteral).isEq(trueLiteral))
            return answer;
        else if (isEqPrimitive(isC1(C), trueLiteral).isEq(trueLiteral)) {
            exprList = cdrPrimitive(C1ToExpr(C));
            env = C1ToEnv(C);
            C = makeC2(answer, C1ToEnv(C), C1ToC(C));
            goto exprListValue;
            }
        else if (isEqPrimitive(isC2(C), trueLiteral).isEq(trueLiteral)) {
            rator = C2ToAnswer(C);
            randList = answer;
            env = C2ToEnv(C);
            C = C2ToC(C);
            goto appValue;
            }
        else if (isEqPrimitive(isC3(C), trueLiteral).isEq(trueLiteral)) {
            exprList = cdrPrimitive(C3ToExprList(C));
            env = C3ToEnv(C);
            C = makeC4(answer, C3ToC(C));
            goto exprListValue;
            }
        else if (isEqPrimitive(isC4(C), trueLiteral).isEq(trueLiteral)) {
            answer = consPrimitive(C4ToAnswer(C), answer);
            C = C4ToC(C);
            goto applyC;
            }
        else if (isEqPrimitive(isC5(C), trueLiteral).isEq(trueLiteral))
            if (isEqPrimitive(answer, falseLiteral).isEq(trueLiteral)) {
                clauses = cdrPrimitive(C5ToClauses(C));
                env = C5ToEnv(C);
                C = C5ToC(C);
                goto condValue;
                }
            else {
                expr = cadarPrimitive(C5ToClauses(C));
                env = C5ToEnv(C);
                C = C5ToC(C);
                goto exprValue;
                }
        else if (isEqPrimitive(isC6(C), trueLiteral).isEq(trueLiteral)) {
            expr = caddrPrimitive(C6ToRator(C));
            env = answer;
            C = C6ToC(C);
            goto exprValue;
            }
        else if (isEqPrimitive(isC7(C), trueLiteral).isEq(trueLiteral)) {
            answer = consPrimitive(consPrimitive(carPrimitive(C7ToSymList(C)), carPrimitive(C7ToRandList(C))), answer);
            C = C7ToC(C);
            goto applyC;
            }
            
    }


//
// Main Program
//


int main()
{

    sExpression E;

    std::cin >> std::noskipws;

    for (;;) {

        std::cout << "scheme00> ";

        readSExpression(E);

        std::cout << toPrettyString(schemeValue(E)) << std::endl << std::endl;

        }

    return 0;

    }


//
// S-Expression Input/Output
//


std::string toPrettyString(sExpression E)
{

    if (E.isPair())
        return "(" + listContentsToPrettyString(E) + ")";
    else
        return E.toString();

    }


std::string listContentsToPrettyString(sExpression list)
{

    if (list.cdr().isNull())
        return toPrettyString(list.car());
    else if (list.cdr().isPair())
        return toPrettyString(list.car()) + " " + listContentsToPrettyString(list.cdr());
    else
        return toPrettyString(list.car()) + " . " + toPrettyString(list.cdr());

    }


void readSExpression(sExpression & E)
{

    char ch;

    for (std::cin >> ch; ch == ' ' || ch == '\t' || ch == '\n'; std::cin >> ch)
        ; // do nothing

    if (ch == '\'') {

        sExpression literalSExpression;

        readSExpression(literalSExpression);
        E = consPrimitive(quoteLiteral, consPrimitive(literalSExpression, nullListLiteral));

        }

    else if (ch == '(')

        readList(E);

    else if (ch == '#')

        readBoolean(E);

    else {

        std::cin.putback(ch);

        readSymbol(E);

        }

    }


void readList(sExpression & E)
{

    char ch;

    for (std::cin >> ch; ch == ' ' || ch == '\t' || ch == '\n'; std::cin >> ch)
        ; // do nothing

    if (ch == ')')

        E = nullListLiteral;

    else if (ch == '.') {

        readSExpression(E);

        for (std::cin >> ch; ch == ' ' || ch == '\t' || ch == '\n'; std::cin >> ch)
            ; // do nothing

        }
    else {

        sExpression headSExpression;
        sExpression tailSExpression;

        std::cin.putback(ch);

        readSExpression(headSExpression);
        readList(tailSExpression);

        E = pair(headSExpression, tailSExpression);

        }

    }


void readBoolean(sExpression & E)
{

    char ch;

    std::cin >> ch;

    if (ch == 't')
        E = trueLiteral;
    else
        E = falseLiteral;

    }


void readSymbol(sExpression & E)
{

    char ch;
    std::string newString;

    for (std::cin >> ch;
            ch != ' ' && ch != '\t' && ch != '\n' && ch != '(' && ch != ')';
            std::cin >> ch)
        newString += ch;

    std::cin.putback(ch);

    E = symbol(newString);

    }
