
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author admin
 */
public class Square
{
    private Integer columnInt;
    private Integer rowInt;
    private Integer value;
    private String player = null;

    public Square(int row, int column)
    {
        this.rowInt = row;
        this.columnInt = column;
    }

    public void setValue(Integer value)
    {
        if (this.value == null)
        {
            this.value = value;
        }
        else
        {
            System.err.println("Square vaue cannot be set multiple times");
        }
    }

    public void setPlayer(String player)
    {
        this.player = player;
    }

    public Integer getRow()
    {
        return rowInt;
    }

    public Integer getColumn()
    {
        return columnInt;
    }

    public Integer getValue()
    {
        return value;
    }

    public String getPlayer()
    {
        return player;
    }
}
