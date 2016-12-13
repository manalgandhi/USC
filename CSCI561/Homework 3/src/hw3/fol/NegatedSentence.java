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
public class NegatedSentence implements Sentence
{
    private Sentence negatedSentence;

    public NegatedSentence(Sentence negatedSentence)
    {
        this.negatedSentence = negatedSentence;
    }

    public Sentence getNegatedSentence()
    {
        return negatedSentence;
    }

    @Override
    public Boolean isCompound()
    {
        return Boolean.FALSE;
    }

    @Override
    public String toString()
    {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("~");
        
        strBuilder.append("(");
        strBuilder.append(negatedSentence.toString());
        strBuilder.append(")");

        return strBuilder.toString();
    }

}
