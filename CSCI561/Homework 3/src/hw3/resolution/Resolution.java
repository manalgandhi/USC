/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3.resolution;

import hw3.DisjunctiveClause;
import hw3.fol.Predicate;
import hw3.homework;
import hw3.kb.KnowledgeBase;

/**
 *
 * @author manal
 */
public class Resolution
{
    private int resolutionCounter;
    private KnowledgeBase knowledgeBase;

    public Resolution(KnowledgeBase knowledgeBase)
    {
        this.knowledgeBase = knowledgeBase;
    }

    public Boolean performResolution(Predicate predcateToProve)
    {
        resolutionCounter = 0;
        //System.out.println(knowledgeBase.toString());
        //1. negate predicate and then add it to the KB
        if (predcateToProve.isNegated())
        {
            predcateToProve.clearNegation();
        }
        else
        {
            predcateToProve.setNegation();
        }

        //2. add predicate to KB
        DisjunctiveClause disClause = new DisjunctiveClause(new Predicate[]
        {
            predcateToProve,
        });
        Integer clauseID = knowledgeBase.addToKB(disClause);

        //3. infer contradiction
        Boolean isContradictionInfered = inferContradiction(clauseID, 0);

        //4. rebuild internal information for the KB
        knowledgeBase.removeClause(clauseID);
        knowledgeBase.rebuildKB();

        //System.out.println(knowledgeBase.toString());
        return isContradictionInfered;
    }

    private Boolean inferContradiction(int clauseIDToUse, int depth)
    {
        //System.out.println("***Entering***\n");
        try
        {

            if (depth > 2000 || resolutionCounter > 20000)
            {
                return Boolean.FALSE;
            }
            //System.out.println(depth);
            Boolean valid = Boolean.FALSE;
            Unification[] unificationArr = knowledgeBase.getPossibleUnificationClauses(clauseIDToUse);

            for (Unification unification : unificationArr)
            {
                DisjunctiveClause unifiedClause = unification.unify();

                if (unifiedClause == null)
                {
                    continue;
                }

                if (unifiedClause.isEmpty())
                {
                    if (homework.DEBUG)
                    {
                        System.out.println(unification.toString());
                        System.out.println("***Leaving***\n");
                    }
                    return Boolean.TRUE;
                }

                Integer clauseID = knowledgeBase.addToKB(unification);

                if (clauseID == -1)
                {
                    //System.out.println(unification.toString());
                    //return Boolean.FALSE;
                    continue;
                }

                resolutionCounter++;

                if (homework.DEBUG)
                {
                    System.out.println(unification.toString());
                    System.out.println("Unified clauseid: " + clauseID);

                    //System.out.println("KB:\n" + knowledgeBase.toString());
                    System.out.println();
                }

                Boolean isContradictionInfered = inferContradiction(clauseID, depth + 1);

                valid = Boolean.TRUE;

                //remove unified clause from KB
                knowledgeBase.removeClause(clauseID);

                if (isContradictionInfered)
                {
                    //System.out.println(unification.toString());
                    //System.out.println("Unified clauseid: " + clauseID);
                    //System.out.println();
                    //System.out.println("***Leaving***\n");

                    return Boolean.TRUE;
                }
            }

            if (!valid)
            {
                if (homework.DEBUG)
                {
                    System.out.println("Search terminated at depth : " + depth + "\n");
                }
            }

            //System.out.println("***Leaving***\n");
            return Boolean.FALSE;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println();
            //System.out.println("KB: "+knowledgeBase);
            //System.out.println("***Leaving***\n");

            return Boolean.FALSE;
        }
    }

}
