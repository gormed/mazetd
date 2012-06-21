/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * MazeTD Project (c) 2012 by Hady Khalifa, Ahmed Arous and Hans Ferchland
 * 
 * MazeTD rights are by its owners/creators.
 * The project was created for educational purposes and may be used under 
 * the GNU Public license only.
 * 
 * If you modify it please let other people have part of it!
 * 
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * GNU Public License
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License 3 as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 * 
 * Email us: 
 * hans[dot]ferchland[at]gmx[dot]de
 * 
 * 
 * Project: MazeTD Project
 * File: Tower.java
 * Type: entities.Tower
 * 
 * Documentation created: 20.05.2012 - 21:42:15 by Ahmed Arous
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui.elements;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mazetd.MazeTDGame;
import gamestates.lib.SingleplayerState;
import gamestates.GamestateManager;

/**
 * The class "MainmenuScreen"  is a registered Screencontroller that lets the 
 * application communicate with the Nifty Mainmenu Screen.
 *
 * @author Ahmed Arous
 */
public class MainmenuScreen extends AbstractAppState implements ScreenController {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /** The nifty. */
    private Nifty nifty;
    
    /** The screen. */
    private Screen screen;
    
    /** The app. */
    private SimpleApplication app;
    
    /** The game. */
    private MazeTDGame game;
    
    /** The singleplayer state. */
    private SingleplayerState singleplayerState;
    
    /** The gamestate manager. */
    private GamestateManager gamestateManager;
    
    /** The hud screen. */
    private HudScreen hudScreen = MazeTDGame.getInstance().getHudScreenInstance();

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    /**
     * constructor.
     */
    public MainmenuScreen() {
    }

    /**
     * Starts the game.
     * @param nextScreen The screen that will be shown.
     */
    public void startGame(String nextScreen) {
        nifty.gotoScreen(nextScreen);
        gamestateManager = GamestateManager.getInstance();
        gamestateManager.enterState(GamestateManager.SINGLEPLAYER_STATE);
        hudScreen.init();
    }

    /**
     * Quits the game.
     *
     */
    public void quitGame() {
        this.game = MazeTDGame.getInstance();
        game.stop();
    }

    /**
     * Nifty GUI ScreenControl methods.
     *
     * @param nifty the nifty
     * @param screen the screen
     */
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    /* (non-Javadoc)
     * @see de.lessvoid.nifty.screen.ScreenController#onStartScreen()
     */
    public void onStartScreen() {
    }

    /* (non-Javadoc)
     * @see de.lessvoid.nifty.screen.ScreenController#onEndScreen()
     */
    public void onEndScreen() {
    }

    /**
     * jME3 AppState methods.
     *
     * @param tpf the tpf
     */
    @Override
    public void update(float tpf) {
    }
}