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
 * File: AbstractOrbEffect.java
 * Type: entities.effects.AbstractOrbEffect
 * 
 * Documentation created: 30.05.2012 - 15:22:01 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.effects;

import entities.Creep;
import entities.Orb;
import entities.Orb.ElementType;
import entities.Tower;

/**
 * The class AbstractOrbEffect for the effects creeps will suffer if 
 * hit by the given projectile. This class is abstract and will have childs like
 * PosionOrbEffect or so.
 *  
 * @author Hans Ferchland
 * @version 0.3
 */
public abstract class AbstractOrbEffect {

    private static AbstractOrbEffect createOrbEffect(ElementType type, int level) {
        switch (type) {
            case BLUE:
                return new FreezeOrbEffect(level);
            case GREEN:
                return new PoisonOrbEffect(level);
            case RED:

                return null;
            case WHITE:

                return new RangeOrbEffect(level);
            case YELLOW:
                return new SpeedOrbEffect(level);
            default:
                return null;
        }
    }

    public static AbstractOrbEffect getOrbEffect(ElementType type, int level) {
        return createOrbEffect(type, level);
    }
    public static final ElementType[] TOWER_ELEMENT_TYPES = {ElementType.WHITE, ElementType.YELLOW};

    public static boolean isTowerElement(ElementType elementType) {
        for (ElementType e : TOWER_ELEMENT_TYPES) {
            if (e.equals(elementType)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isCreepElement(ElementType elementType) {
        return !isTowerElement(elementType);
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    protected OrbEffectType effectType;
    protected Orb.ElementType elementType;
    protected int level;
    protected Creep infected;
    protected Tower enhanced;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Creates an AbstractOrbEffect by the effect type and the orbs level.
     * 
     * @see OrbEffectType
     * @param effectType the desired type
     * @param level the desired level
     */
    public AbstractOrbEffect(OrbEffectType effectType, ElementType elementType, int level) {
        this.elementType = elementType;
        this.effectType = effectType;
        this.level = level;
    }

    /**
     * Updated the orb effect over time if timed (like poison).
     * @param tpf 
     */
    public abstract void update(float tpf);

    /**
     * If its a timed effect this method should be filled with the desired 
     * behaviour and called on every tick the effect should happen 
     * (e.g. all 2 secs).
     */
    public abstract void onEffect();

    /**
     * Will be called if the projectile hits and the effect happens 
     * the first time. Usefull for one-time effects.
     */
    public void onStart(Creep c) {
        this.infected = c;
    }

    public void onStart(Tower t) {
        this.enhanced = t;
    }

    /**
     * Is called if the effect is removed.
     */
    public void onEnd(Creep c) {
        
    }

    public void onEnd(Tower t) {
    }

    public OrbEffectType getEffectType() {
        return effectType;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public int getLevel() {
        return level;
    }
}
