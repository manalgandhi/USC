
import java.util.ArrayList;
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author admin
 */
public class Move
{
    private Coordinate move;
    private ArrayList<Coordinate> conquer;
    private String player;
    private String opponent;
    private String moveType;
    private Long moveValue;
    private Integer depth;

    private String rowStr;
    private String columnStr;

    public Move(Integer depth, Coordinate move, String moveType, String player, String opponent)
    {
        this.depth = depth;
        if (depth != 0)
        {
            this.moveType = moveType;
            this.move = move;
            this.player = player;
            this.opponent = opponent;

            conquer = new ArrayList<>(1);

            this.rowStr = String.valueOf((move.getX() + 1));
            this.columnStr = String.valueOf((char) (move.getY() + 'A'));
        }
    }

    public void setMoveValue(Long moveValue)
    {
        this.moveValue = moveValue;
    }

    public void conquer(Coordinate conquerMove)
    {
        conquer.add(conquerMove);
    }

    public void conquer(Coordinate[] conquerMoveArr)
    {
        conquer.addAll(Arrays.asList(conquerMoveArr));
    }

    public Integer getDepth()
    {
        return depth;
    }

    public Long getMoveValue()
    {
        return moveValue;
    }

    public Coordinate getMove()
    {
        return move;
    }

    public Coordinate[] getConquerList()
    {
        return conquer.toArray(new Coordinate[conquer.size()]);
    }

    public String getMoveType()
    {
        return moveType;
    }

    public String getMoveStr()
    {
        return columnStr + rowStr;
    }
}
