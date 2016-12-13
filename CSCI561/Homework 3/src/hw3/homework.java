package hw3;

import hw3.fol.CNFConverter;
import hw3.fol.Sentence;
import hw3.fol.FOLParser;
import hw3.fol.NegatedSentence;
import hw3.fol.Predicate;
import hw3.kb.KBUtil;
import hw3.kb.KnowledgeBase;
import hw3.resolution.Resolution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author admin
 */
public class homework
{

    public static Boolean DEBUG = Boolean.FALSE;
    private KnowledgeBase knowledgeBase;

    private Counter standardizationIDCounter;

    public homework()
    {
        knowledgeBase = new KnowledgeBase();
        standardizationIDCounter = new Counter();
    }

    public void run(String inputFile, String outputFile) throws IOException
    {
        Input input = Input.readInput(inputFile);

        Sentence[] cnfKBSentenceArr = convertSentencesToCNF(input.getKBSent());

        addCNFSentencesToKB(cnfKBSentenceArr);

        if (DEBUG)
        {
            System.out.println("Knowledge Base: " + knowledgeBase.toString());
            System.out.println("****************************************");
        }

        Sentence[] querySentenceArr = convertSentencesToCNF(input.getQueries());

        Output output = resolveQueries(querySentenceArr);
        output.writeResultsToFile(outputFile);
    }

    private void run() throws IOException
    {
        Input input = Input.readInput();

        Sentence[] cnfKBSentenceArr = convertSentencesToCNF(input.getKBSent());

        addCNFSentencesToKB(cnfKBSentenceArr);

        if (DEBUG)
        {
            System.out.println("Knowledge Base: \n" + knowledgeBase.toString());
            System.out.println("****************************************");
        }

        Sentence[] querySentenceArr = convertSentencesToCNF(input.getQueries());

        Output output = resolveQueries(querySentenceArr);
        output.writeResultsToFile();
    }

    private Sentence[] convertSentencesToCNF(String[] inputSentArr)
    {
        List<Sentence> cnfKBList = new ArrayList<>();
        for (String inputStr : inputSentArr)
        {
            FOLParser folParser = new FOLParser();
            Sentence folSentence = folParser.parse(inputStr);

            CNFConverter cnfConverter = new CNFConverter();
            Sentence cnfSentence = cnfConverter.convert(folSentence);

            cnfKBList.add(cnfSentence);
        }

        return cnfKBList.toArray(new Sentence[cnfKBList.size()]);
    }

    private Output resolveQueries(Sentence[] querySentArr)
    {
        Output output = new Output();

        for (Sentence query : querySentArr)
        {
            Predicate predicateQuery;
            if (query instanceof NegatedSentence)
            {
                predicateQuery = (Predicate) ((NegatedSentence) query).getNegatedSentence();
                predicateQuery.setNegation();
            }
            else
            {
                predicateQuery = (Predicate) query;
            }

            if (DEBUG)
            {
                System.out.println("\nQuery: " + predicateQuery.toString());
            }

            Resolution resolution = new Resolution(knowledgeBase);
            Boolean result = resolution.performResolution(predicateQuery);

            if (DEBUG)
            {
                System.out.println("Result: " + result);
            }

            output.addQueryResult(result);
        }

        return output;
    }

    private void addCNFSentencesToKB(Sentence[] cnfSentenceArr)
    {
        //separate the clauses in every cnf sentence and add it to the KB
        for (Sentence cnfSentence : cnfSentenceArr)
        {
            //separate the clauses based on conjunction
            Sentence[] separatedClauseArr = KBUtil.separateClauses(cnfSentence);
            for (Sentence clause : separatedClauseArr)
            {
                //TODO: comment out debug statements
                //System.out.println("Sep: " + clause.toString());

                //the predicates are part of the disjunctive clause
                Predicate[] standardizedPredicatesInClause = KBUtil.standardizeSentence(clause, standardizationIDCounter);
                //TODO: comment out debug statement
                //System.out.println("Std: " + Arrays.toString(standardizedPredicatesInClause));

                knowledgeBase.addToKB(new DisjunctiveClause(standardizedPredicatesInClause));
            }
        }
    }

    public static void main(String[] args) throws IOException
    {
        homework obj = new homework();
        obj.run();
    }
}
