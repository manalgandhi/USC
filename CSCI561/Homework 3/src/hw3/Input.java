package hw3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author admin
 */
public class Input
{
    private String[] queriesArr;
    private String[] kbSentArr;

    public Input(String[] queriesArr, String[] kbSentArr)
    {
        this.queriesArr = queriesArr;
        this.kbSentArr = kbSentArr;
    }

    public String[] getQueries()
    {
        return queriesArr;
    }

    public String[] getKBSent()
    {
        return kbSentArr;
    }

    public static Input readInput(String pathToFile) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(pathToFile));

        int numOfQueries = Integer.valueOf(reader.readLine());
        ArrayList<String> queriesList = new ArrayList<>();
        for (int i = 0; i < numOfQueries; i++)
        {
            queriesList.add(reader.readLine());
        }

        int numOfSentInKB = Integer.valueOf(reader.readLine());
        ArrayList<String> kbSentList = new ArrayList<>();
        for (int i = 0; i < numOfSentInKB; i++)
        {
            kbSentList.add(reader.readLine());
        }

        Input inp = new Input(
                queriesList.toArray(new String[queriesList.size()]),
                kbSentList.toArray(new String[kbSentList.size()]));

        return inp;
    }

    public static Input readInput() throws FileNotFoundException, IOException
    {
        return readInput("input.txt");
    }
}
