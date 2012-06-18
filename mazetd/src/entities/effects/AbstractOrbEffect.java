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
import entities.Orb.SpecialElementType;
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

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    protected OrbEffectType effectType;
    protected Orb.ElementType elementType;
    protected Orb.SpecialElementType specialElementType;
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
     * Creates an AbstractOrbEffect by the effect type and the orbs level.
     * 
     * @see OrbEffectType
     * @param specialElementType the desired type
     * @param level the desired level
     */
    public AbstractOrbEffect(OrbEffectType effectType, SpecialElementType specialElementType, int level) {
        this.specialElementType = specialElementType;
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
     * Will be called if the effect is added to the creep the first time, 
     * and the projectile hits. Usefull for one-time effects.
     * @param creep the creep that suffers this effect currently the first time
     */
    public void onStart(Creep creep) {
        this.infected = creep;
    }

    /**
     * Will be called if the effect is added to the tower the first time.
     * @param tower the tower that gets this effect for the first time
     */
    public void onStart(Tower tower) {
        this.enhanced = tower;
    }

    /**
     * Is called if the effect is removed.
     * @param creep the creep where the effect is removed
     */
    public void onEnd(Creep creep) {
    }

    /**
     * Is called if the effect is removed.
     * @param tower the tower where the effect is removed
     */
    public void onEnd(Tower tower) {
    }

    /**
     * Gets the OrbEffectType of this OrbEffect.
     * Basicly this is the type of effect.
     * @return the type of effect
     */
    public OrbEffectType getEffectType() {
        return effectType;
    }

    /**
     * Gets the ElementType of this orb effect.
     * Basicly this is the type of orb of this effect.
     * @return the type of element
     */
    public ElementType getElementType() {
        return elementType;
    }

    /**
     * The level this OrbEffect has.
     * @return the level from 0..2
     */
    public int getLevel() {
        return level;
    }
}
