package hw3.fol;

import hw3.Lex;
import java.util.HashSet;
import java.util.Set;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author manal
 */
public class FOLLex extends Lex
{
    private Set<String> connectorsSet;

    public FOLLex()
    {
        connectorsSet = new HashSet<>();

        //connectorsSet.add("NOT");
        //connectorsSet.add("OR");
        //connectorsSet.add("ADD");
        //connectorsSet.add("IMPLIES");
        connectorsSet.add("~");
        connectorsSet.add("|");
        connectorsSet.add("&");
        connectorsSet.add("=>");
    }

    @Override
    public Token nextToken()
    {
        int currentCharPosition = getCurrentPosition();
        char currentChar = getChar();

        if (currentChar == '(')
        {
            increment();
            return new Token(LogicType.LPARANTHESIS, "(", currentCharPosition);
        }
        else if (currentChar == ')')
        {
            increment();
            return new Token(LogicType.RPARANTHESIS, ")", currentCharPosition);
        }
        else if (currentChar == ',')
        {
            increment();
            return new Token(LogicType.COMMA, ",", currentCharPosition);
        }
        else if (isIdentifier(currentChar))
        {
            return getIdentifier();
        }
        else if (Character.isWhitespace(currentChar))
        {
            increment();
            return nextToken();
        }
        else if (currentChar == (char) -1)
        {
            return new Token(LogicType.EOI, null, -1);
        }
        else
        {
            throw new IllegalStateException("FOLLex - Unkown token discovered. "
                    + "Token: " + currentChar + ". Sentence: " + getInputStr());
        }
    }

    private Boolean isIdentifier(Character ch)
    {
        if (Character.isJavaIdentifierStart(ch) 
                || ch == '=' || ch == '>' 
                || ch == '~' || ch == '&'
                || ch == '|')
        {
            return Boolean.TRUE;
        }
        else
        {
            return Boolean.FALSE;
        }
    }

    private Token getIdentifier()
    {
        int startPosition = getCurrentPosition();
        StringBuilder strBuilder = new StringBuilder();

        if (getChar() == '~')
        {
            strBuilder.append(getChar());
            increment();
        }
        else
        {
            while (Character.isJavaIdentifierPart(getChar())
                    || getChar() == '=' || getChar() == '>'
                    || getChar() == '&' || getChar() == '|')
            {
                strBuilder.append(getChar());
                increment();
            }
        }

        String str = strBuilder.toString();
        //System.out.println("getIdentifier: "+str);
        if (connectorsSet.contains(str))
        {
            return new Token(LogicType.CONNECTIVE, str, startPosition);
        }
        else if (Character.isUpperCase(str.charAt(0)))
        {
            if (getChar() == '(')
            {
                return new Token(LogicType.PREDICATE, str, startPosition);
            }
            else
            {
                return new Token(LogicType.CONSTANT, str, startPosition);
            }
        }
        else if (Character.isLowerCase(str.charAt(0)))
        {
            return new Token(LogicType.VARIABLE, str, startPosition);
        }
        else
        {
            throw new RuntimeException("FOLLex - Error on character " + getChar() + " at position " + getCurrentPosition());
        }
    }
}
