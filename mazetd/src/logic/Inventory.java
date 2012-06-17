/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import entities.Orb;
import entities.Orb.ElementType;
import entities.Tower;

/**
 *
 * @author Ahmed
 */
public class Inventory {

    private Player player = Player.getInstance();
    private Tower tower;
    private Orb.ElementType type;
    public static Inventory instance;

    public Inventory() {
    }

    public static Inventory getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new Inventory();
    }

    public Orb.ElementType slot1Context() {
        tower = player.getSelectedTower();
        Orb orb1 = tower.getFirstOrb();

        if (orb1 == null) {
            return null;
        } else {
            return orb1.getElementType();
        }
    }

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

    public Orb.ElementType slot3Context() {
        tower = player.getSelectedTower();
        Orb orb3 = tower.getThirdOrb();

        if (orb3 == null) {
            return null;
        } else {
            return orb3.getElementType();
        }
    }

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
