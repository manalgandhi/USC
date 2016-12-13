package hw3.fol;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author manal
 */
public class CompoundSentence implements Sentence
{
    private Sentence leftSentence, rightSentence;
    private Connective connective;

    public CompoundSentence(Sentence left, Sentence right, Connective connective)
    {
        this.leftSentence = left;
        this.rightSentence = right;
        this.connective = connective;
    }

    public Sentence getLeftSentence()
    {
        return leftSentence;
    }

    public Sentence getRightSentence()
    {
        return rightSentence;
    }

    public Connective getConnective()
    {
        return connective;
    }

    @Override
    public Boolean isCompound()
    {
        return Boolean.TRUE;
    }

    @Override
    public String toString()
    {
        return "(" + leftSentence.toString() + " " + connective.toString() + " " + rightSentence.toString() + ")";
    }

}
