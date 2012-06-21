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
 * File: SpeedOrbEffect.java
 * Type: entities.effects.SpeedOrbEffect
 * 
 * Documentation created: 12.06.2012 - 00:50:39 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.effects;

import entities.Orb.ElementType;
import entities.Tower;

/**
 * The class SpeedOrbEffect is the effect of the yellow orb, that increases
 * attack rate of a tower.
 * @author Hans Ferchland
 * @version 0.2
 */
public class SpeedOrbEffect extends AbstractOrbEffect {


    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /**
     * The three diffrent rates for each level.
     */
    private float[] attackRate = { 1.05f, 0.8f, 0.55f };
    
    /** The old attack rate. */
    private float oldAttackRate = 0;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Creates a SpeedOrbEffect with a given level.
     * @param level the level of the effect
     */
    public SpeedOrbEffect(int level) {
        super(OrbEffectType.RATE, ElementType.YELLOW, level);
    }
    
    /* (non-Javadoc)
     * @see entities.effects.AbstractOrbEffect#update(float)
     */
    @Override
    public void update(float tpf) {
        
    }

    /* (non-Javadoc)
     * @see entities.effects.AbstractOrbEffect#onEffect()
     */
    @Override
    public void onEffect() {
        
    }

    /* (non-Javadoc)
     * @see entities.effects.AbstractOrbEffect#onStart(entities.Tower)
     */
    @Override
    public void onStart(Tower t) {
        super.onStart(t);
        // save old attack rate
        oldAttackRate = t.getAttackRate();
        // apply new rate
        t.setAttackRate(attackRate[level]);
    }

    /* (non-Javadoc)
     * @see entities.effects.AbstractOrbEffect#onEnd(entities.Tower)
     */
    @Override
    public void onEnd(Tower t) {
        // reset attack rate on end
        t.setAttackRate(oldAttackRate);
        super.onEnd(t);
    }
}
