/**
 * Logic for the computer opponent.
 * There are two logics:
 * The offensiveLogic tries to win the match with the next move,
 * and the defensiveLogic tries to block the human player from winning.
 * They should be used together in combination.
 *
 * @author Raphael M. Kuchta
 */

import java.util.Random;
import javax.microedition.lcdui.game.Sprite;


public class ComputerOpponent {

    MyField board[][] = new MyField[3][3];//{{null, null, null}, {null, null, null}, {null, null, null}};
    Random random;
    int rx, ry;                         //for random board position generation
    private int positions[][] = {{0, 0}, {0, 0}, {0, 0}};
    Sprite chips[][] = new Sprite[2][3];//{{null, null, null}, {null, null, null}};

    public ComputerOpponent(MyField board[][], Sprite chips[][])
    {
        //this.board = board;
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                this.board[i][j] = board[i][j];

        random = new Random();
        this.chips = chips;
        /*for(int i = 0; i < 3; i++)
        {
            this.chips[0][i] = chips[0][i];
            this.chips[1][i] = chips[1][i];
        }*/
    }

    /** moves chips */
    private void move(int dx, int dy, int num)
    {
        if(num != -1)
            move(dx, dy, -1, -1, num);
        else
        {
            do
            {
                rx = random.nextInt(3);
             ry = random.nextInt(3);
            } while(board[rx][ry].getPlayer() != 2);
            move(dx, dy, rx, ry, num);
        }
    }
    /** moves chip */
    private void move(int dx, int dy, int sx, int sy, int num)
    {
        if(sx == -1)        //selY should be also NOT_SELECTED
        {
            chips[1][num].setRefPixelPosition(
                    board[dx][dy].getPosX(), board[dx][dy].getPosY());

            board[dx][dy].setPlayer(2);

            board[dx][dy].setChip(chips[1][num]);

            //board[dx][dy].setPosX(dx);
            //board[dx][dy].setPosY(dy);

            // ### UNMATURE ###
            positions[num][0] = dx;
            positions[num][1] = dy;
        }
        else
        {
            board[dx][dy].setChip(board[sx][sy].getChip());
            board[dx][dy].getChip().setRefPixelPosition(
                    board[dx][dy].getPosX(), board[dx][dy].getPosY());
            board[dx][dy].setPlayer(2);

            board[sx][sy].setPlayer(0);
        }
    }

    /** if computer has not placed all 3 chips, place them randomly */
    protected void randomLogic(int num)
    {
        do
        {
            rx = random.nextInt(3);
            ry = random.nextInt(3);
        } while(board[rx][ry].getPlayer() != 0);
        move(rx, ry, num);
    }

    /** defensiveLogic checks first if the opponent has a dangerous
     *  position and blocks him if necessary*/
    protected boolean defensiveLogic(int num)
    {
        //diagonal
        if(board[0][0].getPlayer() == 1 && board[1][1].getPlayer() == 1
                && board[2][2].getPlayer() == 0)
        {
            move(2,2, num);
            return true;
        }
        else if(board[0][0].getPlayer() == 1 && board[2][2].getPlayer() == 1
                && board[1][1].getPlayer() == 0)
        {
            move(1,1, num);
            return true;
        }
        else if(board[1][1].getPlayer() == 1 && board[2][2].getPlayer() == 1
                && board[0][0].getPlayer() == 0)
        {
            move(0,0, num);
            return true;
        }

        else if(board[0][2].getPlayer() == 1 && board[1][1].getPlayer() == 1
                && board[2][0].getPlayer() == 0)
        {
            move(2,0, num);
            return true;
        }
        else if(board[2][0].getPlayer() == 1 && board[0][2].getPlayer() == 1
                && board[1][1].getPlayer() == 0)
        {
            move(1,1, num);
            return true;
        }
        else if(board[2][0].getPlayer() == 1 && board[1][1].getPlayer() == 1
                && board[0][2].getPlayer() == 0)
        {
            move(0,2, num);
            return true;
        }
        else
        {
            for(int i = 0; i < 3; i++)
            {
                //horizontal
                if(board[i][0].getPlayer() == 1 && board[i][1].getPlayer() == 1
                        && board[i][2].getPlayer() == 0)
                {
                    move(i, 2, num);
                    return true;
                }

                else if(board[i][0].getPlayer() == 1 && board[i][2].getPlayer() == 1
                        && board[i][1].getPlayer() == 0)
                {
                    move(i, 1, num);
                    return true;
                }

                else if(board[i][1].getPlayer() == 1 && board[i][2].getPlayer() == 1
                        && board[i][0].getPlayer() == 0)
                {
                    move(i, 0, num);
                    return true;
                }

                //vertical
                else if(board[0][i].getPlayer() == 1 && board[1][i].getPlayer() == 1
                        && board[2][i].getPlayer() == 0)
                {
                    move(2, i, num);
                    return true;
                }
                else if(board[0][i].getPlayer() == 1 && board[2][i].getPlayer() == 1
                        && board[1][i].getPlayer() == 0)
                {
                    move(1, i, num);
                    return true;
                }
                else if(board[1][i].getPlayer() == 1 && board[2][i].getPlayer() == 1
                        && board[0][i].getPlayer() == 0)
                {
                    move(0, i, num);
                    return true;
                }
            }
        }
        //found no defensiveLogic
        return false;
    }

    /** offensiveLogic checks first if we can win with the next move */
    protected boolean offensiveLogic(int num)
    {
         //diagonal
        if(board[0][0].getPlayer() == 2 && board[1][1].getPlayer() == 2
                && board[2][2].getPlayer() == 0)
        {
            move(2,2, num);
            return true;
        }
        else if(board[0][0].getPlayer() == 2 && board[2][2].getPlayer() == 2
                && board[1][1].getPlayer() == 0)
        {
            move(1,1, num);
            return true;
        }
        else if(board[1][1].getPlayer() == 2 && board[2][2].getPlayer() == 2
                && board[0][0].getPlayer() == 0)
        {
            move(0,0, num);
            return true;
        }

        else if(board[0][2].getPlayer() == 2 && board[1][1].getPlayer() == 2
                && board[2][0].getPlayer() == 0)
        {
            move(2,0, num);
            return true;
        }
        else if(board[2][0].getPlayer() == 2 && board[0][2].getPlayer() == 2
                && board[1][1].getPlayer() == 0)
        {
            move(1,1, num);
            return true;
        }
        else if(board[2][0].getPlayer() == 2 && board[1][1].getPlayer() == 2
                && board[0][2].getPlayer() == 0)
        {
            move(0,2, num);
            return true;
        }
        else
        {
            for(int i = 0; i < 3; i++)
            {
                //horizontal
                if(board[i][0].getPlayer() == 2 && board[i][1].getPlayer() == 2
                        && board[i][2].getPlayer() == 0)
                {
                    move(i, 2, num);
                    return true;
                }

                else if(board[i][0].getPlayer() == 2 && board[i][2].getPlayer() == 2
                        && board[i][1].getPlayer() == 0)
                {
                    move(i, 1, num);
                    return true;
                }

                else if(board[i][1].getPlayer() == 2 && board[i][2].getPlayer() == 2
                        && board[i][0].getPlayer() == 0)
                {
                    move(i, 0, num);
                    return true;
                }

                //vertical
                else if(board[0][i].getPlayer() == 2 && board[1][i].getPlayer() == 2
                        && board[2][i].getPlayer() == 0)
                {
                    move(2, i, num);
                    return true;
                }
                else if(board[0][i].getPlayer() == 2 && board[2][i].getPlayer() == 2
                        && board[1][i].getPlayer() == 0)
                {
                    move(1, i, num);
                    return true;
                }
                else if(board[1][i].getPlayer() == 2 && board[2][i].getPlayer() == 2
                        && board[0][i].getPlayer() == 0)
                {
                    move(0, i, num);
                    return true;
                }
            }
        }

        //found no offensiveLogic
        return false;
    }

}
