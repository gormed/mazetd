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
 * File: Gamestate.java
 * Type: gamestates.Gamestate
 * 
 * Documentation created: 13.05.2012 - 23:13:37 by Hans
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gamestates;

import mazetd.MazeTDGame;

/**
 * The Class Gamestate as a base for all states of the game.
 * @author Hans Ferchland
 * @version 0.2
 */
public abstract class Gamestate {

    /** The name of the gamestate. */
    private String name;

    /**
     * Instantiates a new gamestate.
     *
     * @param name the name to use
     */
    public Gamestate(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the gamestate.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * This method is called on starting the state so it can load resources.
     */
    void enter() {
        loadContent(MazeTDGame.getInstance());
    }

    /**
     * This method is called on exiting the state so it can unload resources.
     */
    void leave() {
        unloadContent();
    }

    
    /**
     * Pause is called by the GamestateManager if the gamestate is 
     * currently active so it could stop updating.
     */
    public void pause() {
    }

    /**
     * Resume is called by the GamestateManager if the gamestate is 
     * currently active and was paused so it can start updating again.
     */
    public void resume() {
    }

    /**
     * Reset is called by the GamestateManager so the state could reset itself
     * to its initial state.
     */
    public void reset() {
    }

    /**
     * Terminate is called by the GamestateManager so the gamestate can free or
     * save resources on a quick exit.
     */
    public void terminate() {
    }

    /**
     * Updates the gamestate, this is called by the GamestateManager on 
     * every call with the given time-gap as parameter.
     *
     * @param tpf the time gap
     */
    public abstract void update(float tpf);

    /**
     * Loads the content of this state, need to be implemented for each child-
     * gamestate; called by the GamestateManager.
     *
     * @param game the mazetdgame to load resources easier
     */
    protected abstract void loadContent(MazeTDGame game);

    /**
     * Unloads the states content on exit, called by the GamestateManager.
     */
    protected abstract void unloadContent();
}
