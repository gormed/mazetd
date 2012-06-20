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
 * File: Tower.java
 * Type: entities.Tower
 * 
 * Documentation created: 20.05.2012 - 21:41:55 by Ahmed Arous
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import entities.Orb;
import entities.Orb.ElementType;
import entities.Tower;

/**
 *The class "Inventory" handles the  towercontext and orbplacment between 
 * the tower instances and the hud screen.
 * @author Ahmed
 */
public class Inventory {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    private Player player = Player.getInstance();
    private Tower tower;
    private Orb.ElementType type;
    public static Inventory instance;

    //==========================================================================
    //===   Singleton
    //========================================================================== 
    public static Inventory getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new Inventory();
    }

    /**
     * constructor.
     */
    public Inventory() {
    }

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    /**
     * Checks the first slot of the selected tower.
     * @return null if slot is empty.
     *         else the contained orbtype.
     */
    public Orb.ElementType slot1Context() {
        tower = player.getSelectedTower();
        Orb orb1 = tower.getFirstOrb();

        if (orb1 == null) {
            return null;
        } else {
            return orb1.getElementType();
        }
    }

    /**
     * Checks the second slot of the selected tower.
     * @return null if slot is empty.
     *         else the contained orbtype.
     */
    public Orb.ElementType slot2Context() {
        tower = player.getSelectedTower();
        Orb orb2 = tower.getSecondOrb();

        if (orb2 == null) {
            return null;
        } else {
            ;
            return orb2.getElementType();
        }
    }

    /**
     * Checks the third slot of the selected tower.
     * @return null if slot is empty.
     *         else the contained orbtype.
     */
    public Orb.ElementType slot3Context() {
        tower = player.getSelectedTower();
        Orb orb3 = tower.getThirdOrb();

        if (orb3 == null) {
            return null;
        } else {
            return orb3.getElementType();
        }
    }

    /**
     * Replaces an already placed Orb with a new one or places an orb into 
     * an empty slot.
     * @param slot The Slot for the new orbType.
     * @param type The new Orbtype.
     * @return the remode orbtype.
     */
    public ElementType replaceOrb(int slot, ElementType type) {
        tower = player.getSelectedTower();
        ElementType removedType;
        switch (slot) {
            case 0:
                removedType = tower.replaceOrb(type, 0);
                break;
            case 1:
                removedType = tower.replaceOrb(type, 1);
                break;
            case 2:
            default:
                removedType = tower.replaceOrb(type, 2);
                break;
        }
        return removedType;

    }
}
