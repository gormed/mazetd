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
 * File: OrbEffectType.java
 * Type: entities.effects.OrbEffectType
 * 
 * Documentation created: 30.05.2012 - 15:26:07 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.effects;

import entities.Orb;

/**
 * The enum OrbEffectType for the types of the effect the different orb-elements
 * have.
 * 
 * @see Orb
 * @see OrbEffectManager
 * @author Hans Ferchland
 * @version 0.2
 */
public enum OrbEffectType {

    /** Posion effect for orbs, does damage over time. */
    POISON,
    
    /** Frost effect for orbs, slows the target. */
    FROST,
    
    /** Fire effect for orbs, does additional damage. */
    FIRE,
    
    /** Range effect for orbs, increases the range of a tower. */
    RANGE,
    
    /** Attack rate effect for orbs, increases the attack-rate of the tower. */
    RATE,
    
    /** Attack rate effect for orbs, increases the attack-rate of the tower. */
    RASTA
}
