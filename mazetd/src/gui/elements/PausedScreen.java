package gui.elements;

import com.jme3.app.state.AbstractAppState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mazetd.MazeTDGame;
import gamestates.GamestateManager;

public class PausedScreen extends AbstractAppState implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private HudScreen hudScreen = MazeTDGame.getInstance().getHudScreenInstance();
    private MazeTDGame game = MazeTDGame.getInstance();
    private GamestateManager gamestateManager = GamestateManager.getInstance();

    /** custom methods */
    public void resumeGame(String nextScreen) {
        nifty.gotoScreen(nextScreen);
        hudScreen.setPaused(false);
        gamestateManager.resume();
    }

    public void leaveGame(String nextScreen) {
        nifty.gotoScreen(nextScreen);
        gamestateManager.enterState(GamestateManager.MAINMENU_STATE);
    }

    public void quitGame() {
        game = MazeTDGame.getInstance();
        game.stop();
    }

    public PausedScreen() {
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