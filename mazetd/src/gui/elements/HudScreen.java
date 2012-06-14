package gui.elements;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mazetd.MazeTDGame;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import entities.Orb;
import logic.Player;
import gamestates.GamestateManager;
import logic.WaveManager;
import entities.Orb.ElementType;
import logic.Inventory;

public class HudScreen extends AbstractAppState implements ScreenController {

    private GamestateManager gamestateManager;
    private Nifty nifty;
    private Screen screen;
    private SimpleApplication app;
    private MazeTDGame game;
    private NiftyJmeDisplay niftyDisplay;
    private Player player = Player.getInstance();
    private WaveManager waveManager;
    private int timeElapsed = 0;
    private int min;
    private float time;
    private boolean paused;
    private Orb.ElementType type;
    private Inventory inventory = Inventory.getInstance();
    private boolean towerIsClicked = false;

    public HudScreen() {
    }

    /** custom methods */
    public void disableGui() {
        nifty.exit();
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void showMarketplace(String element, String ScreenID) {
        Element marketPlace = nifty.getScreen(ScreenID).findElementByName(element);
        if (marketPlace.isVisible()) {
            marketPlace.hide();
        } else {
            marketPlace.show();
        }

    }

    public void demolishTower() {
        player.getSelectedTower().demolish();
    }

    public void pause(String nextScreen) {
        paused = true;
        nifty.gotoScreen(nextScreen);
        gamestateManager = GamestateManager.getInstance();
        gamestateManager.pause();
    }

    public void replaceOrb(String orbType, String slot){
        int type = Integer.parseInt(orbType);
        int slotI = Integer.parseInt(slot);
        
        ElementType element= ElementType.values()[type];
        inventory.replaceOrb(slotI, element);
        
        Element orbSelection1 = nifty.getCurrentScreen().findElementByName("orbSelection1");
        orbSelection1.hide();
        
        Element orbSelection2 = nifty.getCurrentScreen().findElementByName("orbSelection2");
        orbSelection2.hide();
        
        Element orbSelection3 = nifty.getCurrentScreen().findElementByName("orbSelection3");
        orbSelection3.hide();
        
  }
  
   
    public void orbSelection1(){
        Element orbSelection = nifty.getCurrentScreen().findElementByName("orbSelection1");
        orbSelection.show();
  }
    
      public void orbSelection2(){
        Element orbSelection = nifty.getCurrentScreen().findElementByName("orbSelection2");
        orbSelection.show();
  }
      
        public void orbSelection3(){
        Element orbSelection = nifty.getCurrentScreen().findElementByName("orbSelection3");
        orbSelection.show();
  }
    public void showContext(float tpf) {
        if (player.towerIsClicked()) {
            Element towerContext = nifty.getCurrentScreen().findElementByName("tower_context");
            towerContext.show();
            towerIsClicked = true;
        } else {
            towerIsClicked = false;
        }
    }

    public String getWave() {
        waveManager = WaveManager.getInstance();
        int currentWave = waveManager.getCurrentWaveCount();
        int maxWaves = waveManager.getmaxWaves();
        return currentWave + "/" + maxWaves;

    }

    public String getTime() {
        if (timeElapsed < 10 && min < 10) {
            return "0" + min + ":0" + timeElapsed;
        }
        if (min < 10) {
            return "0" + min + ":" + timeElapsed;
        }
        if (timeElapsed < 10) {
            return min + ":0" + timeElapsed;
        }

        return min + ":" + timeElapsed;
    }
    
    public String getTowerHP(){
    float hp = player.getSelectedTower().getHealthPoints();
    float maxHp=player.getSelectedTower().getMaxHealthPoints();
        return hp+"/"+maxHp;
    }
    

    private void slot3Context(float tpf) {
        if (GamestateManager.getInstance().getCurrentState().
                equals(GamestateManager.SINGLEPLAYER_STATE)) {
            if (towerIsClicked) {
                Element slot3 = nifty.getCurrentScreen().findElementByName("Orb3");
                Orb.ElementType type3 =inventory.Slot3Context();
                if (type3 != null) {
                    switch (type3) {
                        case RED:
                            NiftyImage redOrbSlot = nifty.getRenderEngine().createImage("Interface/Textures/Orbs/redOrbSlot.png", false);
                            slot3.getRenderer(ImageRenderer.class).setImage(redOrbSlot);
                            slot3.show();
                           break;
                        case BLUE:
                            NiftyImage blueOrbSlot = nifty.getRenderEngine().createImage("Interface/Textures/Orbs/blueOrbSlot.png", false);
                            slot3.getRenderer(ImageRenderer.class).setImage(blueOrbSlot);
                            slot3.show();
                            break;
                        case GREEN:
                            NiftyImage greenOrbSlot = nifty.getRenderEngine().createImage("Interface/Textures/Orbs/greenOrbSlot.png", false);
                            slot3.getRenderer(ImageRenderer.class).setImage(greenOrbSlot);
                            slot3.show();
                            break;
                        case YELLOW:            
                            NiftyImage yellowOrbSlot = nifty.getRenderEngine().createImage("Interface/Textures/Orbs/yellowOrbSlot.png", false);
                            slot3.getRenderer(ImageRenderer.class).setImage(yellowOrbSlot);
                            slot3.show();
                            break;
                        case WHITE:
                            NiftyImage whiteOrbSlot = nifty.getRenderEngine().createImage("Interface/Textures/Orbs/whiteOrbSlot.png", false);
                            slot3.getRenderer(ImageRenderer.class).setImage(whiteOrbSlot);
                            slot3.show();
                            break;
                            
                    }
                } else {
                    slot3.hide();
                }
            }
            /*else{ 
            Element towerContext = nifty.getCurrentScreen().findElementByName("tower_context");
            towerIsClicked=false;
            towerContext.hide();
            }*/
        }
    }

    private void slot2Context(float tpf) {
        if (GamestateManager.getInstance().getCurrentState().
                equals(GamestateManager.SINGLEPLAYER_STATE)) {
            if (towerIsClicked) {
                Element slot2 = nifty.getCurrentScreen().findElementByName("Orb2");
                Orb.ElementType type2=inventory.Slot2Context();
                if (type2 != null) {
                    switch (type2) {
                        case RED:
                            NiftyImage redOrbSlot = nifty.getRenderEngine().createImage("Interface/Textures/Orbs/redOrbSlot.png", false);
                            slot2.getRenderer(ImageRenderer.class).setImage(redOrbSlot);
                            slot2.show();
                                    break;
                        case BLUE:
                            NiftyImage blueOrbSlot = nifty.getRenderEngine().createImage("Interface/Textures/Orbs/blueOrbSlot.png", false);
                            slot2.getRenderer(ImageRenderer.class).setImage(blueOrbSlot);
                            slot2.show();
                            break;
                        case GREEN:
                            NiftyImage greenOrbSlot = nifty.getRenderEngine().createImage("Interface/Textures/Orbs/greenOrbSlot.png", false);
                            slot2.getRenderer(ImageRenderer.class).setImage(greenOrbSlot);
                            slot2.show();
                            break;
                        case YELLOW:
                            NiftyImage yellowOrbSlot = nifty.getRenderEngine().createImage("Interface/Textures/Orbs/yellowOrbSlot.png", false);
                            slot2.getRenderer(ImageRenderer.class).setImage(yellowOrbSlot);
                            slot2.show();
                            break;
                        case WHITE:
                            NiftyImage whiteOrbSlot = nifty.getRenderEngine().createImage("Interface/Textures/Orbs/whiteOrbSlot.png", false);
                            slot2.getRenderer(ImageRenderer.class).setImage(whiteOrbSlot);
                            slot2.show();
                            break;
                    }
                } else {
                    slot2.hide();
                }
            }
        }
    }

    private void slot1Context(float tpf) {
        if (GamestateManager.getInstance().getCurrentState().
                equals(GamestateManager.SINGLEPLAYER_STATE)) {
            if (towerIsClicked) {
                
                Element towerHP = nifty.getCurrentScreen().findElementByName("towerHP");
                towerHP.getRenderer(TextRenderer.class).setText(getTowerHP());
                
                Element slot1 = nifty.getCurrentScreen().findElementByName("Orb1");
                Orb.ElementType type1=inventory.Slot1Context();
               
                if (type1 != null) {
                    
                    switch (type1) {
                        case RED:
                            NiftyImage redOrbSlot = nifty.getRenderEngine().createImage("Interface/Textures/Orbs/redOrbSlot.png", false);
                            slot1.getRenderer(ImageRenderer.class).setImage(redOrbSlot);
                            slot1.show();
                            break;
                        case BLUE:
                            NiftyImage blueOrbSlot = nifty.getRenderEngine().createImage("Interface/Textures/Orbs/blueOrbSlot.png", false);
                            slot1.getRenderer(ImageRenderer.class).setImage(blueOrbSlot);
                            slot1.show();
                            break;
                        case GREEN:
                            NiftyImage greenOrbSlot = nifty.getRenderEngine().createImage("Interface/Textures/Orbs/greenOrbSlot.png", false);
                            slot1.getRenderer(ImageRenderer.class).setImage(greenOrbSlot);
                            slot1.show();
                            break;
                        case YELLOW:
                            NiftyImage yellowOrbSlot = nifty.getRenderEngine().createImage("Interface/Textures/Orbs/yellowOrbSlot.png", false);
                            slot1.getRenderer(ImageRenderer.class).setImage(yellowOrbSlot);
                            slot1.show();
                            break;
                        case WHITE:
                            NiftyImage whiteOrbSlot = nifty.getRenderEngine().createImage("Interface/Textures/Orbs/whiteOrbSlot.png", false);
                            slot1.getRenderer(ImageRenderer.class).setImage(whiteOrbSlot);
                            slot1.show();
                            break;
                    }
                } else {
                    slot1.hide();
                }
            }
        }
    }

    private void updateHealthglobe(float tpf) {
        if (GamestateManager.getInstance().getCurrentState().
                equals(GamestateManager.SINGLEPLAYER_STATE)) {

            Element healthglobe = nifty.getCurrentScreen().findElementByName("healthglobe");

            if (player.getLives() > 4) {
                NiftyImage healthglobe_0 = 
                        nifty.getRenderEngine().createImage(
                        "Interface/Textures/Healthglobe/Healthglobe_Full.png", false);
                healthglobe.getRenderer(ImageRenderer.class).setImage(healthglobe_0);
            }
            if (player.getLives() == 4) {
                NiftyImage healthglobe_1 = 
                        nifty.getRenderEngine().createImage(
                        "Interface/Textures/Healthglobe/Healthglobe_hit_1.png", false);
                healthglobe.getRenderer(ImageRenderer.class).setImage(healthglobe_1);
            }
            if (player.getLives() == 3) {
                NiftyImage healthglobe_2 = 
                        nifty.getRenderEngine().createImage(
                        "Interface/Textures/Healthglobe/Healthglobe_hit_2.png", false);
                healthglobe.getRenderer(ImageRenderer.class).setImage(healthglobe_2);
            }
            if (player.getLives() == 2) {
                NiftyImage healthglobe_3 = 
                        nifty.getRenderEngine().createImage(
                        "Interface/Textures/Healthglobe/Healthglobe_hit_3.png", false);
                healthglobe.getRenderer(ImageRenderer.class).setImage(healthglobe_3);
            }
            if (player.getLives() == 1) {
                NiftyImage healthglobe_4 = 
                        nifty.getRenderEngine().createImage(
                        "Interface/Textures/Healthglobe/Healthglobe_hit_4.png", false);
                healthglobe.getRenderer(ImageRenderer.class).setImage(healthglobe_4);
            }
            if (player.getLives() <= 0) {
                NiftyImage healthglobe_5 = 
                        nifty.getRenderEngine().createImage(
                        "Interface/Textures/Healthglobe/Healthglobe_hit_5.png", false);
                healthglobe.getRenderer(ImageRenderer.class).setImage(healthglobe_5);
            }

        }
    }

    private void updateTextLabels(float tpf) {
        if (GamestateManager.getInstance().getCurrentState().
                equals(GamestateManager.SINGLEPLAYER_STATE)) {
            //find old text
            Element invSlot1 = nifty.getCurrentScreen().findElementByName("redCount");
            Element invSlot2 = nifty.getCurrentScreen().findElementByName("blueCount");
            Element invSlot3 = nifty.getCurrentScreen().findElementByName("greenCount");
            Element invSlot4 = nifty.getCurrentScreen().findElementByName("yellowCount");
            Element invSlot5 = nifty.getCurrentScreen().findElementByName("whiteCount");

            Element gold = nifty.getCurrentScreen().findElementByName("gold");
            Element wave = nifty.getCurrentScreen().findElementByName("wave");
            Element time = nifty.getCurrentScreen().findElementByName("time");
           
           
            //swap old with new text
            invSlot1.getRenderer(TextRenderer.class).setText(player.getRedCount() + "x");
            invSlot2.getRenderer(TextRenderer.class).setText(player.getBlueCount() + "x");
            invSlot3.getRenderer(TextRenderer.class).setText(player.getGreenCount() + "x");
            invSlot4.getRenderer(TextRenderer.class).setText(player.getYellowCount() + "x");
            invSlot5.getRenderer(TextRenderer.class).setText(player.getWhiteCount() + "x");

            gold.getRenderer(TextRenderer.class).setText("Gold: " + player.getGold());
            wave.getRenderer(TextRenderer.class).setText("Wave: " + getWave());
            time.getRenderer(TextRenderer.class).setText("Time: " + getTime());
            
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
        if (!paused) {
            updateTextLabels(tpf);
            updateHealthglobe(tpf);
            showContext(tpf);
            slot3Context(tpf);
            slot2Context(tpf);
            slot1Context(tpf);
            // updateHealthglobe(tpf);
            time += tpf;
            timeElapsed = (int) (time + 0.5f);
            if (timeElapsed >= 59) {
                time = 0;
                min = min + 1;
            }
        }
    }
}
