#include <string>


#include "sExpression.h"


////////
//////// class sExpression
////////


////
//// private members
////


sExpressionNode * & sExpression::nodePointerReference()
{

    return myNodePointer;

    }


sExpressionNode * const & sExpression::nodePointerConstReference() const
{

    return myNodePointer;

    }


////
//// protected members
////


sExpression::sExpression(sExpressionNode * nodePointer)
    : myNodePointer(nodePointer)
{

    }


////
//// public members
////


sExpression::sExpression()
    : myNodePointer(0)
{

    }


sExpression::~sExpression()
{

    }


std::string sExpression::toString() const
{

    return nodePointerConstReference() -> toString();

    }


bool sExpression::isNull() const
{

    return nodePointerConstReference() -> isNull();

    }


bool sExpression::isSymbol() const
{

    return nodePointerConstReference() -> isSymbol();

    }


bool sExpression::isBoolean() const
{

    return nodePointerConstReference() -> isBoolean();

    }


bool sExpression::isPair() const
{

    return nodePointerConstReference() -> isPair();

    }


sExpression sExpression::car() const
{

    return nodePointerConstReference() -> car();

    }


sExpression sExpression::cdr() const
{

    return nodePointerConstReference() -> cdr();

    }


bool sExpression::isEq(sExpression other) const
{

    return
        isNull() && other.isNull()
            || isBoolean() && other.isBoolean() && toString() == other.toString()
            || isSymbol() && other.isSymbol() && toString() == other.toString()
            || isPair() && other.isPair() && nodePointerConstReference() == other.nodePointerConstReference();

    }


////////
//////// class sExpressionNode
////////


////
//// private members
////


////
//// public members
////


sExpressionNode::~sExpressionNode()
{

    }


bool sExpressionNode::isNull() const
{

    return false;

    }


bool sExpressionNode::isSymbol() const
{

    return false;

    }


bool sExpressionNode::isBoolean() const
{

    return false;

    }


bool sExpressionNode::isPair() const
{

    return false;

    }


sExpression sExpressionNode::car() const
{

    return sExpression();

    }


sExpression sExpressionNode::cdr() const
{

    return sExpression();

    }


////////
//////// class null
////////


////
//// private members
////


null::null(nullNode * nodePointer)
    : sExpression(nodePointer)
{

    }


////
//// public members
////


null::null()
    : sExpression(new nullNode)
{

    }


////////
//////// class nullNode
////////


////
//// private members
////


////
//// public members
////


nullNode::nullNode()
    : sExpressionNode()
{

    }


std::string nullNode::toString() const
{

    return "()";

    }


bool nullNode::isNull() const
{

    return true;

    }


////////
//////// class symbol
////////


////
//// private members
////


symbol::symbol(symbolNode * nodePointer)
    : sExpression(nodePointer)
{

    }


////
//// public members
////


symbol::symbol(std::string const & name)
    : sExpression(new symbolNode(name))
{

    }


////////
//////// class symbolNode
////////


////
//// private members
////


std::string & symbolNode::nameReference()
{

    return myName;

    }


std::string const & symbolNode::nameConstReference() const
{

    return myName;

    }


////
//// public members
////


symbolNode::symbolNode(std::string const & name)
    : myName(name)
{

    }


std::string symbolNode::toString() const
{

    return nameConstReference();

    }


bool symbolNode::isSymbol() const
{

    return true;

    }


////////
//////// class boolean
////////


////
//// private members
////


boolean::boolean(booleanNode * nodePointer)
    : sExpression(nodePointer)
{

    }


////
//// public members
////


boolean::boolean(bool value)
    : sExpression(new booleanNode(value))
{

    }


////////
//////// class booleanNode
////////


////
//// private members
////


bool & booleanNode::valueReference()
{

    return myValue;

    }


bool const & booleanNode::valueConstReference() const
{

    return myValue;

    }


////
//// public members
////


booleanNode::booleanNode(bool value)
    : myValue(value)
{

    }


std::string booleanNode::toString() const
{

    return valueConstReference() ? "#t" : "#f";

    }


bool booleanNode::isBoolean() const
{

    return true;

    }


////////
//////// class pair
////////


////
//// private members
////


pair::pair(pairNode * nodePointer)
    : sExpression(nodePointer)
{

    }


////
//// public members
////


pair::pair(sExpression const & car, sExpression const & cdr)
    : sExpression(new pairNode(car, cdr))
{

    }


////////
//////// class pairNode
////////


////
//// private members
////


sExpression & pairNode::carReference()
{

    return myCar;

    }


sExpression const & pairNode::carConstReference() const
{

    return myCar;

    }


sExpression & pairNode::cdrReference()
{

    return myCdr;

    }


sExpression const & pairNode::cdrConstReference() const
{

    return myCdr;

    }


////
//// public members
////


pairNode::pairNode(sExpression const & car, sExpression const & cdr)
    : myCar(car),
      myCdr(cdr)
{

    }


std::string pairNode::toString() const
{

    return
        "(" + carConstReference().toString() + " . " + cdrConstReference().toString() + ")";

    }


bool pairNode::isPair() const
{

    return true;

    }


sExpression pairNode::car() const
{

    return carConstReference();

    }


sExpression pairNode::cdr() const
{

    return cdrConstReference();

    }
