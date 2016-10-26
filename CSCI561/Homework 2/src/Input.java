
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
    private Board board;
    //private ArrayList<ArrayList<Long>> valueBoard;

    private int n;
    private String mode;
    private String playerToPlay;
    private long depth;

    public Input(Board board,
            //ArrayList<ArrayList<Long>> valueBoard,
            int n,
            String mode,
            String playerToPlay,
            long depth)
    {
        this.board = board;
        //this.valueBoard = valueBoard;
        this.n = n;
        this.mode = mode;
        this.playerToPlay = playerToPlay;
        this.depth = depth;
    }

    public Board getBoard()
    {
        return board;
    }

    public int getN()
    {
        return n;
    }

    public String getMode()
    {
        return mode;
    }

    public String getPlayerToPlay()
    {
        return playerToPlay;
    }

    public long getDepth()
    {
        return depth;
    }
    
    public static Input readInput() throws IOException
    {
        return readInput("input.txt");
    }

    public static Input readInput(String inputFile) throws FileNotFoundException, IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

        int nL = Integer.valueOf(reader.readLine().trim());
        String modelL = reader.readLine().trim();
        String playerToPlayL = reader.readLine().trim();
        long depthL = Long.valueOf(reader.readLine().trim());

        //read the board along with the values
        Square[][] boardL = new Square[nL][nL];
        //ArrayList<ArrayList<Long>> valueBoardL = new ArrayList<>();
        for (int i = 0; i < nL; i++)
        {
            String valuesRow = reader.readLine().trim();
            String[] valuesArr = valuesRow.split("\\s+");

            if (valuesArr.length != nL)
            {
                System.out.println("Number of values in row not equal to N: " + valuesArr.length);
            }

            for (int j = 0; j < valuesArr.length; j++)
            {
                Square square = new Square(i, j);
                square.setValue(Integer.valueOf(valuesArr[j]));
                boardL[i][j] = square;
            }

        }

        //read the player positions on the board
        for (int i = 0; i < nL; i++)
        {
            String boardStateRow = reader.readLine().trim();

            for (int j = 0; j < boardStateRow.length(); j++)
            {
                boardL[i][j].setPlayer(String.valueOf(boardStateRow.charAt(j)));
            }
        }

        Board boardObj = new Board(boardL);
        Input input = new Input(boardObj, 
                //valueBoardL, 
                nL, modelL, playerToPlayL, depthL);
        return input;
    }
}
