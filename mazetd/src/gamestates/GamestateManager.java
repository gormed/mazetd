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
 * File: GamestateManager.java
 * Type: gamestates.GamestateManager
 * 
 * Documentation created: 13.05.2012 - 23:13:37 by Hans
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gamestates;

import java.util.HashMap;
import mazetd.MazeTDGame;

/**
 * The Class GamestateManager.
 * @author Hans Ferchland
 * @version 0.1
 */
public class GamestateManager {
    //==========================================================================
    //===   Static Fields
    //==========================================================================

    /** The Constant SINGLEPLAYER_STATE. */
    public static final String SINGLEPLAYER_STATE = "Singleplayer";
    /** The Constant MAINMENU_STATE. */
    public static final String MAINMENU_STATE = "Mainmenu";
    /** The Constant OPTIONS_STATE. */
    public static final String OPTIONS_STATE = "Options";
    /** The Constant TUTORIAL_STATE. */
    public static final String TUTORIAL_STATE = "Tutorial";
    /** The instance of the gamestate manager. */
    private static GamestateManager instance;
    /** The flag to lock the updateing of the current state */
    private static volatile boolean lockUpdate = false;
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /** The gamestates to hold. */
    private HashMap<String, Gamestate> gamestates;
    /** The current state, that will be updated. */
    private Gamestate currentState;
    /** The next state if there is a next state to enter. */
    private Gamestate nextState;

    //==========================================================================
    //===   Singleton
    //==========================================================================
    /** Retrieves the current state of locking */
    public static synchronized boolean isLocked() {
        return lockUpdate;
    }

    /** Locks the current state from updating */
    static synchronized void lock() {
        lockUpdate = true;
    }

    /** Unlocks the current state from updating */
    static synchronized void unlock() {
        lockUpdate = false;
    }

    /**
     * Instantiates a new gamestate manager.
     */
    private GamestateManager() {
        gamestates = new HashMap<String, Gamestate>();
    }

    /**
     * Gets the single instance of GamestateManager.
     *
     * @return single instance of GamestateManager
     */
    public static GamestateManager getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new GamestateManager();
    }
    //==========================================================================
    //===   Methods
    //==========================================================================

    /**
     * Initializes the GamestateManager for the first time.
     *
     * @param startState the start state
     */
    public void initialize(Gamestate startState) {
        addState(startState);
        currentState = startState;
    }

    /**
     * Start the GamestateManager with the initial state.
     */
    public void start() {
        currentState.enter();
        System.out.println(currentState.getName() + "...entered!");
    }

    /**
     * Enter the next gamestate, uload the old one and load the new one.
     *
     * @param nextState the next state to enter
     */
    public synchronized void enterState(String nextState) {
        lock();
        if (gamestates.containsKey(nextState)) {
            this.nextState = gamestates.get(nextState);

            this.currentState.leave();
            this.currentState = null;
            this.nextState.enter();

            this.currentState = this.nextState;
            System.out.println("Gamestate: " + nextState + " entered!");
        }
        unlock();
    }

    /**
     * Adds a Gamestate to the GamestateManager so it can be entered with 
     * the name of the gamestate.
     *
     * @param g the gamestate to add
     */
    public void addState(Gamestate g) {
        gamestates.put(g.getName(), g);
        System.out.println("Gamestate: " + g.getName() + " added!");
    }

    /**
     * Removes a Gamestate from the GamestateManager so it can't be entered 
     * anymore.
     *
     * @param g the gamestate to remove
     */
    public void removeState(Gamestate g) {
        gamestates.remove(g.getName());
        System.out.println("Gamestate: " + g.getName() + " removed!");
    }

    /**
     * Gets a gamestate by the name.
     *
     * @param name the name of the state to get
     * @return the gamestate if actually in the list of gamestates (was added 
     * via addState()), null otherwise
     */
    public Gamestate getGamestate(String name) {
        if (!gamestates.containsKey(name)) {
            return null;
        }
        return gamestates.get(name);
    }

    /**
     * Gets the currently set gamestates name.
     * @return the name of the currently active gamestate
     */
    public String getCurrentState() {
        if (currentState == null) {
            return null;
        }
        return currentState.getName();
    }

    /**
     * Updates the GamestateManger, means the currently active gamestate.
     *
     * @param tpf the tpf
     */
    public void update(float tpf) {
        if (currentState != null && !isLocked()) {
            currentState.update(tpf);
        }
    }

    /**
     * Pauses the updateing of the GamestateManager and the 
     * whole application/game.
     */
    public void pause() {
        if (currentState != null) {
            currentState.pause();
            MazeTDGame.getInstance().setPause(true);
        }
    }

    /**
     * Resumes the updateing of the GamestateManager and unpauses the 
     * application/game.
     */
    public void resume() {
        if (currentState != null) {
            currentState.resume();
            MazeTDGame.getInstance().setPause(false);
        }
    }

    /**
     * Resets the currently active gamestate if implemented for the state.
     */
    public void reset() {
        if (currentState != null) {
            currentState.reset();
        }
    }

    /**
     * Terminates the currently active gamestate so it can unload resources on
     * exit.
     */
    public void terminate() {
        if (currentState != null) {
            currentState.terminate();
        }
    }
}
