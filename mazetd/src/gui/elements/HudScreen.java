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
import logic.WaveManager;



 
public class HudScreen extends AbstractAppState implements ScreenController {
 
    
  private GamestateManager gamestateManager;   
  private Nifty nifty;
  private Screen screen;
  private SimpleApplication app;
  private MazeTDGame game;
  private NiftyJmeDisplay niftyDisplay;
  private Player player= Player.getInstance();
  private WaveManager waveManager;
  private int timeElapsed=0;
  private int min;
  private float time;
  private boolean paused;



 
  
   public HudScreen() {
       
    }   
  
  /** custom methods */ 
  
  public void disableGui() {
  nifty.exit(); 
  }

  public void setPaused(boolean paused) {
        this.paused = paused;
    }
 
  public void showMarketplace(String element, String ScreenID){
        Element marketPlace = nifty.getScreen(ScreenID).findElementByName(element);
        if(marketPlace.isVisible()) marketPlace.hide();
        else marketPlace.show();

    }
  
  public void pause(String nextScreen){
  paused=true;
  nifty.gotoScreen(nextScreen);
  gamestateManager = GamestateManager.getInstance();
  gamestateManager.pause();
  }
 
 public void showContext(){
 Element towerContext = nifty.getCurrentScreen().findElementByName("tower_context");
 towerContext.show();
 }
 
  
  public String getWave() {
  waveManager=WaveManager.getInstance();
 // if(waveManager.isInitialized()){    
  int currentWave = waveManager.getCurrentWaveCount();
  int maxWaves = waveManager.getmaxWaves();
  return currentWave+"/"+maxWaves; 
  //}
  }
   
   public String getTime(){
   if(timeElapsed<10&&min<10){
   return "0"+min+":0"+timeElapsed;
   }
   if(min<10){
   return "0"+min+":"+timeElapsed;
   }
   if(timeElapsed<10){
   return min+":0"+timeElapsed;
   }
  
   return min+":"+timeElapsed;
   }
    
    
    private void updateTextLabels(float tpf) {
    if (GamestateManager.getInstance().getCurrentState().
                equals(GamestateManager.SINGLEPLAYER_STATE)) {
   
    Element invSlot1 = nifty.getCurrentScreen().findElementByName("redCount");
    Element invSlot2 = nifty.getCurrentScreen().findElementByName("blueCount");
    Element invSlot3 = nifty.getCurrentScreen().findElementByName("greenCount");
    Element invSlot4 = nifty.getCurrentScreen().findElementByName("yellowCount");
    Element invSlot5 = nifty.getCurrentScreen().findElementByName("whiteCount");

     Element gold = nifty.getCurrentScreen().findElementByName("gold");
     Element wave = nifty.getCurrentScreen().findElementByName("wave");
     Element time = nifty.getCurrentScreen().findElementByName("time");
   

    invSlot1.getRenderer(TextRenderer.class).setText(player.getRedCount()+"x");
    invSlot2.getRenderer(TextRenderer.class).setText(player.getBlueCount()+"x");
    invSlot3.getRenderer(TextRenderer.class).setText(player.getGreenCount()+"x");
    invSlot4.getRenderer(TextRenderer.class).setText(player.getYellowCount()+"x");
    invSlot5.getRenderer(TextRenderer.class).setText(player.getWhiteCount()+"x");
   
    gold.getRenderer(TextRenderer.class).setText("Gold: "+player.getGold()); 
    wave.getRenderer(TextRenderer.class).setText("Wave: "+getWave());
    time.getRenderer(TextRenderer.class).setText("Time: "+getTime());

    }
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
        if(!paused){
        updateTextLabels(tpf);   
        time+=tpf;
        timeElapsed=(int)(time+0.5f);
        if(timeElapsed>=59){
            time=0;
            min=min+1;
        }
        }
    }
  }
 
