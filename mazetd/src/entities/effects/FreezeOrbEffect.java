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
 * The class FreezeOrbEffect for creeps. This effect will slow a creep.
 * @author Hans Ferchland
 * @version
 */
public class FreezeOrbEffect extends AbstractOrbEffect {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /**
     * The percentage of speed the creep will have from its original speed.
     */
    private float[] slowValue = {0.8f, 0.6f, 0.4f};
    /**
     * The duration of the slow.
     */
    private float[] duration = {4, 5, 6};
    
    /** The duration counter. */
    private float durationCounter = 0;
    
    /** The decaying. */
    private boolean decaying = false;
    
    /** The temp speed. */
    private float tempSpeed;
    // Particle
    /** The freeze emitter. */
    private ParticleEmitter freezeEmitter;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Creates a FreezeOrbEffect with a given level.
     * @param level the level of the effect
     */
    public FreezeOrbEffect(int level) {
        super(OrbEffectType.FROST, ElementType.BLUE, level);
        createFreezeEmitter(MazeTDGame.getInstance());
    }

    /* (non-Javadoc)
     * @see entities.effects.AbstractOrbEffect#update(float)
     */
    @Override
    public void update(float tpf) {
        durationCounter += tpf;

        if (decaying) {
            if (freezeEmitter.getNumVisibleParticles() == 0) {
                infected.getCollidableEntityNode().detachChild(freezeEmitter);
            }
        } else {
            if (durationCounter > duration[level]) {
                infected.removeOrbEffect(this);
                decaying = true;
            }
        }



    }

    /* (non-Javadoc)
     * @see entities.effects.AbstractOrbEffect#onEffect()
     */
    @Override
    public void onEffect() {
    }

    /* (non-Javadoc)
     * @see entities.effects.AbstractOrbEffect#onStart(entities.Creep)
     */
    @Override
    public void onStart(Creep c) {
        infected = c;
        infected.getCollidableEntityNode().attachChild(freezeEmitter);
        tempSpeed = infected.getSpeed();
        infected.setSpeed(tempSpeed * slowValue[level]);
        freezeEmitter.setParticlesPerSec(5);
        freezeEmitter.emitAllParticles();
    }

    /* (non-Javadoc)
     * @see entities.effects.AbstractOrbEffect#onEnd(entities.Creep)
     */
    @Override
    public void onEnd(Creep c) {
        infected.setSpeed(tempSpeed);
        freezeEmitter.setParticlesPerSec(0);
        
    }

    /**
     * Creates the emitter for freeze effects.
     * @param game the mazetdgame ref
     */
    private void createFreezeEmitter(MazeTDGame game) {
                /** Uses Texture from jme3-test-data library! */
        freezeEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 5);
        Material mat_red = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture",
                game.getAssetManager().loadTexture("Textures/Effects/FreezeParticle.png"));
        freezeEmitter.setMaterial(mat_red);
        freezeEmitter.setImagesX(1);
        freezeEmitter.setImagesY(1);
        freezeEmitter.setEndColor(new ColorRGBA(0f, 0f, 1f, 1f));
        freezeEmitter.setStartColor(new ColorRGBA(1f, 1f, 1f, 0.0f));
        freezeEmitter.getParticleInfluencer().setInitialVelocity(
                new Vector3f(0, 2, 0));
        freezeEmitter.setStartSize(0.1f);
        freezeEmitter.setEndSize(0.15f);
        freezeEmitter.setGravity(0f, 0.5f, 1f);
        freezeEmitter.setLowLife(0.6f);
        freezeEmitter.setHighLife(1.0f);
        freezeEmitter.setRotateSpeed((float)Math.PI);
        freezeEmitter.getParticleInfluencer().setVelocityVariation(0.5f);
        freezeEmitter.preload(game.getRenderManager(), game.getViewPort());
    }
}
