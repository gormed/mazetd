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
import gui.elements.MainmenuScreen;

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
    //==========================================================================
    //===   Singleton and Main-method
    //==========================================================================

    /** The single reference to the game*/
    private static MazeTDGame instance;

    /** The hidden contructor of the game*/
    private MazeTDGame() {
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
     * @param args 
     */
    public static void main(String[] args) {
        getInstance().initSettings();

        getInstance().start();
    }
    //==========================================================================
    //===   Static Fields
    //==========================================================================
    public static final String INPUT_MAPPING_CAMERA_DEBUG = "MAZETDGAME_CameraDebug";
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private GamestateManager gamestateManager;
    private ScreenRayCast3D rayCast3D;
    private EventManager eventManager;
    private IsoCameraControl isoCameraControl;
    private GameDebugActionListener gameDebugActionListener = new GameDebugActionListener();
    private boolean showFps = true;
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

    private void initGUI() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/screen.xml", "start", new MainmenuScreen("start"));
        guiViewPort.addProcessor(niftyDisplay);

    }

    /**
     * Initis the base-inputs for debugging
     */
    private void initDebugInputs() {
        if (inputManager != null) {

            inputManager.clearMappings();
            flyCam.registerWithInput(inputManager);
            flyCam.setMoveSpeed(5f);

            if (context.getType() == Type.Display) {
                inputManager.addMapping(INPUT_MAPPING_EXIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
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

    @Override
    public void start() {
        super.start();

    }

    @Override
    public void initialize() {
        // init Application
        super.initialize();
        // init isometric camera
        isoCameraControl = new IsoCameraControl(this);
        detachDebugCamera();
        // init input for debugging and camera
        initDebugInputs();

        // initialize gamestate manager and adds states, finally start the states
        initGamestates();
        
        initGUI();
        // EventManager init
        eventManager = EventManager.getInstance();
        // ScreenRayCast3D
        rayCast3D = ScreenRayCast3D.getInstance();

    }

    /** Enables the debug camera */
    private void attachDebugCamera() {
        flyCam.setEnabled(true);
        isoCameraControl.setEnabled(false);
        flyCam.setDragToRotate(true);
    }

    /** Disables the debug camera */
    private void detachDebugCamera() {
        isoCameraControl.reset();
        flyCam.setEnabled(false);
        isoCameraControl.setEnabled(true);
    }

    @Override
    public void simpleInitApp() {
    }

    @Override
    public void simpleUpdate(float tpf) {
        // frequently updates the games current state
        gamestateManager.update(tpf);
        // camera
        isoCameraControl.updateCamera(tpf);
        // raycaster
        rayCast3D.update(tpf);
        // events
        eventManager.update(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    /**
     * Initializes settings for the game, currently using settings from SolarWars
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
            //settings.put("SettingsDialogImage", "/Interface/solarwars_v2.png");
        }
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

        public void onAction(String name, boolean value, float tpf) {
            if (!value) {
                return;
            }
            switch (name) {
                case INPUT_MAPPING_CAMERA_DEBUG:
                    if (flyCam.isEnabled()) {
                        detachDebugCamera();
                    } else {
                        attachDebugCamera();
                    }
                    break;
                case INPUT_MAPPING_EXIT:
                    stop();
                    break;
                case INPUT_MAPPING_CAMERA_POS:
                    if (cam != null) {
                        Vector3f loc = cam.getLocation();
                        Quaternion rot = cam.getRotation();
                        System.out.println("Camera Position: ("
                                + loc.x + ", " + loc.y + ", " + loc.z + ")");
                        System.out.println("Camera Rotation: " + rot);
                        System.out.println("Camera Direction: " + cam.getDirection());
                    }
                    break;
                case INPUT_MAPPING_MEMORY:
                    BufferUtils.printCurrentDirectMemory(null);
                    break;
                case INPUT_MAPPING_HIDE_STATS:
                    showFps = !showFps;
                    setDisplayFps(!showFps);
                    setDisplayStatView(!showFps);
                    break;
                default:
                    return;
            }


        }
    }
}
