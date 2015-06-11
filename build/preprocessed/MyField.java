/**
 * The Class 'MyField' is used to store the chips on the board
 * with all necessary information such as the position in coordinate system,
 * the reference to the Sprite3D and the player owning this position.
 * (player #0 means this position is empty).
 *
 * @author Raphael M. Kuchta
 */

import javax.microedition.lcdui.game.Sprite;


public class MyField {
    
    private int posX, posY;
    private Sprite chip;
    private int player;

    public MyField()
    {
        posX = 0;
        posY = 0;
        chip = null;
        player = 0;
    }

    protected void setChip(Sprite chip)
    {
        this.chip = chip;
    }
    protected Sprite getChip()
    {
        return chip;
    }

    protected void setPosX(int posX)
    {
        this.posX = posX;
    }
    protected int getPosX()
    {
        return posX;
    }

    protected void setPosY(int posY)
    {
        this.posY = posY;
    }
    protected int getPosY()
    {
        return posY;
    }

    protected void setPlayer(int player)
    {
        this.player = player;
    }
    protected int getPlayer()
    {
        return player;
    }
}
