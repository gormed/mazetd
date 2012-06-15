package gui.elements;
 
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mazetd.MazeTDGame;
import gamestates.GamestateManager;
import gamestates.lib.SingleplayerState;
import de.lessvoid.nifty.elements.Element;
import gamestates.Gamestate;
import gamestates.GamestateManager;
import gui.elements.HudScreen;
 
public class MainmenuScreen extends AbstractAppState implements ScreenController {
 
  private Nifty nifty;
  private Screen screen;
  private SimpleApplication app;
  private MazeTDGame game;
  private SingleplayerState singleplayerState;
  private GamestateManager gamestateManager;
  
  /** custom methods */ 
  public void startGame(String nextScreen) {
  nifty.gotoScreen(nextScreen);
  gamestateManager = GamestateManager.getInstance();
  gamestateManager.enterState("Singleplayer");
  game.getHudScreenInstance().timerInit();
  }
 
  public void quitGame() {
    this.game=MazeTDGame.getInstance();
    game.stop(); 
  }
 
  
  public MainmenuScreen() { 
   
  } 
 
  /** Nifty GUI ScreenControl methods */ 
 
  public void bind(Nifty nifty, Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
  }
 
  public void onStartScreen() { }
 
  public void onEndScreen() { }
 
  /** jME3 AppState methods */ 
 
    @Override
    public void update(float tpf) {
    }


 
}