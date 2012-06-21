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

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import entities.Creep;
import entities.Orb.ElementType;
import mazetd.MazeTDGame;

/**
 * The class DamageOrbEffect for creeps. This effect will do extra 
 * damage to the creep.
 * @author Hady Khalifa
 */
public class DamageOrbEffect extends AbstractOrbEffect {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /**
     * The damage done each level in percent of the maximum health of a creep.
     */
    private float[] damage = {0.05f, 0.075f, 0.15f};
    // Particle
    private ParticleEmitter fireEmitter;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Creates a DamageOrbEffect with a given level.
     * @param level the level of the effect
     */
    public DamageOrbEffect(int level) {
        super(OrbEffectType.FIRE, ElementType.RED, level);
        createFireEmitter(MazeTDGame.getInstance());
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void onEffect() {
    }

    @Override
    public void onStart(Creep c) {
        infected = c;
        // get maximum health
        float hp = infected.getMaxHealthPoints();
        // do damage in percent of the max hp
        infected.applyDamge(hp * damage[level]);
        // attach fire effect
        infected.getCollidableEntityNode().attachChild(fireEmitter);
        // start emitting
        emittFireParticles();
        // remove the effect after damage
        infected.removeOrbEffect(this);
    }

    @Override
    public void onEnd(Creep c) {
    }

    /**
     * Creates the emitter for fire particles.
     * @param game the mazetdgame reference
     */
    private void createFireEmitter(MazeTDGame game) {
        /** Uses Texture from jme3-test-data library! */
        fireEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 15);
        Material mat_red = new Material(game.getAssetManager(),
                "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", game.getAssetManager().loadTexture(
                "Textures/Effects/flame.png"));
        fireEmitter.setMaterial(mat_red);
        fireEmitter.setImagesX(2);
        fireEmitter.setImagesY(2); // 2x2 texture animation
        fireEmitter.setEndColor(new ColorRGBA(1f, 1f, 0f, 1f));   // red
        fireEmitter.setStartColor(new ColorRGBA(1f, 0f, 0f, 0.2f)); // yellow
        fireEmitter.getParticleInfluencer().setInitialVelocity(
                new Vector3f(0, 2, 1));
        fireEmitter.setRandomAngle(true);
        fireEmitter.setRotateSpeed(2);
        fireEmitter.setStartSize(0.6f);
        fireEmitter.setEndSize(0.2f);
        fireEmitter.setGravity(0f, 3f, 0f);
        fireEmitter.setLowLife(0.5f);
        fireEmitter.setHighLife(2f);
        fireEmitter.getParticleInfluencer().setVelocityVariation(0.25f);

    }

    /**
     * Emitts the fire particles on hit.
     */
    private void emittFireParticles() {
        fireEmitter.setParticlesPerSec(2);
        fireEmitter.emitAllParticles();
        fireEmitter.setParticlesPerSec(0);
    }
}
