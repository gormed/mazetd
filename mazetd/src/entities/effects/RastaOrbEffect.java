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
 * The class PoisonOrbEffect.
 * @author Hans Ferchland
 * @version
 */
public class RastaOrbEffect extends AbstractOrbEffect {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private float[] damage = {4, 6, 10};
    private float[] particlePeriod = {0.9f, 0.9f, 0.9f};
    private float[] duration = {1, 2, 3};
    private float particleCounter = 0;
    private float durationCounter = 0;
    private boolean decaying = false;
    // Particle
    private ParticleEmitter poisonEmitter;
    private CreepAI creepAI = CreepAI.getInstance();
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public RastaOrbEffect(int level) {
        super(OrbEffectType.RASTA, SpecialElementType.RASTA, level);
        createPoisonEmitter(MazeTDGame.getInstance());
    }

    @Override
    public void update(float tpf) {
        durationCounter += tpf;
        particleCounter += tpf;

        if (decaying) {
            if (poisonEmitter.getNumVisibleParticles() == 0) {
                infected.getCollidableEntityNode().detachChild(poisonEmitter);
            }
        } else {
            if (durationCounter > duration[level]) {
                infected.removeOrbEffect(this);
                decaying = true;
            }
        }



    }

    @Override
    public void onEffect() {
    }

    @Override
    public void onStart(Creep c) {
        creepAI.setSomeReverse(true);
        infected = c;
        infected.setReverse(true);
        infected.getCollidableEntityNode().attachChild(poisonEmitter);
    }

    @Override
    public void onEnd(Creep c) {

        infected.setReverse(false);
        creepAI.createUniqueCreepPath(infected);
    }

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

    private void emittPoisonParticles() {
        poisonEmitter.setParticlesPerSec(3);
        poisonEmitter.emitAllParticles();
        poisonEmitter.setParticlesPerSec(0);
    }
}
