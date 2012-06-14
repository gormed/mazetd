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

    public static final int PLAYRER_HEALTH = 5;
    private static Player instance;
    private Tower selectedTower;
    private int redCount = 0;
    private int blueCount = 0;
    private int greenCount = 0;
    private int yellowCount = 0;
    private int whiteCount = 0;
    private int gold = 100;
    private int maxLives = PLAYRER_HEALTH;
    private int lives = PLAYRER_HEALTH;
    private Orb.ElementType type;
    private boolean towerIsClicked;
    private ArrayList<Orb> inventory;
    private MazeTDGame game=MazeTDGame.getInstance();

    public static Player getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new Player();
    }

    private Player() {
        inventory = new ArrayList<Orb>();
        lives = maxLives;
        EventManager.getInstance().addCreepListener(this, (Creep) null);
        EventManager.getInstance().addEntityListener(this, (AbstractEntity) null);
    }

    public void update(float tpf) {
        if (!isPlayerAlive()) {
            onDeath();
        }
    }

    // Live
    public int getLives() {
        return lives;
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

    public void buyOrb() {
        //TODO
    }

    public void removeOrb(Orb orb) {
        inventory.remove(orb);
        type = orb.getElementType();

        switch (type) {
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

    public void addOrb(Orb orb) {
        inventory.add(orb);
        type = orb.getElementType();

        switch (type) {
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
            case BLUE:
                if (blueCount > 0) {
                    return true;
                }
            case GREEN:
                if (greenCount > 0) {
                    return true;
                }
            case YELLOW:
                if (yellowCount > 0) {
                    return true;
                }
            case WHITE:
                if (whiteCount > 0) {
                    return true;
                }
            default:
                return false;
        }
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

    public boolean towerIsClicked() {
        return towerIsClicked;
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
            towerIsClicked = true;
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
        // TODO: Show GAME OVER hud
        System.out.println("PLAYER IS DEAD!");
        game.getHudScreenInstance().pause("gameover");
    }
}
