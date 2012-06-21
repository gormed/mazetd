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
 * File: Player.java
 * Type: logic.Player
 * 
 * Documentation created: 13.05.2012 - 23:13:37 by Hans
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import java.util.ArrayList;
import entities.Orb;
import entities.Tower;
import entities.base.AbstractEntity;
import eventsystem.EventManager;
import eventsystem.events.EntityEvent;
import eventsystem.events.EntityEvent.EntityEventType;
import eventsystem.listener.EntityListener;
import entities.Creep;
import eventsystem.events.CreepEvent;
import eventsystem.events.CreepEvent.CreepEventType;
import eventsystem.listener.CreepListener;
import mazetd.MazeTDGame;

/**
 * The class "Player" represents the single player of the MazeTD game.
 * A player can select a Tower,owns gold and has a List of Elementtypes.
 * Beyond that a player can take in diffrent States in the game.
 *
 * @author Ahmed Arous
 */
public class Player implements EntityListener, CreepListener {
    //==========================================================================
    //===   Enums
    //==========================================================================

    /**
     * The enum PlayerState describes the different states that exists for the 
     * player.
     */
    public enum PlayerState {

        /** The PREPARING. */
        PREPARING,
        
        /** The PLAYING. */
        PLAYING,
        
        /** The LOST. */
        LOST,
        
        /** The WON. */
        WON
    }
    //==========================================================================
    //===   Constants
    //==========================================================================
    /**
     * The initial gold the player starts with.
     */
    public static final int PLAYER_START_GOLD = 100;
    /**
     * The initial live the player starts with.
     */
    public static final int PLAYRER_LIVES = 15;
    //==========================================================================
    //===   Singleton
    //==========================================================================
    /** The instance. */
    private static Player instance;

    /**
     * Gets the single reference to the player.
     * @return The Player reference
     */
    public static Player getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new Player();
    }

    /**
     * Instantiates a new player.
     */
    private Player() {
        inventory = new ArrayList<Orb.ElementType>();
        lives = maxLives;
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /** The selected tower. */
    private Tower selectedTower;
    
    /** The red count. */
    private int redCount = 0;
    
    /** The blue count. */
    private int blueCount = 0;
    
    /** The green count. */
    private int greenCount = 0;
    
    /** The yellow count. */
    private int yellowCount = 0;
    
    /** The white count. */
    private int whiteCount = 0;
    
    /** The gold. */
    private int gold = 0;
    
    /** The max lives. */
    private int maxLives = PLAYRER_LIVES;
    
    /** The lives. */
    private int lives = PLAYRER_LIVES;
    
    /** The type. */
    private Orb.ElementType type;
    
    /** The inventory. */
    private ArrayList<Orb.ElementType> inventory;
    
    /** The game. */
    private MazeTDGame game = MazeTDGame.getInstance();
    
    /** The initialized. */
    private boolean initialized = false;
    
    /** The player state. */
    private PlayerState playerState = PlayerState.PREPARING;

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    /**
     * Initializes the player singleton for the first time or after 
     * destroy was called.
     */
    void initialize() {
        if (initialized) {
            return;
        }
        // init gold
        gold = PLAYER_START_GOLD;
        // init lives
        lives = maxLives;
        //reset player state
        playerState = PlayerState.PLAYING;
        // add listeners
        EventManager.getInstance().
                addCreepListener(this, (Creep) null);
        EventManager.getInstance().
                addEntityListener(this, (AbstractEntity) null);

        initialized = true;
    }

    /**
     * Resets the player-singleton.
     */
    void destroy() {
        if (!initialized) {
            return;
        }
        // remove listeners
        EventManager.getInstance().
                removeCreepListener(this);
        EventManager.getInstance().
                removeEntityListener(this);
        // clear inventory
        inventory.clear();
        // reset orb counter
        redCount = 0;
        blueCount = 0;
        greenCount = 0;
        yellowCount = 0;
        whiteCount = 0;
        //reset selectedTower
        selectedTower=null;
        //reset player state
        playerState = PlayerState.PREPARING;

        initialized = false;
    }

    /**
     * Update.
     *
     * @param tpf the tpf
     */
    public void update(float tpf) {
        if (!initialized
                || playerState == PlayerState.LOST
                || playerState == PlayerState.WON) {
            return;
        }
        if (!isPlayerAlive()) {
            onDeath();
        }
        if (WaveManager.getInstance().isCompleted()) {
            onWin();
        }
    }

    // Live
    /**
     * Gets the current live of the Player.
     * @return The current live.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Gets the maximal live of the Player.
     * @return The maximal live.
     */
    public int getMaxLives() {
        return maxLives;
    }

    /**
     * Sets the new Lives of a player, both max and current lives to the new value.
     * @param lives the new Lives of the player
     */
    public void setMaxLives(int lives) {
        this.maxLives = lives;
        this.lives = maxLives;
    }

    // Gold
    /**
     * Adds an amount of gold to the player.
     * @param gains the gold that will be added to the player
     */
    public void addGold(int gains) {
        gold = gold + gains;
    }

    /**
     * Removes an amount of gold from the player.
     * @param costs the gold that will be removed from the player
     */
    public void chargeGold(int costs) {
        gold = gold - costs;
    }

    /**
     * Removes an Elementtype from the player inventory.
     * @param orbType the Elementtype to be removed.
     */
    public void removeOrb(Orb.ElementType orbType) {
        inventory.remove(orbType);

        switch (orbType) {
            case RED:
                redCount = redCount - 1;
                break;
            case BLUE:
                blueCount = blueCount - 1;
                break;
            case GREEN:
                greenCount = greenCount - 1;
                break;
            case YELLOW:
                yellowCount = yellowCount - 1;
                break;
            case WHITE:
                whiteCount = whiteCount - 1;
                break;
            default:
                break;
        }
    }

    /**
     * Adds an Elementtype to the player inventory.
     * @param orbType the Elementtype to be added.
     */
    public void addOrb(Orb.ElementType orbType) {
        inventory.add(orbType);

        switch (orbType) {
            case RED:
                redCount = redCount + 1;
                break;
            case BLUE:
                blueCount = blueCount + 1;
                break;
            case GREEN:
                greenCount = greenCount + 1;
                break;
            case YELLOW:
                yellowCount = yellowCount + 1;
                break;
            case WHITE:
                whiteCount = whiteCount + 1;
                break;
            default:
                break;
        }
    }

    /**
     * Checks if the player owns a certain Elementtype.
     * @param type the Elementtype that is searched for.
     * @return {@code true} if the player owns the type
     * {@code false} else
     */
    public boolean hasType(Orb.ElementType type) {

        switch (type) {
            case RED:
                if (redCount > 0) {
                    return true;
                }
                break;
            case BLUE:
                if (blueCount > 0) {
                    return true;
                }
                break;
            case GREEN:
                if (greenCount > 0) {
                    return true;
                }
                break;
            case YELLOW:
                if (yellowCount > 0) {
                    return true;
                }
                break;
            case WHITE:
                if (whiteCount > 0) {
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * Gets the currently selected Tower by the player.
     * @return The selected tower.
     */
    public Tower getSelectedTower() {
        return selectedTower;
    }

    /**
     * Gets the gold owned by the player.
     * @return gold.
     */
    public int getGold() {
        return gold;
    }

    /**
     * Gets the amount of red Elementtypes the player owns.
     * @return The amount of red Elementtypes.
     */
    public int getRedCount() {
        return redCount;
    }

    /**
     * Gets the amount of blue Elementtypes the player owns.
     * @return The amount of blue Elementtypes.
     */
    public int getBlueCount() {
        return blueCount;
    }

    /**
     * Gets the amount of green Elementtypes the player owns.
     * @return The amount of green Elementtypes.
     */
    public int getGreenCount() {
        return greenCount;
    }

    /**
     * Gets the amount of yellow Elementtypes the player owns.
     * @return The amount of yellow Elementtypes.
     */
    public int getYellowCount() {
        return yellowCount;
    }

    /**
     * Gets the amount of white Elementtypes the player owns.
     * @return The amount of white Elementtypes.
     */
    public int getWhiteCount() {
        return whiteCount;
    }

    /**
     * Gets the current Playerstate of the player.
     * @return The playerstate.
     */
    public PlayerState getPlayerState() {
        return playerState;
    }

    /**
     * Sets the selected Tower of the player.
     * @param selectedTower the new selectedTower
     */
    public void setSelectedTower(Tower selectedTower) {
        this.selectedTower = selectedTower;
    }
  /**
     * Checks if the player is alive.
     * @return {@code true} if the player is alive
     * {@code false} else
     */
    public boolean isPlayerAlive() {
        return lives > 0;
    }

    /* (non-Javadoc)
     * @see eventsystem.listener.EntityListener#onAction(eventsystem.events.EntityEvent)
     */
    @Override
    public void onAction(EntityEvent entityEvent) {
        // get the entity
        AbstractEntity e = entityEvent.getEntity();
        // check if tower an clicked
        if (e instanceof Tower
                && entityEvent.getEventType().equals(EntityEventType.Click)) {
            // cast to tower
            Tower tower = (Tower) e;
            selectedTower = tower;
            Tower.TowerSelection.getInstance().attachToTower(selectedTower);
            System.out.println("Tower clicked:" + tower.getName());

        }
    }

    /* (non-Javadoc)
     * @see eventsystem.listener.CreepListener#onAction(eventsystem.events.CreepEvent)
     */
    @Override
    public void onAction(CreepEvent e) {
        if (e.getType().equals(CreepEventType.ReachedEnd)) {
            Creep c = e.getCreep();
            lives--;

            System.out.println("Lives: " + lives + "/left: " + (maxLives - lives));

        } else if (e.getType().equals(CreepEventType.Death)) {
            Creep c = e.getCreep();
            addGold(c.getGoldDrop());
        }
    }
    /**
     * is Triggered when the player dies.
     * Changes playerstate to Lost.
     * Pauses the game and changes to Game Over Screen.
     */
    private void onDeath() {
        System.out.println("PLAYER HAS LOST!");
        playerState = PlayerState.LOST;
        game.getHudScreenInstance().pause("gameover");
    }
    /**
     * is Triggered when the player wins.
     * Changes playerstate to Won.
     * Pauses the game and changes to Winning Screen.
     */
    private void onWin() {
        System.out.println("PLAYER HAS WON!");
        playerState = PlayerState.WON;
        game.getHudScreenInstance().pause("gamewon");
    }
}
