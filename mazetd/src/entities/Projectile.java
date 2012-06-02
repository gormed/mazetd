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
 * File: Projectile.java
 * Type: entities.Projectile
 * 
 * Documentation created: 01.06.2012 - 20:37:05 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import entities.base.AbstractEntity;
import entities.base.EntityManager;
import entities.effects.OrbEffect;
import logic.Level;
import mazetd.MazeTDGame;

/**
 * The class Projectile for the fired graphical and logical instance of a 
 * projectile fired by a tower.
 * @author Hans Ferchland
 */
class Projectile extends AbstractEntity {
    //==========================================================================
    //===   COnstants
    //==========================================================================

    public static final float PROJECTILE_BASE_SPEED = 3.f;
    public static final float MAX_FADE = 0.8f;
    public static final int PROJECTILE_IMPACT_PARTICLES = 5;
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Geometry geometry;
    private float speed = PROJECTILE_BASE_SPEED;
    private Vector3f position;
    private Creep target;
    private float damage;
    private Material material;
    private final ColorRGBA color;
    private float initialDistance = 0;
    private OrbEffect orbEffect;
    private boolean decays = false;
    private float fadeCounter = 0;
    // floating Particles
    private ParticleEmitter floatingEmitter;
    // impact Particles
    private ParticleEmitter impactEmitter;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * The constructor of a Projectile that is fired to <code>target</code>
     * creep from given position: <code>position</code>.
     * @param name
     * @param position
     * @param target 
     */
    public Projectile(String name, Vector3f position, Creep target, float damage, ColorRGBA color) {
        super(name);
        this.target = target;
        this.position = position;
        this.damage = damage;
        this.color = color;
        this.initialDistance = target.getPosition().subtract(position).length();
    }

    @Override
    public void update(float tpf) {
        if (target != null) {
            move(tpf);
            checkForHit();
        }
        if (decays) {
            fadeCounter += tpf;
            if (fadeCounter > MAX_FADE) {
                destroy();
            }
        }
    }

    @Override
    public Node createNode(MazeTDGame game) {
        super.createNode(game);

        createGeometry(game);
        createFloatingParticleEmitter(game);
        createImpactParticleEmitter(game);

        Level.getInstance().
                getDynamicLevelElements().attachChild(geometryNode);


        return geometryNode;
    }

    private void createFloatingParticleEmitter(MazeTDGame game) {
        /** Uses Texture from jme3-test-data library! */
        floatingEmitter = new ParticleEmitter(
                "Emitter", ParticleMesh.Type.Triangle, 30);
        Material mat_red = new Material(
                game.getAssetManager(),
                "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture(
                "Texture",
                game.getAssetManager().
                loadTexture("Textures/Effects/flame.png"));
        floatingEmitter.setMaterial(mat_red);
        // 2x2 texture animation
        floatingEmitter.setImagesX(2);
        floatingEmitter.setImagesY(2);
        ColorRGBA end = color.clone();
        end.a = 0;
        floatingEmitter.setEndColor(end);
        floatingEmitter.setStartColor(color);
        floatingEmitter.getParticleInfluencer().setInitialVelocity(
                target.getPosition().subtract(position).normalize().negate());
        floatingEmitter.setStartSize(0.2f);
        floatingEmitter.setEndSize(0.05f);
        floatingEmitter.setGravity(0f, 0f, 0f);
        floatingEmitter.setLowLife(0.15f);
        floatingEmitter.setHighLife(0.2f);
        floatingEmitter.setParticlesPerSec(60);
        floatingEmitter.getParticleInfluencer().setVelocityVariation(0.3f);
        geometryNode.attachChild(floatingEmitter);
        floatingEmitter.preload(game.getRenderManager(), game.getViewPort());
        floatingEmitter.emitAllParticles();

    }

    private void createImpactParticleEmitter(MazeTDGame game) {
        /** Explosion effect. Uses Texture from jme3-test-data library! */
        impactEmitter = new ParticleEmitter(
                "impactEmitter", ParticleMesh.Type.Triangle, PROJECTILE_IMPACT_PARTICLES);
        Material impactEmitter_mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        impactEmitter_mat.setTexture("Texture", game.getAssetManager().loadTexture(
                "Textures/Effects/Debris.png"));
        impactEmitter.setMaterial(impactEmitter_mat);
        impactEmitter.setImagesX(3);
        impactEmitter.setImagesY(3); // 3x3 texture animation
        impactEmitter.setRotateSpeed(4);
        impactEmitter.setSelectRandomImage(true);
        impactEmitter.setLowLife(MAX_FADE * 0.9f);
        impactEmitter.setHighLife(MAX_FADE);
        impactEmitter.setStartSize(0.1f);
        impactEmitter.setEndSize(0.16f);
        impactEmitter.getParticleInfluencer().
                setInitialVelocity(new Vector3f(0, 2, 0));
        impactEmitter.setStartColor(color);
        impactEmitter.setGravity(0f, 6f, 3f);
        impactEmitter.getParticleInfluencer().setVelocityVariation(.25f);

        impactEmitter.preload(game.getRenderManager(), game.getViewPort());


    }

    /**
     * Creates the geometry for the Projectirl.
     */
    private void createGeometry(MazeTDGame game) {
        Sphere s = new Sphere(5, 5, 0.1f);

        material = new Material(
                game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        material.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        material.setColor("Specular", ColorRGBA.White);
        material.setColor("Ambient", color);   // ... color of this object
        material.setColor("Diffuse", color);   // ... color of light being reflected

        geometry = new Geometry("ProjectileGeometry", s);
        geometry.setMaterial(material);
        geometry.setShadowMode(ShadowMode.CastAndReceive);
        geometryNode.attachChild(geometry);
        geometryNode.setLocalTranslation(position);
    }

    /**
     * Emitts paricles once for an impact.
     */
    private void emitImpactParitcles() {
        impactEmitter.setLocalTranslation(geometryNode.getLocalTranslation());
        Level.getInstance().getDynamicLevelElements().attachChild(impactEmitter);
        Level.getInstance().getDynamicLevelElements().detachChild(geometryNode);
        impactEmitter.emitAllParticles();
        impactEmitter.setParticlesPerSec(0);


//            Level.getInstance().getDynamicLevelElements().attachChild(impactEmitter);
//
//            //impactEmitter.setEnabled(true);
//            impactEmitter.setShape(new EmitterPointShape(position));
//
//            impactEmitter.setParticlesPerSec(20);
//            impactEmitter.emitAllParticles();
//            impactEmitter.setParticlesPerSec(0);
    }

    /**
     * Moves the projectile each update call.
     * @param tpf the time-gap
     */
    private void move(float tpf) {


//            float currentDistance =
//                    target.getPosition().subtract(position).length();
//            float percentage =
//                    (initialDistance - currentDistance) / initialDistance;

        position = geometryNode.getLocalTranslation();

        Vector3f dir = target.getPosition().subtract(position);
        dir.normalizeLocal();
        dir.multLocal(speed * tpf);
        position.addLocal(dir);
        // TODO: add curved flying
//            if (percentage < 0.9f) {
//                Math.min(0.1f + percentage, 1f);
//                projectilePos.y += Math.sin((percentage) * Math.PI * 2f) * tpf * 1f;
//            }
        geometryNode.setLocalTranslation(position);
    }

    /*
     * Is called from the projectile if the target was hit. Does the 
     * proper damage and destroys the projectile.
     */
    private void onHit() {
        System.out.println(target.getName() + " recieved " + damage + " damage!");
        target.applyDamge(damage);
        decays = true;

    }

    /**
     * Destroys the projectile due removing from entity-manager and
     * from dynamic level elements.
     */
    private void destroy() {
        EntityManager.getInstance().removeEntity(this.getEntityId());
        Level.getInstance().getDynamicLevelElements().detachChild(impactEmitter);
    }

    /**
     * Checks for collision with the targeted creep and fires onHit().
     */
    private void checkForHit() {
        float dist =
                target.getPosition().subtract(position).length();

        if (dist <= Creep.CREEP_TOP_RADIUS && !decays) {
            emitImpactParitcles();
            onHit();
        }

        /**
        CollisionResults collisionResults =
        Collider3D.getInstance().objectCollides(geometryNode.getWorldBound());
        // if there are creeps
        if (collisionResults != null) {
        Node n;
        // find each and
        for (CollisionResult result : collisionResults) {
        n = result.getGeometry().getParent();
        // check if collidable entity
        if (n instanceof CollidableEntityNode) {
        CollidableEntityNode col = (CollidableEntityNode) n;
        AbstractEntity e = col.getEntity();
        // check if creep entity
        if (e instanceof Creep) {
        Creep c = (Creep) e;
        if (c.equals(target)) {
        onHit();
        emitParitcles(position);
        }
        }
        }
        }
        
        }
         */
    }
}
