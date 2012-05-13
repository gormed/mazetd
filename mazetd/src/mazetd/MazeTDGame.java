package mazetd;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import gamestates.GamestateManager;
import gamestates.lib.MainmenuState;

/**
 * Main application class of MazeTD. 
 * 
 * Builds the jme game context, loads assetmanager, camera, 
 * scenegraph and many more.
 * @author Hans Ferchland
 * @version 0.1
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
    //===   Private Fields
    //==========================================================================
    private GamestateManager gamestateManager;
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
        //gamestateManager.addState(mainmenuState);
        gamestateManager.initialize(mainmenuState);
        gamestateManager.start();
    }

    @Override
    public void start() {
        super.start();
        
    }

    @Override
    public void initialize() {
        super.initialize();
        // initialize gamestate manager and adds states, finally start the states
        initGamestates();
        
    }
    @Override
    public void simpleInitApp() {

    }

    @Override
    public void simpleUpdate(float tpf) {
        // frequently updates the games current state
        gamestateManager.update(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

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
            settings.put("Title", "SolarWars_");
            settings.put("Renderer", AppSettings.LWJGL_OPENGL2);
            settings.put("AudioRenderer", AppSettings.LWJGL_OPENAL);
            settings.put("DisableJoysticks", true);
            settings.put("UseInput", true);
            settings.put("VSync", false);
            settings.put("FrameRate", 100);
            //settings.put("SettingsDialogImage", "/Interface/solarwars_v2.png");
        }
    }
}
