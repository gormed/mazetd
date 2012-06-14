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
import java.util.ArrayList;

/**
 *
 * @author Hans Ferchland
 */
public class OrbEffectManager {
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
            effects.add(AbstractOrbEffect.getSpecialOrbEffect(
                    Orb.SpecialElementType.RASTA, 0));
        }

        return effects;
    }

    private ArrayList<AbstractOrbEffect> getEffectList(int[] orbTypeCount) {
        ArrayList<AbstractOrbEffect> effects =
                new ArrayList<AbstractOrbEffect>();
        for (int i = 0; i < orbTypeCount.length; i++) {
            if (orbTypeCount[i] > 0 && orbTypeCount[i] <= 3) {
                effects.add(
                        AbstractOrbEffect.getOrbEffect(
                        Orb.ElementType.values()[i], orbTypeCount[i] - 1));
            }
        }
        return effects;
    }
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
