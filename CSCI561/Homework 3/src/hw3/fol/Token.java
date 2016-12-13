package hw3.fol;

import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author manal
 */
public class Token
{
    private LogicType logicType;
    private String tokenTxt;
    private int startPosInInput;
    private Connective connective;

    public Token(LogicType type, String text, int startPosInInput)
    {
        this.logicType = type;
        this.tokenTxt = text;
        this.startPosInInput = startPosInInput;
        
        connective = Connective.connectiveFor(text);
    }

    public String getTokenText()
    {
        return tokenTxt;
    }

    public LogicType getTokenType()
    {
        return logicType;
    }

    public int getStartPositionInInput()
    {
        return startPosInInput;
    }
    
    public Connective getConnective()
    {
        return connective;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj.getClass().equals(this.getClass()) == Boolean.FALSE)
        {
            return Boolean.FALSE;
        }

        Token tokenObj = (Token) obj;

        if (this.logicType.equals(tokenObj.getTokenType())
                && this.tokenTxt.equals(tokenObj.getTokenText())
                && this.startPosInInput == tokenObj.getStartPositionInInput())
        {
            return Boolean.TRUE;
        }
        else
        {
            return Boolean.FALSE;
        }
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.logicType);
        hash = 17 * hash + Objects.hashCode(this.tokenTxt);
        hash = 17 * hash + this.startPosInInput;
        hash = 17 * hash + Objects.hashCode(this.connective);
        return hash;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(logicType).append(" ");
        builder.append(tokenTxt).append(" ");
        builder.append(startPosInInput).append(" ");
        builder.append("]");

        return builder.toString();
    }
}
