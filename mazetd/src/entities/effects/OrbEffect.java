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
 * File: OrbEffect.java
 * Type: entities.effects.OrbEffect
 * 
 * Documentation created: 30.05.2012 - 15:22:01 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.effects;

/**
 * The class OrbEffect for the effects creeps will suffer if 
 * hit by the given projectile. This class is abstract and will have childs like
 * PosionOrbEffect or so.
 *  
 * @author Hans Ferchland
 * @version 0.2
 */
public abstract class OrbEffect {
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private OrbEffectType effectType;
    private int level;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    /**
     * Creates an OrbEffect by the effect type and the orbs level.
     * 
     * @see OrbEffectType
     * @param effectType the desired type
     * @param level the desired level
     */
    public OrbEffect(OrbEffectType effectType, int level) {
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
    public abstract void onStart();
    
    /**
     * Should be called if the effect is removed.
     */
    public abstract void onEnd();
}
