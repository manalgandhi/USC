
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author admin
 */
public class Output
{
    private String move;
    private String moveType;
    private Board board;

    public Output(String move, String moveType, Board board)
    {
        this.move = move;
        this.moveType = moveType;
        this.board = board;
    }

    public void writeOutput() throws IOException
    {
        writeOutput("output.txt");
    }

    public void writeOutput(String pathToOutputFile) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(pathToOutputFile));
        writer.write(move + " " + moveType);

        for (int i = 0; i < board.size(); i++)
        {
            writer.newLine();
            StringBuilder rowBuilder = new StringBuilder();
            for (int j = 0; j < board.size(); j++)
            {
                Square sqr = board.getSquare(i, j);
                rowBuilder.append(sqr.getPlayer());
                //System.out.println("-"+sqr.getPlayer()+"-");
            }

            writer.write(rowBuilder.toString());
            writer.flush();
        }

        writer.close();
    }
}
