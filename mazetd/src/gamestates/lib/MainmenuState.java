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
 * File: MainmenuState.java
 * Type: gamestates.lib.MainmenuState
 * 
 * Documentation created: 13.05.2012 - 23:13:37 by Hans
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gamestates.lib;

import gamestates.Gamestate;
import gamestates.GamestateManager;
import mazetd.MazeTDGame;

/**
 * Mainmenu state represets the main menu with all its content, interactions and updates.
 * @author Hans Ferchland
 * @version 0.1
 */
public class MainmenuState extends Gamestate {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    /** The game. */
    private MazeTDGame game;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Instanziates a new mainmeu state.
     */
    public MainmenuState() {
        super(GamestateManager.MAINMENU_STATE);
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#loadContent(mazetd.MazeTDGame)
     */
    @Override
    protected void loadContent(MazeTDGame game) {
        this.game = game;
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {
    }
}
