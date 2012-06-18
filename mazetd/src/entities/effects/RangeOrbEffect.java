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
 * File: RangeOrbEffect.java
 * Type: entities.effects.RangeOrbEffect
 * 
 * Documentation created: 05.06.2012 - 23:21:01 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.effects;

import entities.Orb.ElementType;
import entities.Tower;

/**
 * The class RangeOrbEffect.
 * @author Hans Ferchland
 */
public class RangeOrbEffect extends AbstractOrbEffect {

    private float[] range = { 2.5f , 3.5f, 5f };
    private float oldRange = 0;
    
    public RangeOrbEffect(int level) {
        super(OrbEffectType.RANGE, ElementType.WHITE, level);
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void onEffect() {
    }

    @Override
    public void onStart(Tower t) {
        super.onStart(t);
        oldRange = t.getTowerRange();
        t.setTowerRange(range[level]);
    }

    @Override
    public void onEnd(Tower t) {
        super.onEnd(t);
        t.setTowerRange(oldRange);
    }
}
