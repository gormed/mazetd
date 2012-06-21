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
 * File: OrbEffectManager.java
 * Type: entities.effects.OrbEffectManager
 * 
 * Documentation created: 13.06.2012 - 15:29:52 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.effects;

import entities.Orb;
import entities.Orb.ElementType;
import entities.Orb.SpecialElementType;
import java.util.ArrayList;

/**
 * The class OrbEffectManager that manages the creation of several effects 
 * including OrbEffects and SpecialOrbEffects. Handles special combinations 
 * of orbs; gets the effects and special-effects from all Orb 
 * combinations for towers.
 * 
 * @see ElementType
 * @see SpecialElementType
 * @see AbstractOrbEffect
 * @author Hans Ferchland
 */
public class OrbEffectManager {
    //==========================================================================
    //===   Static
    //==========================================================================

    /**
     * Defines the ElementTypes that are meant to be used for towers only 
     * and not on a creep.
     */
    public static final ElementType[] TOWER_ELEMENT_TYPES = {
        ElementType.WHITE,
        ElementType.YELLOW};

    /**
     * Creates an OrbEffect by a given ElementType and level 0..2
     * @param type the ElementType of the OrbEffect to create
     * @param level the level of the OrbEffect
     * @return the OrbEffect according to the given parameters
     */
    public static AbstractOrbEffect getOrbEffect(ElementType type, int level) {
        switch (type) {
            case BLUE:
                return new FreezeOrbEffect(level);
            case GREEN:
                return new PoisonOrbEffect(level);
            case RED:
                return new DamageOrbEffect(level);
            case WHITE:
                return new RangeOrbEffect(level);
            case YELLOW:
                return new SpeedOrbEffect(level);
            default:
                return null;
        }
    }

    /**
     * Creates a SpecialOrbEffect by a given SpecialElementType
     * @param type the SpecialElementType of the OrbEffect to create
     * @return the OrbEffect according to the given parameters
     */
    public static AbstractOrbEffect getSpecialOrbEffect(SpecialElementType type) {
        switch (type) {
            case MULTI:
                return null;
            case RASTA:
                return new RastaOrbEffect();

            case SPLASH:
            default:
                return null;
        }
    }

    /**
     * Checks if an ElementType is a TowerElement or not.
     * @param elementType the type to check for
     * @return true if ElementType is meant for tower, false otherwise
     */
    public static boolean isTowerElement(ElementType elementType) {
        for (ElementType e : TOWER_ELEMENT_TYPES) {
            if (e.equals(elementType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if an ElementType is a CreepElement or not.
     * @param elementType the type to check for
     * @return true if ElementType is meant for creeps, false otherwise
     */
    public static boolean isCreepElement(ElementType elementType) {
        return !isTowerElement(elementType);
    }
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of OrbEffectManager.
     */
    private OrbEffectManager() {
    }

    /**
     * The static method to retrive the one and only instance of OrbEffectManager.
     */
    public static OrbEffectManager getInstance() {
        return OrbEffectManagerHolder.INSTANCE;
    }

    /**
     * The holder-class OrbEffectManagerHolder for the OrbEffectManager.
     */
    private static class OrbEffectManagerHolder {

        private static final OrbEffectManager INSTANCE = new OrbEffectManager();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /**
     * Defines the combo for the rasta tower.
     */
    private static final ElementType[] rastOrbCombo = {ElementType.RED, ElementType.YELLOW, ElementType.GREEN};
    //==========================================================================
    //===   Methods
    //==========================================================================

    /** 
     * Checks all Orbs and gets the Effects in an array by level.
     * @return an array with OrbEffects by level
     */
    public AbstractOrbEffect[] getOrbEffects(Orb firstOrb, Orb secondOrb, Orb thirdOrb) {

        ElementType[] elements = new Orb.ElementType[3];
        int[] orbTypeCount = {0, 0, 0, 0, 0, 0};

        // check for all three orbs its types
        if (firstOrb != null) {
            elements[0] = firstOrb.getElementType();
        }
        if (secondOrb != null) {
            elements[1] = secondOrb.getElementType();
        }
        if (thirdOrb != null) {
            elements[2] = thirdOrb.getElementType();
        }

        // count each orb types
        for (Orb.ElementType e : elements) {
            if (e != null) {
                orbTypeCount[e.ordinal()]++;
            }
        }
        // look for special orb combines
        ArrayList<AbstractOrbEffect> effects = getSpecialEffectList(elements);

        // if no special effects found
        if (effects == null) {
            // get the normal effects
            effects = getEffectList(orbTypeCount);
        }
        AbstractOrbEffect[] effectArray = new AbstractOrbEffect[3];
        return effects.toArray(effectArray);
    }

    /**
     * Gets the list of special effects for a set of three ElementTypes.
     * @param elements the elemnts to get the effect from
     * @return the list of AbstractOrbEffect if there is one, null otherwise
     */
    private ArrayList<AbstractOrbEffect> getSpecialEffectList(ElementType[] elements) {
        ArrayList<AbstractOrbEffect> effects =
                new ArrayList<AbstractOrbEffect>();

        for (ElementType e : elements) {
            if (e == null) {
                return null;
            }
        }
        // Rasta Orb Effect
        boolean fits = true;
        for (int i = 0; i < 3; i++) {
            if (!elements[i].equals(rastOrbCombo[i])) {
                return null;
            }
        }

        if (fits) {
            effects.add(getSpecialOrbEffect(Orb.SpecialElementType.RASTA));
        }

        return effects;
    }

    /**
     * Gets the normal effect list from orb-type count in a tower.
     * @param orbTypeCount the count of the 5 diffrent orb types
     * @return the effects that are produced by the given orbs, 
     * empty list if no orbs
     */
    private ArrayList<AbstractOrbEffect> getEffectList(int[] orbTypeCount) {
        ArrayList<AbstractOrbEffect> effects =
                new ArrayList<AbstractOrbEffect>();
        for (int i = 0; i < orbTypeCount.length; i++) {
            if (orbTypeCount[i] > 0 && orbTypeCount[i] <= 3) {
                effects.add(
                        getOrbEffect(
                        Orb.ElementType.values()[i], orbTypeCount[i] - 1));
            }
        }
        return effects;
    }
}
