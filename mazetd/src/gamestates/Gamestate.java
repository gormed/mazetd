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
 * @version 0.1
 */
public abstract class Gamestate {

    /** The name. */
    private String name;

    /**
     * Instantiates a new gamestate.
     *
     * @param name the name
     */
    public Gamestate(String name) {
        this.name = name;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Enter.
     */
    void enter() {
        loadContent(MazeTDGame.getInstance());
    }

    /**
     * Leave.
     */
    void leave() {
        unloadContent();
    }

    /**
     * Pause.
     */
    public void pause() {
    }

    /**
     * Resume.
     */
    public void resume() {
    }

    /**
     * Reset.
     */
    public void reset() {
    }

    /**
     * Terminate.
     */
    public void terminate() {
    }

    /**
     * Updates the.
     *
     * @param tpf the tpf
     */
    public abstract void update(float tpf);

    /**
     * Load content.
     *
     * @param game the game
     */
    protected abstract void loadContent(MazeTDGame game);

    /**
     * Unload content.
     */
    protected abstract void unloadContent();
}