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

/**
 * The Class GamestateManager.
 * @author Hans Ferchland
 * @version 0.1
 */
public class GamestateManager {

    /** The Constant SINGLEPLAYER_STATE. */
    public static final String SINGLEPLAYER_STATE = "Singleplayer";
    /** The Constant MAINMENU_STATE. */
    public static final String MAINMENU_STATE = "Mainmenu";
    /** The Constant OPTIONS_STATE. */
    public static final String OPTIONS_STATE = "Options";
    /** The Constant TUTORIAL_STATE. */
    public static final String TUTORIAL_STATE = "Tutorial";
    /** The instance. */
    private static GamestateManager instance;
    /** The gamestates. */
    private HashMap<String, Gamestate> gamestates;
    /** The current state. */
    private Gamestate currentState;
    /** The next state. */
    private Gamestate nextState;
    /** The flag to lock the updateing of the current state */
    private static volatile boolean lockUpdate = false;

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

    /**
     * Initializes the.
     *
     * @param startState the start state
     */
    public void initialize(Gamestate startState) {
        addState(startState);
        currentState = startState;
    }

    /**
     * Start.
     */
    public void start() {
        currentState.enter();
        System.out.println(currentState.getName() + "...entered!");
    }

    /**
     * Enter state.
     *
     * @param nextState the next state
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
     * Adds the state.
     *
     * @param g the g
     */
    public void addState(Gamestate g) {
        gamestates.put(g.getName(), g);
        System.out.println("Gamestate: " + g.getName() + " added!");
    }

    /**
     * Removes the state.
     *
     * @param g the g
     */
    public void removeState(Gamestate g) {
        gamestates.remove(g.getName());
        System.out.println("Gamestate: " + g.getName() + " removed!");
    }

    /**
     * Gets the gamestate.
     *
     * @param name the name
     * @return the gamestate
     */
    public Gamestate getGamestate(String name) {
        if (!gamestates.containsKey(name)) {
            return null;
        }
        return gamestates.get(name);
    }
    
    public String getCurrentState() {
        if (currentState == null)
            return null;
        return currentState.getName();
    }

    /**
     * Updates the.
     *
     * @param tpf the tpf
     */
    public void update(float tpf) {
        if (currentState != null && !isLocked()) {
            currentState.update(tpf);
        }
    }

    /**
     * Pause.
     */
    public void pause() {
        if (currentState != null) {
            currentState.pause();
        }
    }

    /**
     * Resume.
     */
    public void resume() {
        if (currentState != null) {
            currentState.resume();
        }
    }

    /**
     * Reset.
     */
    public void reset() {
        if (currentState != null) {
            currentState.reset();
        }
    }

    /**
     * Terminate.
     */
    public void terminate() {
        if (currentState != null) {
            currentState.terminate();
        }
    }
}
