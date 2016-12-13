package hw3.fol;


import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author manal
 */
public class FOLParser
{
    private FOLLex folLexer;
    private Token currentToken;

    public Sentence parse(String sentenceStr)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(sentenceStr);
        builder.append(")");

        sentenceStr = builder.toString();

        folLexer = new FOLLex();
        folLexer.setInput(sentenceStr);

        //load first token
        consume();

        return parseSentence();
    }

    private void consume()
    {
        currentToken = folLexer.nextToken();
    }

    private Sentence parseSentence()
    {
        LogicType currentLogicType = currentToken.getTokenType();
        if (currentLogicType.equals(LogicType.LPARANTHESIS))
        {
            return parseParenthesizedSentence();
        }
        else if (currentLogicType.equals(LogicType.PREDICATE))
        {
            return parsePredicate();
        }
        else if (currentLogicType.equals(LogicType.CONNECTIVE) && 
                currentToken.getConnective().equals(Connective.NOT))
        {
            return parseNegatedSentence();
        }

        throw new RuntimeException("Unable to parse token at "
                + currentToken.getStartPositionInInput()
                + ". Token: " + currentToken.getTokenText());
    }

    private Sentence parseParenthesizedSentence()
    {
        ensureTokenText("(");
        //gets the first part of the sentence
        Sentence sent = parseSentence();

        //read the rest of the terms in the sentence
        while (currentToken.getTokenType().equals(LogicType.CONNECTIVE)
                && currentToken.getConnective().equals(Connective.NOT) == Boolean.FALSE)
        {
            Connective connective = currentToken.getConnective();
            consume();
            Sentence otherSent = parseSentence();
            sent = new CompoundSentence(sent, otherSent, connective);
        }

        ensureTokenText(")");
        return sent;
    }

    private Sentence parsePredicate()
    {
        String predicateName = currentToken.getTokenText();

        consume();
        List<Term> termList = new ArrayList<Term>();
        ensureTokenText("(");

        //parseTerm() consumes a token
        termList.add(parseTerm());

        while (currentToken.getTokenType().equals(LogicType.COMMA))
        {
            consume();
            termList.add(parseTerm());
        }

        ensureTokenText(")");

        return new Predicate(predicateName, termList);
    }

    private Term parseTerm()
    {
        LogicType tokenType = currentToken.getTokenType();
        if (tokenType.equals(LogicType.CONSTANT))
        {
            Term term = new ConstantTerm(currentToken.getTokenText());
            consume();
            return term;
        }
        else if (tokenType.equals(LogicType.VARIABLE))
        {
            Term term = new VariableTerm(currentToken.getTokenText());
            consume();
            return term;
        }
        else
        {
            throw new IllegalStateException(
                    "Token isnot a constant or a variable at "
                    + currentToken.getStartPositionInInput() + ". Token: "
                    + currentToken.getTokenText());
        }
    }

    private Sentence parseNegatedSentence()
    {
        ensureTokenText("~");

        return new NegatedSentence(parseSentence());
    }

    private Boolean ensureTokenText(String text)
    {
        if (currentToken.getTokenText().equals(text) == Boolean.FALSE)
        {
            throw new InternalError("Token does not match with intended input. "
                    + "Expected: " + text + " at " + currentToken.getStartPositionInInput()
                    + ", but read: " + currentToken.getTokenText());
        }

        //after ensuring the current token, moves current token to next token
        consume();

        return Boolean.TRUE;
    }

}
