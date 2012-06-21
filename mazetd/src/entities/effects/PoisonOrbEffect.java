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
 * File: PoisonOrbEffect.java
 * Type: entities.effects.PoisonOrbEffect
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
 * The class PoisonOrbEffect is a Creep effect. This effect will damage the 
 * creep over time
 * @author Hans Ferchland
 * @version 0.2
 */
public class PoisonOrbEffect extends AbstractOrbEffect {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /**
     * Percentual damage to apply to the creep each level.
     */
    private float[] damage = {0.01f, 0.0125f, 0.02f};
    /**
     * Period of the effect for each level.
     */
    private float[] damagePeriod = {0.4f, 0.4f, 0.4f};
    /**
     * Effect-duaration for each level.
     */
    private float[] duration = {3, 4, 5};
    
    /** The damage counter. */
    private float damageCounter = 0;
    
    /** The duration counter. */
    private float durationCounter = 0;
    
    /** The decaying. */
    private boolean decaying = false;
    // Particle
    /** The poison emitter. */
    private ParticleEmitter poisonEmitter;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Creates a PoisonOrbEffect with a given level.
     *
     * @param level the level of the effect
     */
    public PoisonOrbEffect(int level) {
        super(OrbEffectType.POISON, ElementType.GREEN, level);
        createPoisonEmitter(MazeTDGame.getInstance());
    }

    /* (non-Javadoc)
     * @see entities.effects.AbstractOrbEffect#update(float)
     */
    @Override
    public void update(float tpf) {
        damageCounter += tpf;
        durationCounter += tpf;

        if (decaying) {
            if (poisonEmitter.getNumVisibleParticles() == 0) {
                infected.getCollidableEntityNode().detachChild(poisonEmitter);
            }
        } else {
            if (durationCounter > duration[level]) {
                infected.removeOrbEffect(this);
                decaying = true;
            }
            if (damageCounter > damagePeriod[level]) {
                onEffect();
                damageCounter = 0;
            }
        }
    }

    /* (non-Javadoc)
     * @see entities.effects.AbstractOrbEffect#onEffect()
     */
    @Override
    public void onEffect() {
        // emitt particles
        emittPoisonParticles();
        // apply percentage damage
        float hp = infected.getMaxHealthPoints();
        infected.applyDamge(hp * damage[level]);
    }

    /* (non-Javadoc)
     * @see entities.effects.AbstractOrbEffect#onStart(entities.Creep)
     */
    @Override
    public void onStart(Creep c) {
        infected = c;
        infected.getCollidableEntityNode().attachChild(poisonEmitter);
    }

    /* (non-Javadoc)
     * @see entities.effects.AbstractOrbEffect#onEnd(entities.Creep)
     */
    @Override
    public void onEnd(Creep c) {
    }

    /**
     * Creates a particle emitter for the effect visuals.
     *
     * @param game the mazetdgame ref
     */
    private void createPoisonEmitter(MazeTDGame game) {
        /** Uses Texture from jme3-test-data library! */
        poisonEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 3);
        Material mat_red = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture",
                game.getAssetManager().loadTexture("Textures/Effects/PoisonParticle.png"));
        poisonEmitter.setMaterial(mat_red);
        poisonEmitter.setImagesX(1);
        poisonEmitter.setImagesY(1);
        poisonEmitter.setEndColor(new ColorRGBA(0f, 1f, 0f, 1f));
        poisonEmitter.setStartColor(new ColorRGBA(0.5f, 1f, 0f, 0.0f));
        poisonEmitter.getParticleInfluencer().setInitialVelocity(
                new Vector3f(0, 1, 0));
        poisonEmitter.setStartSize(0.025f);
        poisonEmitter.setEndSize(0.075f);
        poisonEmitter.setGravity(0f, -2f, 1f);
        poisonEmitter.setLowLife(0.6f);
        poisonEmitter.setHighLife(1.0f);
        poisonEmitter.getParticleInfluencer().setVelocityVariation(0.25f);
        poisonEmitter.preload(game.getRenderManager(), game.getViewPort());
        poisonEmitter.setParticlesPerSec(0);

    }

    /**
     * Emitts three particles each tick when damage is applied.
     */
    private void emittPoisonParticles() {
        poisonEmitter.setParticlesPerSec(3);
        poisonEmitter.emitAllParticles();
        poisonEmitter.setParticlesPerSec(0);
    }
}
