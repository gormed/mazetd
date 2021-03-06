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
 * File: MazeTDGame.java
 * Type: mazetd.MazeTDGame
 * 
 * Documentation created: 13.05.2012 - 23:13:37 by Hans
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package mazetd;

import eventsystem.port.ScreenRayCast3D;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext.Type;
import com.jme3.util.BufferUtils;
import eventsystem.EventManager;
import gamestates.GamestateManager;
import gamestates.lib.MainmenuState;
import gamestates.lib.SingleplayerState;
import de.lessvoid.nifty.Nifty;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.system.JmeSystem;
import de.lessvoid.nifty.screen.ScreenController;
import gui.elements.MainmenuScreen;
import gui.elements.HudScreen;
import gui.elements.PausedScreen;
import java.util.HashMap;

/**
 * Main application class of MazeTD. 
 * 
 * Builds the jme game context, loads assetmanager, camera, 
 * scenegraph and many more.
 * @author Hans Ferchland
 * @version 0.12
 * @see SimpleApplication
 */
public class MazeTDGame extends SimpleApplication {

    /** The Hud screen instance. */
    private HudScreen HudScreenInstance;
    
    /** The Constant HUD_SCREEN. */
    public static final String HUD_SCREEN = "HUDScreen";
    
    /** The Constant MAIN_MENU_SCREEN. */
    public static final String MAIN_MENU_SCREEN = "MainMenueScreen";
    //==========================================================================
    //===   Constants
    //==========================================================================
    /** The Constant PAUSE_SCREEN. */
    public static final String PAUSE_SCREEN = "PauseScreen";
    
    /** The Constant SHADOWS_ENABLED. */
    private static final boolean SHADOWS_ENABLED = true;
    
    /** The Constant SSAO_ENABLED. */
    private static final boolean SSAO_ENABLED = false;
    //==========================================================================
    //===   Singleton and Main-method
    //==========================================================================
    /** The single reference to the game. */
    private static MazeTDGame instance;
    
    /** The was paused. */
    private boolean wasPaused;
    
    /** The pause. */
    private boolean pause;

    /**
     * The hidden contructor of the game.
     */
    private MazeTDGame() {
        super();
        assetManager = JmeSystem.newAssetManager(Thread.currentThread().getContextClassLoader().getResource("com/jme3/asset/Desktop.cfg"));

    }

    /**
     * Gets the single reference to the game.
     * @return the MazeTDGame reference
     */
    public static MazeTDGame getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new MazeTDGame();
    }

    /**
     * Main invocation method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        getInstance().initSettings();

        getInstance().start();
    }
    //==========================================================================
    //===   Static Fields
    //==========================================================================
    /** The Constant INPUT_MAPPING_CAMERA_DEBUG. */
    public static final String INPUT_MAPPING_CAMERA_DEBUG = "MAZETDGAME_CameraDebug";
    
    /** The Constant BLOOM_INTENSITY. */
    public static final float BLOOM_INTENSITY = 1.0f;
    
    /** The Constant BLOOM_SAMPLING_FACTOR. */
    public static final int BLOOM_SAMPLING_FACTOR = 2;
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /** The gamestate manager. */
    private GamestateManager gamestateManager;
    
    /** The ray cast3 d. */
    private ScreenRayCast3D rayCast3D;
    
    /** The event manager. */
    private EventManager eventManager;
    
    /** The iso camera control. */
    private IsoCameraControl isoCameraControl;
    
    /** The app states. */
    private HashMap<String, AbstractAppState> appStates =
            new HashMap<String, AbstractAppState>();
    
    /** The game debug action listener. */
    private GameDebugActionListener gameDebugActionListener =
            new GameDebugActionListener();
    
    /** The show fps. */
    private boolean showFps = false;
    /** The post processor. */
    private FilterPostProcessor postProcessor;
    
    /** The bloom filter. */
    private BloomFilter bloomFilter =
            new BloomFilter(BloomFilter.GlowMode.Objects);
    
    /** The pssm shadow renderer. */
    private PssmShadowRenderer pssmShadowRenderer;
    //==========================================================================
    //===   Package Fields
    //==========================================================================

    //==========================================================================
    //===   Application Methods
    //==========================================================================
    /**
     * Initializes gamestate manager and adds gamestates; starts with initial state.
     */
    private void initGamestates() {
        gamestateManager = GamestateManager.getInstance();
        MainmenuState mainmenuState = new MainmenuState();
        SingleplayerState singleplayerState = new SingleplayerState();

        gamestateManager.addState(singleplayerState);
        gamestateManager.initialize(mainmenuState);
        gamestateManager.start();

        //flyCam.setDragToRotate(true);
    }

    /**
     * initializes the gui.
     */
    private void initGUI() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        HudScreenInstance = new HudScreen();
        appStates.put(MAIN_MENU_SCREEN, new MainmenuScreen());
        appStates.put(HUD_SCREEN, HudScreenInstance);
        appStates.put(PAUSE_SCREEN, new PausedScreen());
        nifty.fromXml("Interface/screen.xml", "start",
                (ScreenController) appStates.get(MAIN_MENU_SCREEN),
                (ScreenController) appStates.get(HUD_SCREEN),
                (ScreenController) appStates.get(PAUSE_SCREEN));
        guiViewPort.addProcessor(niftyDisplay);

    }

    /**
     * Initializes postprocessor and adds filters.
     */
    private void initPostProcessors() {
        postProcessor = new FilterPostProcessor(assetManager);
        bloomFilter.setDownSamplingFactor(BLOOM_SAMPLING_FACTOR);
        bloomFilter.setBloomIntensity(BLOOM_INTENSITY);
        bloomFilter.setBlurScale(1f);
        postProcessor.addFilter(bloomFilter);
        viewPort.addProcessor(postProcessor);
        if (SSAO_ENABLED) {
            /** Ambient occlusion shadows */
            FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
            SSAOFilter ssaoFilter = new SSAOFilter(12.94f, 43.92f, 0.33f, 0.61f);
            fpp.addFilter(ssaoFilter);
            viewPort.addProcessor(fpp);


        }
        if (SHADOWS_ENABLED) {
            /** Advanced shadows for uneven surfaces */
            pssmShadowRenderer = new PssmShadowRenderer(assetManager, 1024, 3);
            pssmShadowRenderer.setDirection(new Vector3f(-.5f, -.5f, -.5f).normalizeLocal());
            pssmShadowRenderer.setFilterMode(PssmShadowRenderer.FilterMode.Bilinear);
            pssmShadowRenderer.setShadowIntensity(0.5f);
            pssmShadowRenderer.setEdgesThickness(-3);
            pssmShadowRenderer.setShadowZExtend(getCamera().getFrustumFar());
//            pssmShadowRenderer.setEdgesThickness(0);
//            pssmShadowRenderer.setCompareMode(PssmShadowRenderer.CompareMode.Hardware);
            viewPort.addProcessor(pssmShadowRenderer);
        }

    }

    /**
     * Initis the base-inputs for debugging.
     */
    private void initDebugInputs() {
        if (inputManager != null) {

            inputManager.clearMappings();
            flyCam.registerWithInput(inputManager);
            flyCam.setMoveSpeed(5f);

            if (context.getType() == Type.Display) {
                //inputManager.addMapping(INPUT_MAPPING_EXIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
            }

            inputManager.addMapping(INPUT_MAPPING_CAMERA_DEBUG, new KeyTrigger(KeyInput.KEY_F1));
            inputManager.addMapping(INPUT_MAPPING_CAMERA_POS, new KeyTrigger(KeyInput.KEY_F2));
            inputManager.addMapping(INPUT_MAPPING_HIDE_STATS, new KeyTrigger(KeyInput.KEY_F3));
            inputManager.addMapping(INPUT_MAPPING_MEMORY, new KeyTrigger(KeyInput.KEY_F4));
            inputManager.addListener(gameDebugActionListener,
                    INPUT_MAPPING_CAMERA_DEBUG, INPUT_MAPPING_EXIT,
                    INPUT_MAPPING_CAMERA_POS, INPUT_MAPPING_MEMORY, INPUT_MAPPING_HIDE_STATS);
        }
    }

    /* (non-Javadoc)
     * @see com.jme3.app.SimpleApplication#start()
     */
    @Override
    public void start() {
        super.start();

    }

    /* (non-Javadoc)
     * @see com.jme3.app.Application#stop()
     */
    @Override
    public void stop() {

        gamestateManager.terminate();
        super.stop();
    }

    /* (non-Javadoc)
     * @see com.jme3.app.Application#loseFocus()
     */
    @Override
    public void loseFocus() {
        wasPaused = true;
        super.loseFocus();
    }

    /* (non-Javadoc)
     * @see com.jme3.app.SimpleApplication#initialize()
     */
    @Override
    public void initialize() {
        // init Application
        super.initialize();
        // init isometric camera
        isoCameraControl = new IsoCameraControl(this);
        detachDebugCamera();
        setDisplayFps(showFps);
        setDisplayStatView(showFps);
        //init the Post-Processors
        initPostProcessors();
        // init input for debugging and camera
        initDebugInputs();
        // initialize gamestate manager and adds states, finally start the states
        initGamestates();
        // init games gui
        initGUI();
        // EventManager init
        eventManager = EventManager.getInstance();
        // ScreenRayCast3D
        rayCast3D = ScreenRayCast3D.getInstance();
        rayCast3D.initialize();

    }

    /**
     * Enables the debug camera.
     */
    private void attachDebugCamera() {
        flyCam.setEnabled(true);
        isoCameraControl.setEnabled(false);
        flyCam.setDragToRotate(true);
    }

    /**
     * Disables the debug camera.
     */
    private void detachDebugCamera() {
        isoCameraControl.reset();
        flyCam.setEnabled(false);
        isoCameraControl.setEnabled(true);
    }

    /* (non-Javadoc)
     * @see com.jme3.app.SimpleApplication#simpleInitApp()
     */
    @Override
    public void simpleInitApp() {
    }

    /* (non-Javadoc)
     * @see com.jme3.app.SimpleApplication#simpleUpdate(float)
     */
    @Override
    public void simpleUpdate(float tpf) {

        if (wasPaused) {
            tpf = 0;
            wasPaused = false;
        }

        for (AbstractAppState s : appStates.values()) {
            s.update(tpf);
        }

        if (!pause) {
            // frequently updates the games current state
            gamestateManager.update(tpf);
            // camera
            isoCameraControl.updateCamera(tpf);
            // events
            eventManager.update(tpf);
        }
    }

    /* (non-Javadoc)
     * @see com.jme3.app.SimpleApplication#simpleRender(com.jme3.renderer.RenderManager)
     */
    @Override
    public void simpleRender(RenderManager rm) {
    }

    /**
     * Initializes settings for the game, currently using settings from SolarWars.
     */
    public void initSettings() {
        if (settings == null) {
            settings = new AppSettings(true);
            settings.put("Width", 1024);
            settings.put("Height", 768);
            settings.put("BitsPerPixel", 24);
            settings.put("Frequency", 60);
            settings.put("DepthBits", 24);
            settings.put("StencilBits", 0);
            settings.put("Samples", 4);
            settings.put("Fullscreen", false);
            settings.put("Title", "Maze TD");
            settings.put("Renderer", AppSettings.LWJGL_OPENGL2);
            settings.put("AudioRenderer", AppSettings.LWJGL_OPENAL);
            settings.put("DisableJoysticks", true);
            settings.put("UseInput", true);
            settings.put("VSync", false);
            settings.put("FrameRate", 100);
            settings.put("SettingsDialogImage", "/Interface/Logo.png");
        }
    }

    /**
     * Gets the pssm shadow renderer.
     *
     * @return the pssm shadow renderer
     */
    public PssmShadowRenderer getPssmShadowRenderer() {
        return pssmShadowRenderer;
    }

    /**
     * Gets the iso camera control.
     *
     * @return the iso camera control
     */
    public IsoCameraControl getIsoCameraControl() {
        return isoCameraControl;
    }

    /**
     * Gets the hud screen instance.
     *
     * @return the hud screen instance
     */
    public HudScreen getHudScreenInstance() {
        return HudScreenInstance;
    }

    /**
     * Checks if is pause.
     *
     * @return true, if is pause
     */
    public boolean isPause() {
        return pause;
    }

    /**
     * Sets the pause.
     *
     * @param pause the new pause
     */
    public void setPause(boolean pause) {
        this.pause = pause;
    }
    //==========================================================================
    //===   Inner Classes
    //==========================================================================

    /**
     * The class GameActionListener listens to debug actions for the game.
     * Exiting, display FPS, memory shots, enable debug camera 
     * and camera position can be retrieved during runnig game.
     * @author Hans Ferchland
     * @version 0.12
     */
    private class GameDebugActionListener implements ActionListener {

        /* (non-Javadoc)
         * @see com.jme3.input.controls.ActionListener#onAction(java.lang.String, boolean, float)
         */
        @Override
        public void onAction(String name, boolean value, float tpf) {
            if (!value) {
                return;
            }
            if (name.equals(INPUT_MAPPING_CAMERA_DEBUG)) {
                if (flyCam.isEnabled()) {
                    detachDebugCamera();
                } else {
                    attachDebugCamera();
                }

            } else if (name.equals(INPUT_MAPPING_EXIT)) {
                stop();

            } else if (name.equals(INPUT_MAPPING_CAMERA_POS)) {
                if (cam != null) {
                    Vector3f loc = cam.getLocation();
                    Quaternion rot = cam.getRotation();
                    System.out.println("Camera Position: ("
                            + loc.x + ", " + loc.y + ", " + loc.z + ")");
                    System.out.println("Camera Rotation: " + rot);
                    System.out.println("Camera Direction: " + cam.getDirection());
                }

            } else if (name.equals(INPUT_MAPPING_MEMORY)) {
                BufferUtils.printCurrentDirectMemory(null);

            } else if (name.equals(INPUT_MAPPING_HIDE_STATS)) {
                showFps = !showFps;
                setDisplayFps(!showFps);
                setDisplayStatView(!showFps);
            }
        }
    }
}
