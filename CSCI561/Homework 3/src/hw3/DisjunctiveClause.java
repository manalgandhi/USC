/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3;

import hw3.fol.Predicate;
import hw3.fol.Term;
import hw3.fol.VariableTerm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author manal
 */
public class DisjunctiveClause
{
    private Predicate[] predicateArr;
    private Predicate[] locallyStandardizedPredicateArr;

    public DisjunctiveClause(Predicate[] predicateArr)
    {
        Arrays.sort(predicateArr);
        this.predicateArr = predicateArr;

        standardizePredicates();
    }

    public Predicate[] getPredicates()
    {
        return predicateArr;
    }

    public DisjunctiveClause getCopy()
    {
        List<Predicate> predCopyList = new ArrayList<>();

        for (Predicate pred : predicateArr)
        {
            predCopyList.add(pred.getCopy());
        }

        DisjunctiveClause disClaue = new DisjunctiveClause(
                predCopyList.toArray(new Predicate[predCopyList.size()]));

        return disClaue;
    }

    public Boolean isUnitClause()
    {
        return predicateArr.length == 1 ? Boolean.TRUE : Boolean.FALSE;
    }

    public Boolean isEmpty()
    {
        return predicateArr.length <= 0;
    }

    public Predicate[] getStandardizedPredicates()
    {
        return locallyStandardizedPredicateArr;
    }

    private void standardizePredicates()
    {
        ArrayList<Predicate> standardizedPredList = new ArrayList<>();

        int idCounter = 0;
        HashMap<String, String> standardizedTermMap = new HashMap<>();
        for (Predicate pred : predicateArr)
        {
            List<Term> newTermList = new ArrayList<Term>();
            for (Term term : pred.getTerms())
            {
                if (term instanceof VariableTerm)
                {
                    if (standardizedTermMap.containsKey(term.getTerm()) == Boolean.FALSE)
                    {
                        standardizedTermMap.put(term.getTerm(), "sd" + idCounter);
                        idCounter++;
                    }
                    Term newTerm = new VariableTerm(standardizedTermMap.get(term.getTerm()));

                    newTermList.add(newTerm);
                }
                else
                {
                    newTermList.add(term);
                }
            }

            Predicate standardizedPred = new Predicate(pred.getPredicateName(), newTermList);
            if (pred.isNegated())
            {
                standardizedPred.setNegation();
            }

            standardizedPredList.add(standardizedPred);
        }

        locallyStandardizedPredicateArr = standardizedPredList.toArray(new Predicate[standardizedPredList.size()]);
    }

    @Override
    public String toString()
    {
        return Arrays.toString(getPredicates());
    }

    @Override
    public boolean equals(Object disClause)
    {
        if (disClause instanceof DisjunctiveClause)
        {
            Predicate[] firstPredArr = getStandardizedPredicates();
            Predicate[] secondPredArr = ((DisjunctiveClause) disClause).getStandardizedPredicates();

            if (firstPredArr.length == secondPredArr.length)
            {
                for (int i = 0; i < firstPredArr.length; i++)
                {
                    if (firstPredArr[i].equals(secondPredArr[i]) == Boolean.FALSE)
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 67 * hash + Arrays.deepHashCode(this.locallyStandardizedPredicateArr);
        return hash;
    }
}
