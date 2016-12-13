/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3.resolution;

import hw3.DisjunctiveClause;
import hw3.Pair;
import hw3.fol.ConstantTerm;
import hw3.fol.LogicType;
import hw3.fol.Predicate;
import hw3.fol.Term;
import hw3.fol.VariableTerm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;

/**
 *
 * @author manal
 */
public class Unification
{
    private DisjunctiveClause clauseOne;
    private DisjunctiveClause clauseTwo;

    private Integer clauseIDOne;
    private Integer clauseIDTwo;

    private Predicate predicateOne;
    private Predicate predicateTwo;

    private DisjunctiveClause unifiedClause;

    public Unification(DisjunctiveClause clauseOne,
            DisjunctiveClause clauseTwo,
            Integer clauseIDOne,
            Integer clauseIDTwo,
            Predicate predOne,
            Predicate predTwo)
    {
        this.clauseIDOne = clauseIDOne;
        this.clauseIDTwo = clauseIDTwo;

        this.clauseOne = clauseOne;
        this.clauseTwo = clauseTwo;

        this.predicateOne = predOne;
        this.predicateTwo = predTwo;
    }

    public Boolean isEitherClauseUnit()
    {
        if (clauseOne.isUnitClause() || clauseTwo.isUnitClause())
        {
            return Boolean.TRUE;
        }
        else
        {
            return Boolean.FALSE;
        }
    }

    public DisjunctiveClause unify()
    {
        HashMap<String, String> substitutionMap = getSubstitutionForUnification(
                predicateOne, predicateTwo);

        if (substitutionMap == null)
        {
            return null;
        }

        ArrayList<Predicate> unifiedPredicateList = new ArrayList<>();
        for (Predicate predicate : clauseOne.getPredicates())
        {
            if (predicate.equals(predicateOne))
            {
                continue;
            }

            ArrayList<Term> newTermList = new ArrayList<>();
            for (Term term : predicate.getTerms())
            {
                if (substitutionMap.containsKey(term.getTerm()))
                {
                    String substitution = substitutionMap.get(term.getTerm());
                    if (Character.isUpperCase(substitution.charAt(0)))
                    {
                        Term newTerm = new ConstantTerm(substitution);
                        newTermList.add(newTerm);
                    }
                    else
                    {
                        Term newTerm = new VariableTerm(substitution);
                        newTermList.add(newTerm);
                    }
                }
                else
                {
                    newTermList.add(term);
                }
            }

            Predicate newPred = new Predicate(predicate.getPredicateName(), newTermList);

            if (predicate.isNegated())
            {
                newPred.setNegation();
            }
            unifiedPredicateList.add(newPred);
        }

        for (Predicate predicate : clauseTwo.getPredicates())
        {
            if (predicate.equals(predicateTwo) == Boolean.TRUE)
            {
                continue;
            }

            ArrayList<Term> newTermList = new ArrayList<>();
            for (Term term : predicate.getTerms())
            {
                if (substitutionMap.containsKey(term.getTerm()))
                {
                    String substitution = substitutionMap.get(term.getTerm());
                    if (Character.isUpperCase(substitution.charAt(0)))
                    {
                        Term newTerm = new ConstantTerm(substitution);
                        newTermList.add(newTerm);
                    }
                    else
                    {
                        Term newTerm = new VariableTerm(substitution);
                        newTermList.add(newTerm);
                    }
                }
                else
                {
                    newTermList.add(term);
                }
            }

            Predicate newPred = new Predicate(predicate.getPredicateName(), newTermList);
            if (predicate.isNegated())
            {
                newPred.setNegation();
            }

            unifiedPredicateList.add(newPred);
        }
        
        unifiedClause = new DisjunctiveClause(
                removeTautologies(unifiedPredicateList));

        return unifiedClause;
    }

    public DisjunctiveClause getUnifiedClause()
    {
        return unifiedClause;
    }

    public Pair<String, String> getKey()
    {
        return new Pair<>(
                getClauseKey(clauseIDOne, clauseIDTwo, predicateOne, predicateTwo),
                getClauseKey(clauseIDTwo, clauseIDOne, predicateTwo, predicateOne));
    }

    public Pair<Predicate, Predicate> getPredicates()
    {
        return new Pair<>(predicateOne, predicateTwo);
    }

    public static String getClauseKey(Integer clauseIDOne, Integer clauseIDTwo, Predicate predOne, Predicate predTwo)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(clauseIDOne).append("|");
        builder.append(clauseIDTwo).append("|");

        if (predOne.isNegated())
        {
            builder.append("~");
        }
        builder.append(predOne.getPredicateName());
        builder.append("|");

        if (predTwo.isNegated())
        {
            builder.append("~");
        }
        builder.append(predTwo.getPredicateName());

        return builder.toString();
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Clauses: [(" + clauseIDOne + ")" + clauseOne.toString()
                + "] [(" + clauseIDTwo + ")" + clauseTwo.toString() + "]");
        builder.append(System.lineSeparator());
        builder.append("Predicates: [" + predicateOne.toString() + "] [" + predicateTwo + "]");
        builder.append(System.lineSeparator());
        builder.append("UnifiedClause: " + unifiedClause.toString());

        return builder.toString();
    }

    private HashMap<String, String> getSubstitutionForUnification(Predicate firstPred, Predicate secondPred)
    {
        HashMap<String, String> substitutionMap = new HashMap<>();

        Term[] firstPredTermArr = firstPred.getTerms();
        Term[] secondPredTermArr = secondPred.getTerms();

        for (int i = 0; i < firstPredTermArr.length; i++)
        {
            Term firstPredTerm = firstPredTermArr[i];
            Term secondPredTerm = secondPredTermArr[i];

            if (substitutionMap.containsKey(firstPredTerm.getTerm())
                    && substitutionMap.containsKey(secondPredTerm.getTerm()))
            {
                return null;
            }

            if (firstPredTerm.getType().equals(LogicType.CONSTANT)
                    && secondPredTerm.getType().equals(LogicType.CONSTANT))
            {
                if (firstPredTermArr[i].equals(secondPredTermArr[i]))
                {
                    continue;
                }
                else
                {
                    return null;
                }
            }

            if (substitutionMap.containsKey(firstPredTerm.getTerm()))
            {
                if (substitutionMap.get(firstPredTerm.getTerm()).equals(secondPredTerm.getTerm()))
                {
                    continue;
                }
                else
                {
                    return null;
                }
            }

            if (substitutionMap.containsKey(secondPredTerm.getTerm()))
            {
                if (substitutionMap.get(secondPredTerm.getTerm()).equals(firstPredTerm.getTerm()))
                {
                    continue;
                }
                else
                {
                    return null;
                }
            }

            /*
            if (firstPredTermArr[i].equals(secondPredTermArr[i]))
            {
                continue;
            }
             */
            if (firstPredTermArr[i].getType().equals(LogicType.VARIABLE))
            {
                substitutionMap.put(firstPredTerm.getTerm(), secondPredTerm.getTerm());
            }
            else
            {
                substitutionMap.put(secondPredTerm.getTerm(), firstPredTerm.getTerm());
            }
        }

        for (Entry<String, String> entry : substitutionMap.entrySet())
        {
            HashSet<String> valuesEval = new HashSet<>();
            String str = entry.getKey();

            valuesEval.add(str);

            while (substitutionMap.containsKey(str))
            {
                if (valuesEval.contains(substitutionMap.get(str)))
                {
                    break;
                }

                str = substitutionMap.get(str);

                valuesEval.add(str);
            }

            substitutionMap.replace(entry.getKey(), str);
        }

        return substitutionMap;
    }

    private Predicate[] removeTautologies(List<Predicate> predicateList)
    {
        TreeSet<Integer> predToRemove = new TreeSet<>(Collections.reverseOrder());

        for (int i = 0; i < predicateList.size() - 1; i++)
        {
            for (int j = i + 1; j < predicateList.size(); j++)
            {
                Predicate predOne = predicateList.get(i);
                Predicate predTwo = predicateList.get(j);

                if (predOne.equals(predTwo))
                {
                    predToRemove.add(j);
                }
                if (predOne.isNegated())
                {
                    if (predTwo.isNegated() == Boolean.FALSE)
                    {
                        predOne.clearNegation();
                        if (predOne.equals(predTwo))
                        {
                            predToRemove.add(i);
                            predToRemove.add(j);
                        }
                        predOne.setNegation();
                    }
                }

                if (predTwo.isNegated())
                {
                    if (predOne.isNegated() == Boolean.FALSE)
                    {
                        predTwo.clearNegation();
                        if (predOne.equals(predTwo))
                        {
                            predToRemove.add(i);
                            predToRemove.add(j);
                        }
                        predTwo.setNegation();
                    }
                }
            }
        }


        Iterator<Integer> it = predToRemove.iterator();
        while(it.hasNext())
        {
            predicateList.remove(it.next().intValue());
        }

        return predicateList.toArray(new Predicate[predicateList.size()]);
    }
}
