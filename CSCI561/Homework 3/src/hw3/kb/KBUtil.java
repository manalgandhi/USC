/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3.kb;

import hw3.Counter;
import hw3.fol.CompoundSentence;
import hw3.fol.Connective;
import hw3.fol.LogicType;
import hw3.fol.Predicate;
import hw3.fol.Sentence;
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
public class KBUtil
{
    public static Sentence[] separateClauses(Sentence inputSentence)
    {

        ArrayList<Sentence> separatedClausesList = new ArrayList<>();

        if (inputSentence instanceof CompoundSentence)
        {
            if (((CompoundSentence) inputSentence).getConnective().equals(Connective.AND))
            {
                Sentence[] leftSeparatedClauses = separateClauses(((CompoundSentence) inputSentence).getLeftSentence());
                Sentence[] rightSeparatedClauses = separateClauses(((CompoundSentence) inputSentence).getRightSentence());

                separatedClausesList.addAll(Arrays.asList(leftSeparatedClauses));
                separatedClausesList.addAll(Arrays.asList(rightSeparatedClauses));
            }
            else
            {
                separatedClausesList.add(inputSentence);
            }
        }
        else
        {
            separatedClausesList.add(inputSentence);
        }

        return separatedClausesList.toArray(new Sentence[separatedClausesList.size()]);
    }

    //returns standardized predicates that are part of the sentence
    public static Predicate[] standardizeSentence(Sentence inputSentence, Counter counter)
    {
        HashMap<String, String> standardizedVariableMap = new HashMap<>();

        ArrayList<Predicate> standardizedPredicateList = new ArrayList<>();

        Predicate[] predicatesInInputSent = getPredicatesInSentence(inputSentence);
        for (Predicate predicate : predicatesInInputSent)
        {
            Term[] predicateTermArr = predicate.getTerms();
            List<Term> standardizedTermList = new ArrayList<>();

            for (Term term : predicateTermArr)
            {
                Term standardizedTerm;
                if (term.getType().equals(LogicType.VARIABLE))
                {
                    String standardizedVariableName;
                    String variableName = term.getTerm();
                    if (standardizedVariableMap.containsKey(variableName))
                    {
                        standardizedVariableName = standardizedVariableMap.get(variableName);
                    }
                    else
                    {
                        standardizedVariableName = "id" + counter.getValue();
                        counter.increment();

                        standardizedVariableMap.put(variableName, standardizedVariableName);
                    }

                    standardizedTerm = new VariableTerm(standardizedVariableName);
                }
                else
                {
                    standardizedTerm = term;
                }

                standardizedTermList.add(standardizedTerm);
            }

            Predicate standardizedPred = new Predicate(predicate.getPredicateName(), standardizedTermList);

            if (predicate.isNegated())
            {
                standardizedPred.setNegation();
            }

            standardizedPredicateList.add(standardizedPred);
        }

        return standardizedPredicateList.toArray(new Predicate[standardizedPredicateList.size()]);
    }

    private static Predicate[] getPredicatesInSentence(Sentence inputSentence)
    {
        //objects of type negation or predicate
        ArrayList<Predicate> predicateList = new ArrayList<>();
        if (inputSentence instanceof CompoundSentence)
        {
            Predicate[] predicatesLeftSent = getPredicatesInSentence(((CompoundSentence) inputSentence).getLeftSentence());
            Predicate[] predicatesRightSent = getPredicatesInSentence(((CompoundSentence) inputSentence).getRightSentence());

            predicateList.addAll(Arrays.asList(predicatesLeftSent));
            predicateList.addAll(Arrays.asList(predicatesRightSent));
        }
        else
        {
            predicateList.add((Predicate) inputSentence);
        }

        return predicateList.toArray(new Predicate[predicateList.size()]);
    }
}
