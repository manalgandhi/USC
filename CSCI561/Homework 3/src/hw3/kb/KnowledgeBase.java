/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3.kb;

import hw3.Counter;
import hw3.DisjunctiveClause;
import hw3.Pair;
import hw3.fol.Predicate;
import hw3.resolution.Unification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 *
 * @author manal
 */
public class KnowledgeBase
{
    private Counter kbClauseCounter;

    private HashSet<String> unificationKeysInKB;
    private HashMap<Integer, Pair<String, String>> clauseIDVsUnificationKeysMap;
    private HashMap<Integer, DisjunctiveClause> clauseIDVsDisjunctiveClauseMap;
    private HashSet<DisjunctiveClause> disjunctiveClaueSet;
    private HashMap<String, List<Pair<Integer, Predicate>>> posPredNameVSClauseIDPredPairMap;
    private HashMap<String, List<Pair<Integer, Predicate>>> negPredNameVSClauseIDPredPairMap;

    public KnowledgeBase()
    {
        kbClauseCounter = new Counter();

        unificationKeysInKB = new HashSet<>();
        clauseIDVsUnificationKeysMap = new HashMap<>();
        clauseIDVsDisjunctiveClauseMap = new HashMap<>();
        disjunctiveClaueSet = new HashSet<>();
        posPredNameVSClauseIDPredPairMap = new HashMap<>();
        negPredNameVSClauseIDPredPairMap = new HashMap<>();
    }

    public int addToKB(Unification unificationObj)
    {
        Integer unificationClauseID = addToKB(unificationObj.getUnifiedClause());

        if (unificationClauseID == -1)
        {
            return unificationClauseID;
        }

        Pair<String, String> unificationKeyPair = unificationObj.getKey();
        unificationKeysInKB.add(unificationKeyPair.getFirst());
        unificationKeysInKB.add(unificationKeyPair.getSecond());

        //System.out.println("Keys added to KB. Keys: "+unificationKeysInKB.toString());
        clauseIDVsUnificationKeysMap.put(unificationClauseID, unificationKeyPair);
        return unificationClauseID;
    }

    //the predicates form a disjunctive sentence
    public int addToKB(DisjunctiveClause disjuntiveClause)
    {
        if (disjunctiveClaueSet.contains(disjuntiveClause))
        {
            return -1;
        }

        Integer clauseID = kbClauseCounter.getValue();
        kbClauseCounter.increment();

        DisjunctiveClause duplicatePredRemoved = removeDuplicatePredicates(disjuntiveClause);
        clauseIDVsDisjunctiveClauseMap.put(clauseID, duplicatePredRemoved);

        for (Predicate predicate : disjuntiveClause.getPredicates())
        {
            if (predicate.isNegated())
            {
                addPredToMap(predicate, clauseID, negPredNameVSClauseIDPredPairMap);
            }
            else
            {
                addPredToMap(predicate, clauseID, posPredNameVSClauseIDPredPairMap);
            }
        }

        disjunctiveClaueSet.add(disjuntiveClause);

        //System.out.println("Adding dis clause: " + disjuntiveClause.toString()+". ID: "+clauseID);
        //System.out.println("Pos and neg maps:\n " + posPredNameVSClauseIDPredPairMap.toString() + "\n" + negPredNameVSClauseIDPredPairMap.toString() + "\n");
        return clauseID;
    }

    public DisjunctiveClause getDisjunctiveClause(Integer clauseID)
    {
        return clauseIDVsDisjunctiveClauseMap.get(clauseID);
    }

    public Unification[] getPossibleUnificationClauses(Integer clauseIDToUnifyWith)
    {
        List<Unification> unificationList = new ArrayList<>();

        DisjunctiveClause claueToUnifyWith = getDisjunctiveClause(clauseIDToUnifyWith);
        Predicate[] predicatesToUnifyWith = claueToUnifyWith.getPredicates();

        for (Predicate predicate : predicatesToUnifyWith)
        {
            List<Pair<Integer, Predicate>> listOfClauses;
            if (predicate.isNegated())
            {
                listOfClauses = posPredNameVSClauseIDPredPairMap.get(predicate.getPredicateName());
            }
            else
            {
                listOfClauses = negPredNameVSClauseIDPredPairMap.get(predicate.getPredicateName());
            }

            if (listOfClauses == null)
            {
                continue;
            }

            for (Pair<Integer, Predicate> pair : listOfClauses)
            {
                String key = Unification.getClauseKey(
                        clauseIDToUnifyWith,
                        pair.getFirst(),
                        predicate,
                        pair.getSecond());

                if (unificationKeysInKB.contains(key))
                {
                    //System.out.println("||||||**********|||||||********||||||||");
                    continue;
                }

                DisjunctiveClause clause = getDisjunctiveClause(pair.getFirst());

                //System.out.println("PredName: " + predicateName);
                //System.out.println("Clausewithneg: " + clauseWithNegPred + " [" + negPredClauseID + "]");
                //System.out.println("Clausewithpos:" + clauseWithPosPred + " [" + posPredClauseID + "]");
                
                Unification unification = new Unification(
                        claueToUnifyWith,
                        clause,
                        clauseIDToUnifyWith,
                        pair.getFirst(),
                        predicate,
                        pair.getSecond());

                unificationList.add(unification);
            }
        }

        //sSystem.out.println(Arrays.toString(unificationList.toArray(new Unification[unificationList.size()])));
        Collections.sort(unificationList, UNIFICATION_CUSTOM_COMP.reversed());
        //System.out.println(Arrays.toString(unificationList.toArray(new Unification[unificationList.size()])));

        return unificationList.toArray(new Unification[unificationList.size()]);
    }

    public void rebuildKB()
    {
        posPredNameVSClauseIDPredPairMap = new HashMap<>();
        negPredNameVSClauseIDPredPairMap = new HashMap<>();
        disjunctiveClaueSet = new HashSet<>();
        unificationKeysInKB = new HashSet<>();
        clauseIDVsUnificationKeysMap = new HashMap<>();

        for (Entry<Integer, DisjunctiveClause> entry : clauseIDVsDisjunctiveClauseMap.entrySet())
        {
            for (Predicate predicate : entry.getValue().getPredicates())
            {
                if (predicate.isNegated())
                {
                    addPredToMap(predicate, entry.getKey(), negPredNameVSClauseIDPredPairMap);
                }
                else
                {
                    addPredToMap(predicate, entry.getKey(), posPredNameVSClauseIDPredPairMap);
                }
            }
        }
    }

    public void removeClause(Integer clauseID)
    {
        DisjunctiveClause disjunctiveClause = clauseIDVsDisjunctiveClauseMap.remove(clauseID);

        disjunctiveClaueSet.remove(disjunctiveClause);

        Predicate[] predicateArr = disjunctiveClause.getPredicates();

        for (Predicate predicate : predicateArr)
        {
            String predicateName = predicate.getPredicateName();
            if (posPredNameVSClauseIDPredPairMap.containsKey(predicateName))
            {
                //posPredNameVSClauseIDPredPairMap.get(predicateName).remove(new Pair<>(clauseID, predicate));
                removePairFromMap(posPredNameVSClauseIDPredPairMap, new Pair<>(clauseID, predicate));
            }
            if (negPredNameVSClauseIDPredPairMap.containsKey(predicateName))
            {
                //negPredNameVSClauseIDPredPairMap.get(predicateName).remove(new Pair<>(clauseID, predicate));
                removePairFromMap(negPredNameVSClauseIDPredPairMap, new Pair<>(clauseID, predicate));
            }
        }

        if (clauseIDVsUnificationKeysMap.containsKey(clauseID))
        {
            Pair<String, String> unificationKeys = clauseIDVsUnificationKeysMap.remove(clauseID);

            unificationKeysInKB.remove(unificationKeys.getFirst());
            unificationKeysInKB.remove(unificationKeys.getSecond());
        }

        //System.out.println("Removed clause: " + disjunctiveClause.toString() + ". ID: " + clauseID);
        //System.out.println("Pos and neg maps: " + posPredNameVSClauseIDPredPairMap.toString() + "\n" + negPredNameVSClauseIDPredPairMap.toString() + "\n");
    }

    public void removePairFromMap(HashMap<String, List<Pair<Integer, Predicate>>> map,
            Pair<Integer, Predicate> pair)
    {
        Predicate predicate = pair.getSecond();
        Integer clauseID = pair.getFirst();

        if (map.containsKey(predicate.getPredicateName()))
        {
            List<Pair<Integer, Predicate>> list = map.get(predicate.getPredicateName());
            for (int i = 0; i < list.size(); i++)
            {
                Pair<Integer, Predicate> pairFromList = list.get(i);
                if (Objects.equals(pairFromList.getFirst(), clauseID)
                        && pairFromList.getSecond().equals(predicate))
                {
                    list.remove(i);
                    i--;
                }
            }

            //map.put(predicate.getPredicateName(), list);
        }
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        for (Entry<Integer, DisjunctiveClause> clauseInKB : clauseIDVsDisjunctiveClauseMap.entrySet())
        {
            builder.append(clauseInKB.getKey()).append(": ");
            builder.append(clauseInKB.getValue().toString());
            builder.append(System.lineSeparator());
        }
        
        builder.append(System.lineSeparator());
        
        for(DisjunctiveClause clause:disjunctiveClaueSet)
        {
            builder.append(clause.toString());
            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }

    private void addPredToMap(Predicate predicate, Integer clauseID,
            Map<String, List<Pair<Integer, Predicate>>> map)
    {
        if (map.containsKey(predicate.getPredicateName()))
        {
            map.get(predicate.getPredicateName()).add(new Pair<>(clauseID, predicate));
        }
        else
        {
            List<Pair<Integer, Predicate>> clauseIDPredPairList = new ArrayList<>();
            clauseIDPredPairList.add(new Pair<>(clauseID, predicate));
            map.put(predicate.getPredicateName(), clauseIDPredPairList);
        }
    }

    private DisjunctiveClause removeDuplicatePredicates(DisjunctiveClause disjuntiveClause)
    {
        HashSet<Predicate> predSet = new HashSet<>();

        List<Predicate> newPredList = new ArrayList<>();
        for (Predicate pred : disjuntiveClause.getPredicates())
        {
            if (predSet.contains(pred) == Boolean.FALSE)
            {
                predSet.add(pred);
                newPredList.add(pred);
            }
        }

        DisjunctiveClause newClause = new DisjunctiveClause(
                newPredList.toArray(new Predicate[newPredList.size()]));

        return newClause;

    }

    private static final Comparator<Unification> UNIFICATION_CUSTOM_COMP = new Comparator<Unification>()
    {
        @Override
        public int compare(Unification clauseOne, Unification clauseTwo)
        {
            if (clauseOne.isEitherClauseUnit() && clauseTwo.isEitherClauseUnit()
                    || (!clauseOne.isEitherClauseUnit() && !clauseTwo.isEitherClauseUnit()))
            {
                return compareBasedOnNumOfConstantsInPredicates(clauseOne, clauseTwo);
            }
            else if (clauseOne.isEitherClauseUnit())
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }

        private int compareBasedOnNumOfConstantsInPredicates(Unification clauseOne, Unification clauseTwo)
        {
            Pair<Predicate, Predicate> predicateInOne = clauseOne.getPredicates();
            int numOfConstInPredOne = predicateInOne.getFirst().getNumOfConstants() + predicateInOne.getSecond().getNumOfConstants();

            Pair<Predicate, Predicate> predicateInTwo = clauseTwo.getPredicates();
            int numOfConstInPredTwo = predicateInTwo.getFirst().getNumOfConstants() + predicateInTwo.getSecond().getNumOfConstants();

            if (numOfConstInPredOne > 0 && numOfConstInPredTwo > 0)
            {
                if (numOfConstInPredOne == numOfConstInPredTwo)
                {
                    return 0;
                }
                else if (numOfConstInPredOne > numOfConstInPredTwo)
                {
                    return 1;
                }
                else
                {
                    return -1;
                }
            }
            else if (numOfConstInPredOne > 0)
            {
                return 1;
            }
            else if (numOfConstInPredTwo > 0)
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }
    };

}
