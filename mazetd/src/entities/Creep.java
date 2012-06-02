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
 * File: Creep.java
 * Type: entities.Creep
 * 
 * Documentation created: 21.05.2012 - 22:01:48 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Cylinder;
import entities.Map.MapSquare;
import entities.Orb.ElementType;
import entities.base.CollidableEntity;
import entities.base.EntityManager;
import entities.effects.OrbEffect;
import entities.nodes.CollidableEntityNode;
import eventsystem.CreepHandler;
import eventsystem.events.CreepEvent.CreepEventType;
import eventsystem.port.Collider3D;
import java.util.HashSet;
import java.util.Queue;
import java.util.Random;
import logic.pathfinding.Pathfinder;
import mazetd.MazeTDGame;

/**
 * The class Creep discribes the creep entity.
 * A creep can walk to a given position, can be hit by a projectile, can have 
 * orb effects on it and can destroy a tower.
 * @author Hans Ferchland
 * @version 0.1f
 */
public class Creep extends CollidableEntity {
    //==========================================================================
    //===   Constants
    //==========================================================================

    public static final float CREEP_BASE_DAMAGE = 50.0f;
    public static final float CREEP_BASE_ORB_DROP = 0.0f;
    public static final float CREEP_BASE_SPEED = 1.1f;
    public static final float CREEP_DECAY = 1f;
    public static final int CREEP_GOLD_PARTICLES = 5;
    public static final float CREEP_GROUND_RADIUS = 0.25f;
    public static final float CREEP_HEIGHT = 0.5f;
    public static final int CREEP_MAX_HP = 100;
    public static final float CREEP_MIN_DISTANCE = 0.1f;
    private static final int CREEP_SAMPLES = 10;
    public static final float CREEP_TOP_RADIUS = 0.1f;
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    // visual
    private Geometry geometry;
    private Material material;
    private Vector3f position;
    private Vector3f target;
    private boolean deacying = false;
    private float decayTime = 0;
    // logic
    private float healthPoints = CREEP_MAX_HP;
    private float maxHealthPoints = CREEP_MAX_HP;
    private Tower attacker;
    private Tower attacking;
    private float damage = CREEP_BASE_DAMAGE;
    private HashSet<OrbEffect> orbEffects = new HashSet<OrbEffect>();
    private float speed = CREEP_BASE_SPEED;
    private boolean moving = true;
    private int goldDrop = 0;
    private float orbDropRate = CREEP_BASE_ORB_DROP;
    // Pathfinding
    private Queue<Map.MapSquare> path;
    private MapSquare currentSquare;
    private MapSquare lastSquare = null;
    //Particles
    private ParticleEmitter goldEmitter;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * The constructor of the entity with a given name, hp and position.
     * @param name the desired name of the creep
     * @param position the position on the map
     * @param healthPoints the HP of the creep
     */
    public Creep(String name, Vector3f position, float healthPoints, float maxHealthPoints) {
        super(name);
        this.maxHealthPoints = maxHealthPoints;
        this.healthPoints = healthPoints;
        this.position = position;
        this.path = Pathfinder.getInstance().getMainPath();
        this.currentSquare = path.peek();
        start(path.poll().getLocalTranslation());
    }

    @Override
    protected void update(float tpf) {
        if (deacying) {

            decayTime += tpf;
            if (decayTime > CREEP_DECAY) {
                rotten();
            }
        }
        // if moving do this part
        moveUpdate(tpf);
    }

    @Override
    public void onCollision(CollisionResults collisionResults) {
    }

    @Override
    public CollidableEntityNode createNode(MazeTDGame game) {
        super.createNode(game);

        createCreepGeometry(game);
        createGoldEmitter(game);

        return collidableEntityNode;
    }

    /**
     * Starts the movement of a creep coordinated by pathfinding.
     * @param firstTarget the first desired position to go to
     */
    private void start(Vector3f firstTarget) {
        moveTo(firstTarget);
    }

    /**
     * Finally frees the creeps resources.
     */
    private void rotten() {
        Collider3D.getInstance().removeCollisonObject(collidableEntityNode);
        EntityManager.getInstance().removeEntity(id);
    }

    /**
     * Moves the creep for every update call. The creep will go to a position 
     * given by its path calculated by the pathfinder. If it arrives on a position,
     * controlled by a min. distance, it will poll the next point in path to 
     * go to from the path-Queue.
     * @param tpf the time-gap
     */
    private void moveUpdate(float tpf) {
        if (moving) {
            position = collidableEntityNode.getLocalTranslation();

            Vector3f dir = target.subtract(position);
            float distance = dir.length();
            dir.normalizeLocal();
            dir.multLocal(speed * tpf);

            if (distance < CREEP_MIN_DISTANCE) {
                // moving ended because the creep is at the target
                if (!path.isEmpty()) {
                    // so get the next square to move to
                    this.lastSquare = currentSquare;
                    this.currentSquare = path.peek();
                    // and remove it from the Queue
                    moveTo(path.poll().getLocalTranslation());
                } else {
                    // TODO: handle tower-attacking or greep-enters-goal event
                    // otherwise we are attacking a tower or are at the goal.
                    moving = false;
                }
            } else {
                // if not at the targeted position, move
                position.addLocal(dir);
            }
            // apply translation to the geometry
            collidableEntityNode.setLocalTranslation(position);
        }
    }

    /**
     * Creates the creeps geometry and attaches it to the collidableEntityNode.
     * @param game the MazeTDGame reference
     */
    private void createCreepGeometry(MazeTDGame game) {
        // Material
        material = new Material(
                game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        material.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        material.setColor("Specular", ColorRGBA.White);
        material.setColor("Ambient", ColorRGBA.Black);   // ... color of this object
        material.setColor("Diffuse", ColorRGBA.Gray);   // ... color of light being reflected

        // Geometry
        float[] angles = {(float) Math.PI / 2, 0, 0};

        Cylinder c = new Cylinder(
                CREEP_SAMPLES,
                CREEP_SAMPLES,
                CREEP_GROUND_RADIUS,
                CREEP_TOP_RADIUS,
                CREEP_HEIGHT,
                true, false);

        geometry = new Geometry("Creep_Geometry_" + name, c);
        geometry.setMaterial(material);
        geometry.setLocalTranslation(0, CREEP_HEIGHT * 0.5f + 0.01f, 0);
        geometry.setLocalRotation(new Quaternion(angles));
        geometry.setQueueBucket(Bucket.Inherit);

        collidableEntityNode.attachChild(geometry);
        collidableEntityNode.setLocalTranslation(position);
        collidableEntityNode.setShadowMode(ShadowMode.CastAndReceive);
    }

    private void createGoldEmitter(MazeTDGame game) {

        goldEmitter = new ParticleEmitter("Emitter",
                ParticleMesh.Type.Triangle, CREEP_GOLD_PARTICLES);
        Material mat_red = new Material(
                game.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture",
                game.getAssetManager().loadTexture("Textures/Effects/GoldCoin.png"));
        goldEmitter.setMaterial(mat_red);
        goldEmitter.setImagesX(4);
        goldEmitter.setImagesY(4); // 4x4 texture animation
        goldEmitter.setEndColor(new ColorRGBA(1f, 1f, 0f, 0f));
        goldEmitter.setStartColor(new ColorRGBA(1f, 1f, 0f, 1.0f));
        goldEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2.5f, 0));
        goldEmitter.setStartSize(0.15f);
        goldEmitter.setEndSize(0.175f);
        goldEmitter.setGravity(0f, 1.7f, 1f);
        goldEmitter.setLowLife(0.9f);
        goldEmitter.setHighLife(1.2f);
        goldEmitter.getParticleInfluencer().setVelocityVariation(0.3f);
        goldEmitter.preload(game.getRenderManager(), game.getViewPort());
        goldEmitter.setLocalTranslation(0, CREEP_HEIGHT + 0.2f, 0);
    }

    /**
     * Sets the moving-target of the creep.
     * @param target the desired position on the map
     */
    public void moveTo(Vector3f target) {
        this.target = target;
        this.target.y = 0;
        moving = true;
    }

    /**
     * Stops the movement of the creep. You have to 
     * call moveTo() to move it again.
     */
    public void stop() {
        moving = false;
    }

    /**
     * Damages a creep by <code>amount</code> points.
     * @param amount the amount of received damaged
     */
    void applyDamge(float amount) {
        this.healthPoints -= amount;
        if (isDead() && !deacying) {
            // visualize the death
            triggerGoldEmitter();
            material.setColor("Ambient", ColorRGBA.Red);
            Tower save = attacker;
            // trigger on death
            onDeath();

            // invoke creep death event
            CreepHandler.getInstance().
                    invokeCreepAction(CreepEventType.Death, this, save);
        }
    }

    private void onDeath() {
        // set decaying
        deacying = true;
        // stop movement
        stop();
        // drop an orb if chance is high enough
        dropOrb();
    }

    private void dropOrb() {
        Random r = new Random(System.currentTimeMillis());

        if (r.nextFloat() <= this.orbDropRate) {
            ElementType random;
            random = ElementType.values()[
                    r.nextInt(ElementType.values().length)];
            //Orb o = new Orb(name + "'s orb", new Vector3f(position.x, 0, position.z), random);
            EntityManager.getInstance().createOrb(
                    name + "'s orb",
                    new Vector3f(position.x, 0, position.z),
                    random);
        }
    }

    private void triggerGoldEmitter() {
        collidableEntityNode.attachChild(goldEmitter);
        goldEmitter.emitAllParticles();
        goldEmitter.setParticlesPerSec(0);
    }

    /**
     * Checks if the creep is dead.
     * @return true if below or at 0 HP, false otherwise
     */
    public boolean isDead() {
        return healthPoints <= 0;
    }

    /**
     * The current position of the creep. May be obsolete if not moving, call 
     * getCollidableEntityNode().getLocalTranslation() for exact info.
     * @return the position
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Gets the current tower which is attacking the creep.
     * @return the attacking tower or null if not under attack
     */
    public Tower getAttacker() {
        return attacker;
    }

    /**
     * Applys a tower to attack this creep.
     * @param attacker the tower currently attacking this creep
     */
    public void setAttacker(Tower attacker) {
        this.attacker = attacker;
    }

    /**
     * Returns the current HP of this creep.
     * @return the current HP
     */
    public float getHealthPoints() {
        return healthPoints;
    }

    /**
     * Gets the effects this creep is suffering.
     * @return the set of effects the creep is suffering, null if list is empty.
     */
    public HashSet<OrbEffect> getOrbEffects() {
        if (orbEffects.isEmpty()) {
            return null;
        }
        return orbEffects;
    }

    /**
     * Adds an effect to the creep.
     * @param effect the effect to add
     */
    public void addOrbEffect(OrbEffect effect) {
        orbEffects.add(effect);
    }

    /**
     * Removes an effect from the creep.
     * @param effect the effect to remove
     */
    public void removeOrbEffect(OrbEffect effect) {
        orbEffects.remove(effect);
    }

    /**
     * Applys a new path-Queue for the creep.
     * @param path the new path for the creeps movement
     */
    public void setPath(Queue<MapSquare> path) {
        this.path = path;
        moveTo(path.poll().getLocalTranslation());
    }

    public boolean isOnSquare(MapSquare field) {
        // TODO: check the old square too if building a tower!

        return field.equals(currentSquare)
                || (lastSquare != null && field.equals(lastSquare));
    }

    /**
     * Gets the currently targeted MapSquare of this creep.
     * @return the targeted map-square
     */
    public MapSquare getCurrentSquare() {
        return currentSquare;
    }

    /**
     * Stes the creeps speed, base speed is 1.1.
     * @param value the new speed
     */
    public void setSpeed(float value) {
        speed = value;
    }

    /**
     * Sets the gold dropped by this creep on death.
     * @param value 
     */
    public void setGoldDrop(int value) {
        goldDrop = value;
    }

    /**
     * Sets the orb drop rate by this creep on death.
     * @param value 
     */
    public void setOrbDropRate(float value) {
        orbDropRate = value;
    }

    /**
     * Sets the damage done to towers.
     * @param value 
     */
    public void setDamage(float value) {
        damage = value;
    }

    public float getDamage() {
        return damage;
    }

    public int getGoldDrop() {
        return goldDrop;
    }

    public float getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public float getOrbDropRate() {
        return orbDropRate;
    }

    public float getSpeed() {
        return speed;
    }
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
