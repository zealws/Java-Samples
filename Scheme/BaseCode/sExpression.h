#ifndef _SEXPRESSION_H_
#define _SEXPRESSION_H_


#include <string>


class sExpression;
class sExpressionNode;

class null;
class nullNode;

class symbol;
class symbolNode;

class boolean;
class booleanNode;

class pair;
class pairNode;


class sExpression
{

    private:

        sExpressionNode * myNodePointer;

        sExpressionNode * & nodePointerReference();
        sExpressionNode * const & nodePointerConstReference() const;

    protected:

        sExpression(sExpressionNode * nodePointer);

    public:

        sExpression();

        // compiler-generated copy ctor is okay
        // compiler-generated operator = is okay

        virtual ~sExpression();

        virtual std::string toString() const;

        virtual bool isNull() const;

        virtual bool isSymbol() const;

        virtual bool isBoolean() const;

        virtual bool isPair() const;
        virtual sExpression car() const;
        virtual sExpression cdr() const;

        virtual bool isEq(sExpression other) const;

    };


class sExpressionNode
{

    private:

    public:

        // compiler-generated parameterless ctor is okay
        // compiler-generated copy ctor is okay
        // compiler-generated operator = is okay

        virtual ~sExpressionNode();

        virtual std::string toString() const = 0;

        virtual bool isNull() const;

        virtual bool isSymbol() const;

        virtual bool isBoolean() const;

        virtual bool isPair() const;
        virtual sExpression car() const;
        virtual sExpression cdr() const;

    };


class null : public sExpression
{

    private:

        null(nullNode * nodePointer);

    public:

        null();

        // compiler-generated copy ctor is okay
        // compiler-generated operator = is okay
        // compiler-generated virtual dtor is okay

    };


class nullNode : public sExpressionNode
{

    private:

    public:

        nullNode();

        // compiler-generated copy ctor is okay
        // compiler-generated operator = is okay
        // compiler-generated virtual dtor is okay

        virtual std::string toString() const;

        virtual bool isNull() const;

    };


class symbol : public sExpression
{

    private:

        symbol();
            // leave unimplemented

        symbol(symbolNode * nodePointer);

    public:

        // compiler-generated copy ctor is okay
        // compiler-generated operator = is okay
        // compiler-generated virtual dtor is okay

        symbol(std::string const & name);

    };


class symbolNode : public sExpressionNode
{

    private:

        std::string myName;

        symbolNode();
            // leave unimplemented

        virtual std::string & nameReference();
        virtual std::string const & nameConstReference() const;

    public:

        // compiler-generated copy ctor is okay
        // compiler-generated operator = is okay
        // compiler-generated virtual dtor is okay

        symbolNode(std::string const & name);

        virtual std::string toString() const;

        virtual bool isSymbol() const;

    };


class boolean : public sExpression
{

    private:

        boolean();
            // leave unimplemented

        boolean(booleanNode * nodePointer);

    public:

        // compiler-generated copy ctor is okay
        // compiler-generated operator = is okay
        // compiler-generated virtual dtor is okay

        boolean(bool value);

    };


class booleanNode : public sExpressionNode
{

    private:

        bool myValue;

        booleanNode();
            // leave unimplemented

        virtual bool & valueReference();
        virtual bool const & valueConstReference() const;

    public:

        // compiler-generated copy ctor is okay
        // compiler-generated operator = is okay
        // compiler-generated virtual dtor is okay

        booleanNode(bool value);

        virtual std::string toString() const;

        virtual bool isBoolean() const;

    };


class pair : public sExpression
{

    private:

        pair();
            // leave unimplemented

        pair(pairNode * nodePointer);

    public:

        // compiler-generated copy ctor is okay
        // compiler-generated operator = is okay
        // compiler-generated virtual dtor is okay

        pair(sExpression const & car, sExpression const & cdr);

    };


class pairNode : public sExpressionNode
{

    private:

        sExpression myCar;
        sExpression myCdr;

        pairNode();
            // leave unimplemented

        virtual sExpression & carReference();
        virtual sExpression const & carConstReference() const;

        virtual sExpression & cdrReference();
        virtual sExpression const & cdrConstReference() const;

    public:

        // compiler-generated copy ctor is okay
        // compiler-generated operator = is okay
        // compiler-generated virtual dtor is okay

        pairNode(sExpression const & car, sExpression const & cdr);

        virtual std::string toString() const;

        virtual bool isPair() const;
        virtual sExpression car() const;
        virtual sExpression cdr() const;

    };


#endif
