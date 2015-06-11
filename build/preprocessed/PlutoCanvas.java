/**
 *
 * @author Raphael M. Kuchta
 */

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;


public class PlutoCanvas extends GameCanvas implements Runnable {

    private int screenWidth;
    private int screenHeight;
    
    private boolean isRunning = false;
    private Graphics graphics;
    
    private boolean[] key = new boolean[5];
    private static final int K_FIRE = 0;
    private static final int K_UP = 1;
    private static final int K_DOWN = 2;
    private static final int K_LEFT = 3;
    private static final int K_RIGHT = 4;

    //match type
    private static final int ONE_PLAYER = 0;
    private static final int TWO_PLAYER = 1;
    private static final int BLUETOOTH = 2;

    private int mode = ONE_PLAYER;

    private static final int MOVEMENT = 60;

    private static final int NOT_SELECTED = -1;

    private static final int OFFSET = 30;

    private Image background;

    private int x,y;
    private int selX, selY;
    private int p1_color = 0;
    private int p2_color = 1;
    private int currentPlayer;
    private int whoWon = 0;
    
    private int allIn[] = {3 , 3};//new int[2];   //How many fresh chips this player has
    // [currentPlayer - 1][3 chips]
    private Sprite chips[][] = {{null, null, null}, {null, null, null}};
    // [00][10][20]
    // [01][11][21]
    // [02][12][22]
    private MyField board[][] = {{null, null, null}, {null, null, null}, {null, null, null}};
    private Sprite selection = null;

    private Font myFont;
    
    protected boolean showMenue = false;

    private ComputerOpponent comp = null;

    
   
    
 // DEBUG /////////////////////
    private long time = 0;
    private int minFrames = 100;
 //////////////////////////////

    public PlutoCanvas()
    {
        super(true);
    }
    
    /** The init method is called from the PlutoMidlet class 
     *  to initialise the game for the first time.
     */
    protected void init()
    {
        graphics = getGraphics();
        
        screenWidth = getWidth();
        screenHeight = getHeight();

        myFont = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD,
                Font.SIZE_LARGE);

        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                board[i][j] = new MyField();

        board[0][0].setPosX((int) screenWidth/2 - MOVEMENT);
        board[0][0].setPosY((int) screenHeight/2 - MOVEMENT);
        board[0][1].setPosX((int) screenWidth/2 - MOVEMENT);
        board[0][1].setPosY((int) screenHeight/2);
        board[0][2].setPosX((int) screenWidth/2 - MOVEMENT);
        board[0][2].setPosY((int) screenHeight/2 + MOVEMENT);

        board[1][0].setPosX((int) screenWidth/2);
        board[1][0].setPosY((int) screenHeight/2 - MOVEMENT);
        board[1][1].setPosX((int) screenWidth/2);
        board[1][1].setPosY((int) screenHeight/2);
        board[1][2].setPosX((int) screenWidth/2);
        board[1][2].setPosY((int) screenHeight/2 + MOVEMENT);

        board[2][0].setPosX((int) screenWidth/2 + MOVEMENT);
        board[2][0].setPosY((int) screenHeight/2 - MOVEMENT);
        board[2][1].setPosX((int) screenWidth/2 + MOVEMENT);
        board[2][1].setPosY((int) screenHeight/2);
        board[2][2].setPosX((int) screenWidth/2 + MOVEMENT);
        board[2][2].setPosY((int) screenHeight/2 + MOVEMENT);

        for(int i = 0; i < 3; i++)
        {
            chips[0][i] = MyLoader.loadChip(p1_color);
            chips[1][i] = MyLoader.loadChip(p2_color);
        }

        //background = MyLoader.loadBackground(bg_number);

        selection = MyLoader.loadSelection();

        restart();

        System.gc();                    //release memory

        Thread thread = new Thread(this);
        isRunning = true;
        thread.start();
    }
    
    protected void pause()
    {
        isRunning = false;
    }

    /** Restarts a game. All positions and visible items 
     *  are reset to their default values.
     */
    protected void restart()
    {
        x = 1;
        y = 1;
        selX = NOT_SELECTED;
        selY = NOT_SELECTED;
        currentPlayer = 1;              //Player 1 is the default first player

        //give 3 chips to both players
        allIn[0] = 3;
        allIn[1] = 3;

        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
            {
                board[i][j].setChip(null);
                board[i][j].setPlayer(0);
            }

        selection.setRefPixelPosition((int) screenWidth/2,(int) screenHeight/2);


        comp = null;
        if(mode == ONE_PLAYER)
        {
            comp = new ComputerOpponent(board, chips);
        }
    }

    protected void resume()
    {
        Thread thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    protected void showMenue()
    {
        // DEBUG !!!!
        Sprite menue = MyLoader.loadMenue();
        int selected = 0;
        int first = 0;                  // inclusive (first run -> continue)
        int last = 12;                  // exclusive (first run -> exit)
        int nextElement;
        Image menue_selector = MyLoader.loadMenueSelector();

        graphics.setColor(0x23452c);
        //graphics.fillRect(0, 0, screenWidth, screenHeight);
        menue.defineReferencePixel(50, 10);



        while(showMenue)
        {
            graphics.fillRect(0, 0, screenWidth, screenHeight);
            nextElement = 0;
            for(int i = first; i < last; i += 2)
            {
                if(i == selected)
                    graphics.drawImage(menue_selector, screenWidth/2, OFFSET + nextElement * 20, Graphics.HCENTER | Graphics.VCENTER);

                menue.setFrame(i);
                
                menue.setRefPixelPosition(screenWidth/2, OFFSET + nextElement * 20);
                menue.paint(graphics);
                nextElement ++;
            }
            flushGraphics();

            try {
                Thread.sleep(150);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            processKeys();
            if(key[K_FIRE])
            {
                switch(selected)
                {
                    case 0:             // Continue
                        showMenue = false;
                        break;
                    case 2:             // New Game
                        first = 12;
                        selected = first;
                        last = 18;
                        break;
                    case 4:             // Options
                        break;
                    case 6:             // Help
                        break;
                    case 8:             // About
                        break;
                    case 10:            // Exit
                        PlutoMidlet.MyExitApp();
                        break;
                        //TODO: Implement it right!
                    case 12:            // One Player
                        mode = ONE_PLAYER;
                        restart();
                        showMenue = false;
                        break;
                    case 14:            // Two Player
                        mode = TWO_PLAYER;
                        restart();
                        showMenue = false;
                        break;
                    case 16:            //Bluetooth
                        break;
                }
            }
            else if(key[K_UP] && selected != first)
                selected -= 2;
            else if(key[K_DOWN] && selected != last - 2)
                selected += 2;
        }
//        isRunning = false;
//        resume();
    }

    
    public void run() {
        while(isRunning)
        {
            //graphics.drawImage(background, 0, 0, Graphics.TOP |Graphics.LEFT);
            
            graphics.setColor(0x88AADD);
            graphics.fillRect(0, 0, screenWidth, screenHeight);

            time = System.currentTimeMillis();
            
            if((mode == ONE_PLAYER) && (currentPlayer == 2))
            {
                if(allIn[1] != -1)
                    allIn[1]--;

                if(!comp.defensiveLogic(allIn[1]))
                        if(!comp.offensiveLogic(allIn[1]))
                            comp.randomLogic(allIn[1]);

                currentPlayer = 1;
            }
            else
            {
                processKeys();
                processAction();
            }


            //RENDER ******************************************
            for(int i = 0; i < 3; i++)
                for(int j = 0; j < 3; j++)
                    if(board[i][j].getPlayer() != 0)
                        board[i][j].getChip().paint(graphics);
            
            selection.paint(graphics);
            
            //END RENDER **************************************
    
            graphics.setFont(myFont);
            graphics.setColor(0x000000);
            
            time = (System.currentTimeMillis() - time);
            
            graphics.drawString("Turn: Player " + currentPlayer, screenWidth - 5, 5, Graphics.TOP | Graphics.RIGHT);

            flushGraphics();

            whoWon = isWon();
            //TODO: show "Player X won!" screen and stop the game.
            if(whoWon != 0)
            {
                graphics.fillRect(30, 80, 190, 80);
                graphics.setColor(0xAABBCC);
                if(whoWon == 1)
                    graphics.drawString("Player 1 won!!!" ,
                            60, 100, Graphics.TOP | Graphics.LEFT);
                else if(whoWon == 2)
                    graphics.drawString("Player 2 won!!!" ,
                            60, 100, Graphics.TOP | Graphics.LEFT);

                graphics.drawString("Press FIRE to continue", 40, 120, Graphics.TOP | Graphics.LEFT);

                flushGraphics();

                do
                {
                    processKeys();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {}
                } while(!key[K_FIRE]);

                showMenue = true;
            }

            if(showMenue == true)
                showMenue();

            try
            {
                Thread.sleep(200);
            }
            catch(Exception ex) {}
        }
    }
    
    protected void processKeys()
    {
        int keys = getKeyStates();
        
        if((keys & GameCanvas.FIRE_PRESSED) != 0)
            key[K_FIRE] = true;
        else
            key[K_FIRE] = false;

        if((keys & GameCanvas.UP_PRESSED) != 0)
            key[K_UP] = true;
        else
            key[K_UP] = false;

        if((keys & GameCanvas.DOWN_PRESSED) != 0)
            key[K_DOWN] = true;
        else
            key[K_DOWN] = false;

        if((keys & GameCanvas.LEFT_PRESSED) != 0)
            key[K_LEFT] = true;
        else
            key[K_LEFT] = false;

        if((keys & GameCanvas.RIGHT_PRESSED) != 0)
            key[K_RIGHT] = true;
        else
            key[K_RIGHT] = false;
    }
    
    private void processAction()
    {
        if(key[K_UP] && (y != 0))       //TOP
        {
            selection.setRefPixelPosition(selection.getRefPixelX(),
                    selection.getRefPixelY() - MOVEMENT);
            y--;
        }
        else if(key[K_DOWN] && (y != 2))//BOTTOM
        {
           selection.setRefPixelPosition(selection.getRefPixelX(),
                   selection.getRefPixelY() + MOVEMENT);
           y++;
        }

        if(key[K_RIGHT] && (x != 2))    //RIGHT
        {
            selection.setRefPixelPosition(selection.getRefPixelX() + MOVEMENT,
                    selection.getRefPixelY());
            x++;
        }
        else if(key[K_LEFT] && (x != 0))//LEFT
        {
            selection.setRefPixelPosition(selection.getRefPixelX() - MOVEMENT,
                    selection.getRefPixelY());
            x--;
        }

        if(key[K_FIRE])
        {
            //No fields selected (selY is also -1)
            if(selX == NOT_SELECTED)
            {
                //Wo dont move chips; we put a new one in the game
                if(allIn[currentPlayer - 1] != 0)
                {
                    if(board[x][y].getPlayer() == 0)
                    {
                        move();
                    }
                    else
                    {
                        //TODO: error effect
                    }
                }
                else                    //select chip for movement
                {
                    //Empty fields have player number 0!
                    if(board[x][y].getPlayer() == currentPlayer)
                    {
                        // TODO: visible selected
                        selX = x;
                        selY = y;
                        //TODO: blink effect
                    }
                    else
                    {
                        //TODO: error effect (blink / tone)
                    }
                }
            }
            else
            {
                //The field is empty. We can move there.
                if(board[x][y].getPlayer() == 0)
                {
                    move();
                }
                else
                {
                    //TODO: error effect ( blink / tone)

                    //TODO: evtl. unselect prev. selected chip!
                }
            }
        }
    }

    /** Stops the thread */
    protected void destroy()
    {
        isRunning = false;
        //TODO: save game state???
    }

    /** Returns 0 if no player has won, 1 if player 1 has won
     *  and 2 if player 2 has won. */
    private int isWon()
    {
        for(int i = 0; i < 3; i++)
        {
            //horizontal
            if(board[i][0].getPlayer() == 1 && board[i][1].getPlayer() == 1 &&
                    board[i][2].getPlayer() == 1)
                return 1;
            //else
            if (board[i][0].getPlayer() == 2 && board[i][1].getPlayer() == 2 &&
                    board[i][2].getPlayer() == 2)
                return 2;

            //vertical
            if(board[0][i].getPlayer() == 1 && board[1][i].getPlayer() == 1 &&
                    board[2][i].getPlayer() == 1)
                return 1;
            //else
            if(board[0][i].getPlayer() == 2 && board[1][i].getPlayer() == 2 &&
                    board[2][i].getPlayer() == 2)
                return 2;
        }

        //diagonal
        if(board[0][0].getPlayer() == 1 && board[1][1].getPlayer() == 1 &&
                board[2][2].getPlayer() == 1)
            return 1;
        if(board[0][2].getPlayer() == 1 && board[1][1].getPlayer() == 1 &&
                board[2][0].getPlayer() == 1)
            return 1;
        if(board[0][0].getPlayer() == 2 && board[1][1].getPlayer() == 2 &&
                board[2][2].getPlayer() == 2)
            return 2;
        if(board[0][2].getPlayer() == 2 && board[1][1].getPlayer() == 2 &&
                board[2][0].getPlayer() == 2)
            return 2;

        return 0;
    }

    private void move()
    {
        if(selX == NOT_SELECTED)        //selY should be also NOT_SELECTED
        {
            allIn[currentPlayer - 1]--;

            chips[currentPlayer - 1][allIn[currentPlayer - 1]].setRefPixelPosition(
                    selection.getRefPixelX(), selection.getRefPixelY());

            board[x][y].setPlayer(currentPlayer);

            board[x][y].setChip(
                    chips[currentPlayer - 1][allIn[currentPlayer - 1]]);

            board[x][y].setPosX(selection.getRefPixelX());
            board[x][y].setPosY(selection.getRefPixelY());
        }
        else
        {
            board[x][y].setChip(board[selX][selY].getChip());
            board[x][y].getChip().setRefPixelPosition(
                    selection.getRefPixelX(), selection.getRefPixelY());
            board[x][y].setPlayer(currentPlayer);

            board[selX][selY].setPlayer(0);
            selX = NOT_SELECTED;
            selY = NOT_SELECTED;
        }

        if(currentPlayer == 1)
            currentPlayer = 2;
        else
            currentPlayer = 1;
    }
}
