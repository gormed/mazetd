package gui.elements;
 
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mazetd.MazeTDGame;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import logic.Player;
import gamestates.Gamestate;
import gamestates.GamestateManager;
 
public class HudScreen extends AbstractAppState implements ScreenController {
 
  private Nifty nifty;
  private Screen screen;
  private SimpleApplication app;
  private MazeTDGame game;
  private NiftyJmeDisplay niftyDisplay;
  private Player player= Player.getInstance();

 
 /** custom methods */ 
 public void disableGui() {
  nifty.exit(); 
  }
  
  public void showMarketplace(String element, String ScreenID){
        Element niftyElement = nifty.getScreen(ScreenID).findElementByName(element);
        if(niftyElement.isVisible()) niftyElement.hide();
        else niftyElement.show();

    }
 
  public String getGold() {
  return "TestGold"; 
  }
  
  public String getWave() {
  return "TestWave"; 
  }
   
  public String getTime() {
  return "TestTime"; 
  }
  
  public String getHealth() {
  return "TestHealth"; 
  }
  
    public int getRed(){
    int r = player.getRedCount();
    return r;
    }
    
    public int getBlue(){
    int b = player.getBlueCount();
    return b;
    }
      
    public int getGreen(){
    int g = player.getGreenCount();
    return g;
    }
    
    public int getYellow(){
    int y = player.getYellowCount();
    return y;
    }
   
    public int getWhite(){
    int w = player.getWhiteCount();
    return w;
    }
    
    private void updateTextLabels(float tpf) {
//    if (screen.getScreenId().equals("hud")) {
        if (GamestateManager.getInstance().getCurrentState().
                equals(GamestateManager.SINGLEPLAYER_STATE)) {
    
    Element invSlot1 = nifty.getCurrentScreen().findElementByName("redCount");
    Element invSlot2 = nifty.getCurrentScreen().findElementByName("blueCount");
    Element invSlot3 = nifty.getCurrentScreen().findElementByName("greenCount");
    Element invSlot4 = nifty.getCurrentScreen().findElementByName("yellowCount");
    Element invSlot5 = nifty.getCurrentScreen().findElementByName("whiteCount");


    invSlot1.getRenderer(TextRenderer.class).setText(getRed()+"x");
    invSlot2.getRenderer(TextRenderer.class).setText(getBlue()+"x");
    invSlot3.getRenderer(TextRenderer.class).setText(getGreen()+"x");
    invSlot4.getRenderer(TextRenderer.class).setText(getYellow()+"x");
    invSlot5.getRenderer(TextRenderer.class).setText(getWhite()+"x");
    }
    }
    
  public HudScreen() {
        
    }   
  

 
  /** Nifty GUI ScreenControl methods */ 
 
  public void bind(Nifty nifty, Screen screen) {
    this.nifty = nifty;
    this.screen = screen;
  }
 
  public void onStartScreen() { 
  }
 
  public void onEndScreen() { 
  }
 
  /** jME3 AppState methods */ 
 
    @Override
    public void update(float tpf) {
    updateTextLabels(tpf);   

 
    }
  }
 
