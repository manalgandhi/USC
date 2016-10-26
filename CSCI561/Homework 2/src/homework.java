
import java.io.IOException;
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
public class homework
{
    private Long depthLimit;
    private String player;
    private Integer N;
    private Board board;

    public static final String EMPTY_SQUARE = ".";

    public static void main(String[] args) throws IOException
    {
        homework obj = new homework();
        Long start = System.currentTimeMillis();
        obj.run();
        System.out.println("Runtime: "+(System.currentTimeMillis()-start));
    }

    public void run() throws IOException
    {
        run("input.txt", "output.txt");
    }

    public void run(String inputFile, String outputFile) throws IOException
    {
        Input input = Input.readInput(inputFile);
        depthLimit = input.getDepth();
        player = input.getPlayerToPlay();
        N = input.getN();
        board = input.getBoard();

        Move baseMove = new Move(0, null, null, null, null);

        Boolean runAB;
        switch (input.getMode())
        {
            case "MINIMAX":
                runAB = Boolean.FALSE;
                break;
            case "ALPHABETA":
                runAB = Boolean.TRUE;
                break;
            default:
                runAB = Boolean.TRUE;
        }

        Output output = MINIMAX(baseMove, runAB);
        output.writeOutput(outputFile);
    }

    public Output MINIMAX(Move state, Boolean runAB)
    {
        String opponentPlayer;
        if (player.equals("X"))
        {
            opponentPlayer = "O";
        }
        else
        {
            opponentPlayer = "X";
        }

        Move bestNextMove = MAX(state, player, opponentPlayer, Long.MIN_VALUE, Long.MAX_VALUE, runAB);

        board.performMove(bestNextMove, player, opponentPlayer);
        Output output = new Output(
                bestNextMove.getMoveStr(),
                bestNextMove.getMoveType(),
                board);
        
        //System.out.println(bestNextMove.getMoveValue());

        return output;

    }

    public Move MAX(Move parentMove, String nextPlayer, String opponentPlayer, Long alpha, Long beta, Boolean runAB)
    {
        //terminal test
        if (parentMove.getDepth() >= depthLimit)
        {
            Long evalValue = board.eval(player);
            parentMove.setMoveValue(evalValue);
            return parentMove;
        }

        Move[] nextStakeMoveArr = getStakeMoves(parentMove.getDepth() + 1, nextPlayer, opponentPlayer);
        if (nextStakeMoveArr.length < 1)
        {
            Long evalValue = board.eval(player);
            parentMove.setMoveValue(evalValue);
            return parentMove;
        }

        //find max of child states
        Long value = Long.MIN_VALUE;
        Move moveToReturn = null;

        for (Move nextMove : nextStakeMoveArr)
        {
            board.performMove(nextMove, nextPlayer, opponentPlayer);
            Move move = MIN(nextMove, opponentPlayer, nextPlayer, alpha, beta, runAB);
            board.reverseMove(nextMove, nextPlayer, opponentPlayer);

            Long evalValue = move.getMoveValue();
            if (evalValue > value)
            {
                nextMove.setMoveValue(evalValue);
                moveToReturn = nextMove;
                value = evalValue;
            }

            if (runAB)
            {
                if (value >= beta)
                {
                    return moveToReturn;
                }

                alpha = Math.max(alpha, value);
            }
        }

        Move[] nextRaidMoveArr = getRaidMoves(parentMove.getDepth() + 1, nextPlayer, opponentPlayer);
        for (Move nextMove : nextRaidMoveArr)
        {
            board.performMove(nextMove, nextPlayer, opponentPlayer);
            Move move = MIN(nextMove, opponentPlayer, nextPlayer, alpha, beta, runAB);
            board.reverseMove(nextMove, nextPlayer, opponentPlayer);

            Long evalValue = move.getMoveValue();
            if (evalValue > value)
            {
                nextMove.setMoveValue(evalValue);
                moveToReturn = nextMove;
                value = evalValue;
            }

            if (runAB)
            {
                if (value >= beta)
                {
                    return moveToReturn;
                }

                alpha = Math.max(alpha, value);
            }
        }

        return moveToReturn;
    }

    public Move MIN(Move parentMove, String nextPlayer, String opponentPlayer, Long alpha, Long beta, Boolean runAB)
    {
        //terminal test
        if (parentMove.getDepth() >= depthLimit)
        {
            Long evalValue = board.eval(player);
            parentMove.setMoveValue(evalValue);
            return parentMove;
        }

        Move[] nextStakeMovesArr = getStakeMoves(parentMove.getDepth() + 1, nextPlayer, opponentPlayer);
        if (nextStakeMovesArr.length < 1)
        {
            Long evalValue = board.eval(player);
            parentMove.setMoveValue(evalValue);
            return parentMove;
        }

        //find the min of children states
        Long value = Long.MAX_VALUE;
        Move moveToReturn = null;

        for (Move nextMove : nextStakeMovesArr)
        {
            board.performMove(nextMove, nextPlayer, opponentPlayer);
            Move move = MAX(nextMove, opponentPlayer, nextPlayer, alpha, beta, runAB);
            board.reverseMove(nextMove, nextPlayer, opponentPlayer);

            Long evalValue = move.getMoveValue();
            if (evalValue < value)
            {
                nextMove.setMoveValue(evalValue);
                moveToReturn = nextMove;
                value = evalValue;
            }

            if (runAB)
            {
                if (value <= alpha)
                {
                    return moveToReturn;
                }

                beta = Math.min(beta, value);
            }
        }

        Move[] nextRaidMoveArr = getRaidMoves(parentMove.getDepth() + 1, nextPlayer, opponentPlayer);
        for (Move nextMove : nextRaidMoveArr)
        {
            board.performMove(nextMove, nextPlayer, opponentPlayer);
            Move move = MAX(nextMove, opponentPlayer, nextPlayer, alpha, beta, runAB);
            board.reverseMove(nextMove, nextPlayer, opponentPlayer);

            Long evalValue = move.getMoveValue();
            if (evalValue < value)
            {
                nextMove.setMoveValue(evalValue);
                moveToReturn = nextMove;
                value = evalValue;
            }

            if (runAB)
            {
                if (value <= alpha)
                {
                    return moveToReturn;
                }

                beta = Math.min(beta, value);
            }
        }

        return moveToReturn;
    }

    public Move[] getStakeMoves(Integer newDepth, String currentPlayer, String opponentPlayer)
    {
        ArrayList<Move> stakeMoveList = new ArrayList<>();

        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                Square sqr = board.getSquare(i, j);
                if (sqr.getPlayer().equals(EMPTY_SQUARE))
                {
                    Move stakeMove = new Move(newDepth, new Coordinate(i, j), "Stake", currentPlayer, opponentPlayer);
                    stakeMoveList.add(stakeMove);
                }
            }
        }

        return stakeMoveList.toArray(new Move[stakeMoveList.size()]);
    }

    public Move[] getRaidMoves(Integer newDepth, String currentPlayer, String oppenentPlayer)
    {
        ArrayList<Move> raidMoveList = new ArrayList<>();

        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                Square sqr = board.getSquare(i, j);
                if (sqr.getPlayer().equals(EMPTY_SQUARE))
                {
                    if (isRaidValid(currentPlayer, oppenentPlayer, i, j))
                    {
                        Move raidMove = new Move(newDepth, new Coordinate(i, j), "Raid", currentPlayer, oppenentPlayer);
                        Coordinate[] raidCoordiantesArr = getOpponentCoordinatesForRaid(i, j, currentPlayer, oppenentPlayer);
                        raidMove.conquer(raidCoordiantesArr);

                        raidMoveList.add(raidMove);
                    }
                }
            }
        }

        return raidMoveList.toArray(new Move[raidMoveList.size()]);
    }

    private Boolean isRaidValid(
            String currentPlayer,
            String opponentPlayer,
            int row,
            int column)
    {
        //left square
        if ((column - 1) >= 0)
        {
            if (board.getSquare(row, column - 1).getPlayer().equals(currentPlayer))
            {
                return Boolean.TRUE;
            }
        }
        //right
        if ((column + 1) < N)
        {
            if (board.getSquare(row, column + 1).getPlayer().equals(currentPlayer))
            {
                return Boolean.TRUE;
            }
        }
        //down
        if ((row + 1) < N)
        {
            if (board.getSquare(row + 1, column).getPlayer().equals(currentPlayer))
            {
                return Boolean.TRUE;
            }
        }
        //up
        if ((row - 1) >= 0)
        {
            if (board.getSquare(row - 1, column).getPlayer().equals(currentPlayer))
            {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    public Coordinate[] getOpponentCoordinatesForRaid(
            int raidRow,
            int raidColumn,
            String currentPlayer,
            String opponentPlayer)
    {
        ArrayList<Coordinate> coordinatesOfRaid = new ArrayList<>();

        if ((raidRow - 1) >= 0)
        {
            if (board.getSquare(raidRow - 1, raidColumn).getPlayer().equals(opponentPlayer))
            {
                coordinatesOfRaid.add(new Coordinate(raidRow - 1, raidColumn));
            }
        }
        if ((raidRow + 1) < N)
        {
            if (board.getSquare(raidRow + 1, raidColumn).getPlayer().equals(opponentPlayer))
            {
                coordinatesOfRaid.add(new Coordinate(raidRow + 1, raidColumn));
            }
        }

        if ((raidColumn - 1) >= 0)
        {
            if (board.getSquare(raidRow, raidColumn - 1).getPlayer().equals(opponentPlayer))
            {
                coordinatesOfRaid.add(new Coordinate(raidRow, raidColumn - 1));
            }
        }

        if ((raidColumn + 1) < N)
        {
            if (board.getSquare(raidRow, raidColumn + 1).getPlayer().equals(opponentPlayer))
            {
                coordinatesOfRaid.add(new Coordinate(raidRow, raidColumn + 1));
            }
        }

        return coordinatesOfRaid.toArray(new Coordinate[coordinatesOfRaid.size()]);
    }
}
