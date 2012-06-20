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
 * Documentation created: 20.05.2012 - 21:41:32 by Ahmed Arous
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui.elements;

import com.jme3.app.state.AbstractAppState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mazetd.MazeTDGame;
import gamestates.GamestateManager;

/**
 * The class "PausedScreen"  is a registered Screencontroller that lets the 
 * application communicate with the Nifty Paused,Game Over and Game Won Screen.
 *
 * @author Ahmed Arous
 */
public class PausedScreen extends AbstractAppState implements ScreenController {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Nifty nifty;
    private Screen screen;
    private HudScreen hudScreen = MazeTDGame.getInstance().getHudScreenInstance();
    private MazeTDGame game = MazeTDGame.getInstance();
    private GamestateManager gamestateManager = GamestateManager.getInstance();

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    /**
     * constructor.
     */
    public PausedScreen() {
    }

    /**
     * Resumes the game.
     * @param nextScreen The screen that will be shown.
     */
    public void resumeGame(String nextScreen) {
        nifty.gotoScreen(nextScreen);
        hudScreen.setPaused(false);
        gamestateManager.resume();
    }

    /**
     * Leaves the game round.
     * @param nextScreen The screen that will be shown.
     */
    public void leaveGame(String nextScreen) {
        nifty.gotoScreen(nextScreen);
        gamestateManager.enterState(GamestateManager.MAINMENU_STATE);
    }

    /**
     * Quits the game.
     * @param nextScreen The screen that will be shown.
     */
    public void quitGame() {
        game = MazeTDGame.getInstance();
        game.stop();
    }

    /** Nifty GUI ScreenControl methods */
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    /** jME3 AppState methods */
    @Override
    public void update(float tpf) {
    }
}