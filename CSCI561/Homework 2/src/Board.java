
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author admin
 */
public class Board
{
    private Square[][] board;

    public Board(Square[][] board)
    {
        this.board = board;
    }
    
    public Square getSquare(int x, int y)
    {
        return board[x][y];
    }
    
    public Integer size()
    {
        return board.length;
    }

    public void performMove(Move move, String player, String opponentPlayer)
    {
        Coordinate co = move.getMove();
        board[co.getX()][co.getY()].setPlayer(player);

        for (Coordinate c : move.getConquerList())
        {
            board[c.getX()][c.getY()].setPlayer(player);
        }
    }

    public void reverseMove(Move move, String player, String opponentPlayer)
    {
        Coordinate co = move.getMove();
        board[co.getX()][co.getY()].setPlayer(homework.EMPTY_SQUARE);

        for (Coordinate c : move.getConquerList())
        {
            board[c.getX()][c.getY()].setPlayer(opponentPlayer);
        }
    }

    public Long eval(String player)
    {
        Long currentPlayerValue = 0l;
        Long opponentValue = 0l;
        for (Square[] row : board)
        {
            for (Square sqr : row)
            {
                if (sqr.getPlayer().equals(homework.EMPTY_SQUARE) == Boolean.FALSE)
                {
                    if (sqr.getPlayer().equals(player))
                    {
                        currentPlayerValue += sqr.getValue();
                    }
                    else
                    {
                        opponentValue += sqr.getValue();
                    }
                }
            }
        }

        return (currentPlayerValue - opponentValue);
    }
}
