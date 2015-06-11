/**
 * MyLoader is used to load most of the Images in this game.
 * It manages also alpha (blending), appearance and composingMode.
 *
 * @author Raphael M. Kuchta
 */

import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;


public class MyLoader {

    private static String[] boards = { "/res/feld1.png", "/res/feld2.png",
        "/res/feld3.png", "/res/feld4.png", "/res/feld5.png",
        "/res/feld6.png", "/res/feld7.png", "/res/feld8.png" };

    private static String[] chips = {"/res/red.png", "/res/yellow.png"};

//    nicht mehr notwendig, da STATIC:
//    public MyLoader()
//    {
//    }

    protected static Image loadBackground(int number)
    {
        //if number > available fields, use last field
        if(number > boards.length)
            number = boards.length;

        Image bg = null;
        try {
            bg = Image.createImage(boards[number]);
        } catch (IOException ex) {}
        return bg;
    }

    protected static Sprite loadChip(int color)
    {
        if(color > chips.length)
            color = chips.length;
        Sprite chip_sprite = null;
        try {
            chip_sprite = new Sprite(Image.createImage(chips[color]), 50, 50);
        } catch (IOException ex) {}
        chip_sprite.defineReferencePixel(25, 25);
        return chip_sprite;
    }

    protected static Sprite loadSelection()
    {
        Sprite selection_sprite = null;
        try {
            selection_sprite = new Sprite(Image.createImage("/res/auswahl.png"));
        } catch (IOException ex) {}
        selection_sprite.defineReferencePixel(25, 25);
        return selection_sprite;
    }

    protected static Image loadMenueSelector()
    {
        Image img = null;
        try {
            img = Image.createImage("/res/menue_auswahl.png");
        } catch (IOException ex) {}

        return img;
    }

    protected static Sprite loadMenue()
    {
        Sprite menue_sprite = null;
        try {
            menue_sprite = new Sprite(Image.createImage("/res/menue.png"), 100, 20);
        } catch (IOException ex) {}
        return menue_sprite;
    }
}
