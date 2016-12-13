package hw3.fol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class Predicate implements Sentence, Comparable<Predicate>
{
    private String predicateName;
    private List<Term> termList;

    private Boolean isNegated;

    private int numOfConstants = 0;

    public Predicate(String predicateName, List<Term> termList)
    {
        this.predicateName = predicateName;
        this.termList = termList;
        isNegated = Boolean.FALSE;

        for (Term term : termList)
        {
            if (term.getType().equals(LogicType.CONSTANT))
            {
                numOfConstants++;
            }
        }
    }

    public Predicate(String predicateName, Term[] termArr)
    {
        this(predicateName, Arrays.asList(termArr));
    }

    public void setNegation()
    {
        isNegated = Boolean.TRUE;
    }

    public void clearNegation()
    {
        isNegated = Boolean.FALSE;
    }

    public Boolean isNegated()
    {
        return isNegated;
    }

    public Integer getNumOfConstants()
    {
        return numOfConstants;
    }

    public String getPredicateName()
    {
        return predicateName;
    }

    public Term[] getTerms()
    {
        return termList.toArray(new Term[termList.size()]);
    }

    @Override
    public Boolean isCompound()
    {
        return Boolean.FALSE;
    }

    public Predicate getCopy()
    {
        List<Term> newTermList = new ArrayList<>(termList);
        Predicate newPred = new Predicate(predicateName, newTermList);

        return newPred;
    }

    @Override
    public boolean equals(Object predicate)
    {
        if (predicate instanceof Predicate)
        {
            if (this.predicateName.equals(((Predicate) predicate).getPredicateName())
                    && isNegated().equals(((Predicate) predicate).isNegated())
                    && this.numOfConstants == ((Predicate) predicate).getNumOfConstants())
            {
                Term[] firstTermList = getTerms();
                Term[] secondTermList = ((Predicate) predicate).getTerms();
                if (firstTermList.length == secondTermList.length)
                {
                    for (int i = 0; i < firstTermList.length; i++)
                    {
                        if (firstTermList[i].equals(secondTermList[i]) == Boolean.FALSE)
                        {
                            return false;
                        }
                    }

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.predicateName);
        hash = 41 * hash + Objects.hashCode(this.termList);
        hash = 41 * hash + Objects.hashCode(this.isNegated);
        return hash;
    }

    @Override
    public String toString()
    {
        StringBuilder strBuilder = new StringBuilder();

        if (isNegated)
        {
            strBuilder.append("~");
        }

        strBuilder.append(predicateName).append("(");
        for (int i = 0; i < termList.size() - 1; i++)
        {
            strBuilder.append(termList.get(i).toString());
            strBuilder.append(",");
        }

        strBuilder.append(termList.get(termList.size() - 1));
        strBuilder.append(")");

        return strBuilder.toString();
    }

    @Override
    public int compareTo(Predicate o)
    {
        return getPredicateName().compareTo(o.getPredicateName());
    }
}
