/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author manal
 */
public class Output
{
    private ArrayList<Boolean> queryResultList;

    public Output()
    {
        queryResultList = new ArrayList<>();
    }

    public void addQueryResult(Boolean result)
    {
        queryResultList.add(result);
    }

    public void writeResultsToFile(String pathToOutputFile) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(pathToOutputFile));

        for (Boolean result : queryResultList)
        {
            if (result)
            {
                //System.out.println("TRUE");
                writer.write("TRUE");
            }
            else
            {
                //System.out.println("FALSE");
                writer.write("FALSE");
            }
            writer.write(System.lineSeparator());
            writer.flush();
        }

        writer.close();
    }

    public void writeResultsToFile() throws IOException
    {
        writeResultsToFile("output.txt");
    }
}
