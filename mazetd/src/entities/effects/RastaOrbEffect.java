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
import entities.Orb.SpecialElementType;
import logic.pathfinding.CreepAI;
import mazetd.MazeTDGame;

/**
 * The class RastaOrbEffect is a SpecialElementType with a unique effect if the
 * orb-combination of a tower fits. This effect will let a hit creep 
 * run backwards.
 * 
 * @author Hady Khalifa
 * @version 1.0
 * @see OrbEffectManager
 */
public class RastaOrbEffect extends AbstractOrbEffect {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /** The duration. */
    private float duration = 3;
    
    /** The duration counter. */
    private float durationCounter = 0;
    
    /** The decaying. */
    private boolean decaying = false;
    // Particle
    /** The poison emitter. */
    private ParticleEmitter poisonEmitter;
    
    /** The creep ai. */
    private CreepAI creepAI = CreepAI.getInstance();
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Creates a SpeedOrbEffect.
     */
    public RastaOrbEffect() {
        super(OrbEffectType.RASTA, SpecialElementType.RASTA, 0);
        createRastaEmitter(MazeTDGame.getInstance());
    }

    /* (non-Javadoc)
     * @see entities.effects.AbstractOrbEffect#update(float)
     */
    @Override
    public void update(float tpf) {
        durationCounter += tpf;

        if (decaying) {
            if (poisonEmitter.getNumVisibleParticles() == 0) {
                infected.getCollidableEntityNode().detachChild(poisonEmitter);
            }
        } else {
            if (durationCounter > duration) {
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
        // set reversed creep mode on
        creepAI.setSomeReverse(true);
        // gets save the infected
        infected = c;
        // reverse path of the creep
        infected.setReverse(true);
        // attach effect and start effect
        infected.getCollidableEntityNode().attachChild(poisonEmitter);
        poisonEmitter.emitAllParticles();
    }

    /* (non-Javadoc)
     * @see entities.effects.AbstractOrbEffect#onEnd(entities.Creep)
     */
    @Override
    public void onEnd(Creep c) {

        infected.setReverse(false);
        creepAI.createUniqueCreepPath(infected);
        poisonEmitter.setNumParticles(0);
    }

    /**
     * Creates an emitter for the effect as visuals.
     * @param game the mazetdgame reference
     */
    private void createRastaEmitter(MazeTDGame game) {
        /** Uses Texture from jme3-test-data library! */
        poisonEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 5);
        Material mat_red = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture",
                game.getAssetManager().loadTexture("Textures/Effects/rasta.png"));
        poisonEmitter.setMaterial(mat_red);
        poisonEmitter.setImagesX(1);
        poisonEmitter.setImagesY(1);
        poisonEmitter.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));
        poisonEmitter.setStartColor(new ColorRGBA(0f, 1f, 0f, 0.0f));
        poisonEmitter.getParticleInfluencer().setInitialVelocity(
                new Vector3f(0, 1.5f, 0));
        poisonEmitter.setStartSize(0.05f);
        poisonEmitter.setEndSize(0.1f);
        poisonEmitter.setGravity(0f, -2f, 1f);
        poisonEmitter.setLowLife(0.6f);
        poisonEmitter.setHighLife(1.2f);
        poisonEmitter.setRotateSpeed((float) Math.PI);
        poisonEmitter.setRandomAngle(true);
        poisonEmitter.getParticleInfluencer().setVelocityVariation(0.25f);
        poisonEmitter.preload(game.getRenderManager(), game.getViewPort());
        poisonEmitter.setParticlesPerSec(5);

    }
}
