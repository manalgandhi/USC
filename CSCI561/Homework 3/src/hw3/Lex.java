package hw3;


import hw3.fol.Token;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author manal
 */
public abstract class Lex
{
    private String inputStr;

    private int currentPositionInInput;

    public void setInput(String input)
    {
        this.inputStr = input;
        currentPositionInInput = 0;
    }

    public abstract Token nextToken();

    protected char getChar()
    {
        if (currentPositionInInput >= inputStr.length())
        {
            return (char) -1;
        }
        return inputStr.charAt(currentPositionInInput);
    }

    protected void increment()
    {
        currentPositionInInput++;
    }
    
    protected int getCurrentPosition()
    {
        return currentPositionInInput;
    }
    
    protected String getInputStr()
    {
        return inputStr;
    }
}
