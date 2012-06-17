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
 * The class Player
 * @author Ahmed
 * @version
 */
public class Player implements EntityListener, CreepListener {
    //==========================================================================
    //===   Enums
    //==========================================================================

    /**
     * The enum PlayerState describes the different states that exists for the player.
     */
    public enum PlayerState {

        PREPARING,
        PLAYING,
        LOST,
        WON
    }
    //==========================================================================
    //===   Constants
    //==========================================================================
    public static final int PLAYER_START_GOLD = 100;
    public static final int PLAYRER_LIVES = 5;
    //==========================================================================
    //===   Singleton
    //==========================================================================
    private static Player instance;

    public static Player getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new Player();
    }

    private Player() {
        inventory = new ArrayList<Orb.ElementType>();
        lives = maxLives;
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Tower selectedTower;
    private int redCount = 0;
    private int blueCount = 0;
    private int greenCount = 0;
    private int yellowCount = 0;
    private int whiteCount = 0;
    private int gold = 0;
    private int maxLives = PLAYRER_LIVES;
    private int lives = PLAYRER_LIVES;
    private Orb.ElementType type;
    private ArrayList<Orb.ElementType> inventory;
    private MazeTDGame game = MazeTDGame.getInstance();
    private boolean initialized = false;
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

        initialized = false;
    }

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
    public int getLives() {
        return lives;
    }

    public int getMaxLives() {
        return maxLives;
    }

    public int getLivesLeft() {
        return maxLives - lives;
    }

    public void setMaxLives(int lives) {
        this.maxLives = lives;
        this.lives = maxLives;
    }

    // Gold
    public void addGold(int gains) {
        gold = gold + gains;
    }

    public void chargeGold(int costs) {
        gold = gold - costs;
    }

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

    public Tower getSelectedTower() {
        return selectedTower;
    }

    public int getGold() {
        return gold;
    }

    public int getRedCount() {
        return redCount;
    }

    public int getBlueCount() {
        return blueCount;
    }

    public int getGreenCount() {
        return greenCount;
    }

    public int getYellowCount() {
        return yellowCount;
    }

    public int getWhiteCount() {
        return whiteCount;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setSelectedTower(Tower selectedTower) {
        this.selectedTower = selectedTower;
    }

    public boolean isPlayerAlive() {
        return lives > 0;
    }

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

    private void onDeath() {
        System.out.println("PLAYER HAS LOST!");
        playerState = PlayerState.LOST;
        game.getHudScreenInstance().pause("gameover");
    }

    private void onWin() {
        System.out.println("PLAYER HAS WON!");
        playerState = PlayerState.WON;
    }
}
