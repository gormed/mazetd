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
 * The class OrbEffect.
 * @author Hans Ferchland
 * @version
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
    public OrbEffect(OrbEffectType effectType, int level) {
        this.effectType = effectType;
        this.level = level;
    }
    
    public abstract void update(float tpf);
    
    public abstract void onEffect();
    
    public abstract void onStart();
    
    public abstract void onEnd();
}
