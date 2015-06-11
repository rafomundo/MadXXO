/**
 * @author Raphael M. Kuchta
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;


public class PlutoMidlet extends MIDlet implements CommandListener {
    
    private PlutoCanvas pluto = null;
    
    private Command optCommand = new Command("Options", Command.BACK, 0);

    private static PlutoMidlet myMidlet;
    
    public void startApp()
            throws MIDletStateChangeException {
        myMidlet = this;
        if(pluto == null)
        {
            pluto = new PlutoCanvas();
            pluto.setFullScreenMode(false);
            pluto.init();
            pluto.addCommand(optCommand);
            pluto.setCommandListener(this);
            Display.getDisplay(this).setCurrent(pluto);
        }
        else
        {
            Display.getDisplay(this).setCurrent(pluto);
            pluto.resume();
        }
    }

    public void pauseApp() {
        MyPauseApp();
    }

    protected void MyPauseApp()
    {
        if(pluto != null)
        {
            //pluto.pause();
            pluto.showMenue = true;

        }
    }

    public void destroyApp(boolean unconditional)
            throws MIDletStateChangeException {
        //MyExitApp();                    // call cleanup code
    }

    protected static void MyExitApp()
    {
        if (myMidlet.pluto != null) {
            myMidlet.pluto.destroy();
        }

        try {
            myMidlet.destroyApp(false);
            myMidlet.notifyDestroyed();              //destroyes MIDlet
        } catch (MIDletStateChangeException ex) {
            ex.printStackTrace();
        }    
    }

    public void commandAction(Command command, Displayable displayable) {
        if(command == optCommand)
            MyPauseApp();
    }
}
