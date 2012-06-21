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

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Quad;
import entities.Map.MapSquare;
import entities.Orb.ElementType;
import entities.base.CollidableEntity;
import entities.base.EntityManager;
import entities.effects.AbstractOrbEffect;
import entities.effects.RastaOrbEffect;
import entities.geometry.ClickableGeometry;
import entities.nodes.CollidableEntityNode;
import eventsystem.CreepHandler;
import eventsystem.events.CreepEvent.CreepEventType;
import eventsystem.port.Collider3D;
import eventsystem.port.ScreenRayCast3D;
import java.util.HashSet;
import java.util.Queue;
import java.util.Random;
import logic.pathfinding.Pathfinder;
import mazetd.MazeTDGame;
import logic.Player;

/**
 * The class Creep discribes the creep entity.
 * A creep can walk to a given position, can be hit by a projectile, can have 
 * orb effects on it and can destroy a tower.
 * @author Hans Ferchland
 * @version 0.3
 */
public class Creep extends CollidableEntity {
    //==========================================================================
    //===   Constants
    //==========================================================================

    public static final float CREEP_BASE_DAMAGE = 50.0f;
    public static final float CREEP_BASE_ORB_DROP = 0.0f;
    public static final float CREEP_BASE_SPEED = 1.1f;
    public static final float CREEP_DECAY = 1f;
    public static final int CREEP_DESTROY_PARTICLES = 10;
    public static final int CREEP_GOLD_PARTICLES = 5;
    public static final float CREEP_GROUND_RADIUS = 0.25f;
    public static final float CREEP_HEIGHT = 0.5f;
    public static final int CREEP_MAX_HP = 100;
    public static final float CREEP_MIN_DISTANCE = 0.5f;
    private static final int CREEP_SAMPLES = CREEP_DESTROY_PARTICLES;
    public static final float CREEP_TOP_RADIUS = 0.1f;
    private static final float CREEP_BASE_DAMAGE_INTERVAL = 1f;
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    // visual
    private Geometry geometry;
    private Material material;
    private Vector3f position;
    private Vector3f destination;
    private boolean decaying = false;
    private float decayTime = 0;
    private ClickableGeometry debugGeometry;
    private boolean debugPathToggle = false;
    private HealthBar healthBar;
    // logic
    private float healthPoints = CREEP_MAX_HP;
    private float maxHealthPoints = CREEP_MAX_HP;
    private Tower attacker;
    private Tower target;
    private Player player = Player.getInstance();
    private HashSet<AbstractOrbEffect> orbEffects = new HashSet<AbstractOrbEffect>();
    private float speed = CREEP_BASE_SPEED;
    private boolean moving = true;
    private boolean attacking = false;
    public boolean reverted = false;
    private boolean dropping = false;
    private int goldDrop = 0;
    private float orbDropRate = CREEP_BASE_ORB_DROP;
    private float damage = CREEP_BASE_DAMAGE;
    private float damageInterval = CREEP_BASE_DAMAGE_INTERVAL;
    private float intervalCounter = 0;
    // Pathfinding
    private Queue<Map.MapSquare> path;
    private MapSquare currentSquare;
    private MapSquare lastSquare = null;
    //Particles
    private ParticleEmitter goldEmitter;
    private ParticleEmitter destroyedEmitter;
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
    public void onCollision(CollisionResults collisionResults) {
    }

    @Override
    public CollidableEntityNode createNode(MazeTDGame game) {
        super.createNode(game);

        createCreepGeometry(game);
        createGoldEmitter(game);
        createDestroyedEmitter(game);
        createDebugGeometry(game);

        healthBar = new HealthBar(this);

        return collidableEntityNode;
    }

    @Override
    protected void update(float tpf) {
        // update the bar
        healthBar.update(tpf);
        // if dead and therfore decaying
        if (decaying) {
            decayTime += tpf;
            if (decayTime > CREEP_DECAY) {
                // finally destroy
                rotten();
            }
            return;
        }
        // update the orb-effects on this creep
        orbEffectUpdate(tpf);
        // if moving do this part
        moveUpdate(tpf);
        // if attacking this
        attackUpdate(tpf);
        // Path debugging
        if (Pathfinder.DEBUG_PATH) {
            if (debugPathToggle) {
                for (MapSquare s : path) {
                    s.setCreepPathDebug(moving);
                }
            }
        }
    }

    /**
     * Checks if the creep has a reverted path, means that it runs backward.
     * @return true if runs backward, false otherwise
     */
    public boolean isReverse() {
        return reverted;
    }

    /**
     * Sets the flag for going backwards.
     * @param reverted true to go backwards, false to go forward
     */
    public void setReverse(boolean reverted) {
        this.reverted = reverted;
    }

    /**
     * Creates the creeps geometry and attaches it to the 
     * collidableEntityNode.
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

    /**
     * Creates the emitter for gold animation on death.
     * @param game the mazetd game
     */
    private void createGoldEmitter(MazeTDGame game) {

        goldEmitter = new ParticleEmitter("GoldEmitter",
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
     * Creates the emitter for destroy animation on reached end.
     * @param game the mazetd game
     */
    private void createDestroyedEmitter(MazeTDGame game) {
        destroyedEmitter = new ParticleEmitter("DestroyedEmitter",
                ParticleMesh.Type.Triangle, CREEP_DESTROY_PARTICLES);
        Material sparkMat = new Material(
                game.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        sparkMat.setTexture("Texture",
                game.getAssetManager().loadTexture("Textures/Effects/spark.png"));
        destroyedEmitter.setMaterial(sparkMat);
        destroyedEmitter.setImagesX(1);
        destroyedEmitter.setImagesY(1);
        destroyedEmitter.setEndColor(new ColorRGBA(0f, 0f, 1f, 0.0f));
        destroyedEmitter.setStartColor(new ColorRGBA(0.8f, 0f, 0.2f, 1.0f));
        destroyedEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 6f, 0));
        destroyedEmitter.setStartSize(1f);
        destroyedEmitter.setEndSize(0.5f);
        destroyedEmitter.setGravity(0f, -1f, 1f);
        destroyedEmitter.setLowLife(1.5f);
        destroyedEmitter.setHighLife(2.0f);
        destroyedEmitter.setFacingVelocity(true);
        destroyedEmitter.getParticleInfluencer().setVelocityVariation(0.2f);
        destroyedEmitter.preload(game.getRenderManager(), game.getViewPort());
        destroyedEmitter.setLocalTranslation(0, CREEP_HEIGHT + 0.2f, 0);
    }

    /**
     * Creates debug geometry for pathfind-debug.
     * @param game the mazetd game
     */
    private void createDebugGeometry(MazeTDGame game) {

        Material m = new Material(
                game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        m.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        m.setColor("Specular", ColorRGBA.BlackNoAlpha);
        m.setColor("Ambient", ColorRGBA.BlackNoAlpha);   // ... color of this object
        m.setColor("Diffuse", ColorRGBA.BlackNoAlpha);   // ... color of light being reflected


        float[] angles = {(float) Math.PI / 2, 0, 0};

        debugGeometry = new ClickableGeometry(
                name + "_DebugGeometry",
                new Cylinder(
                CREEP_SAMPLES,
                CREEP_SAMPLES,
                CREEP_GROUND_RADIUS,
                CREEP_TOP_RADIUS,
                CREEP_HEIGHT,
                true, false)) {

            @Override
            public void onRayCastClick(Vector2f mouse, CollisionResult result) {
                debugPathToggle = !debugPathToggle;
            }

            @Override
            public void onRayCastMouseOver(Vector2f mouse, CollisionResult result) {
                for (MapSquare s : path) {
                    s.setCreepPathDebug(moving);
                }

            }

            @Override
            public void onRayCastMouseLeft(Vector2f mouse, CollisionResult result) {
                if (!debugPathToggle) {
                    for (MapSquare s : path) {
                        s.setCreepPathDebug(false);
                    }
                }

            }
        };
        debugGeometry.setMaterial(m);
        debugGeometry.setCullHint(CullHint.Always);
        //debugGeometry.setLocalTranslation(0, CREEP_HEIGHT * 0.5f + 0.01f, 0);
        debugGeometry.setLocalRotation(new Quaternion(angles));
        ScreenRayCast3D.getInstance().addClickableObject(debugGeometry);

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
        collidableEntityNode.detachAllChildren();
        Collider3D.getInstance().removeCollisonObject(collidableEntityNode);
        ScreenRayCast3D.getInstance().removeClickableObject(debugGeometry);
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

            Vector3f dir = destination.subtract(position);
            float distance = dir.length();
            dir.normalizeLocal();
            dir.multLocal(speed * tpf);

            if (distance < CREEP_MIN_DISTANCE) {
                // moving ended because the creep is at the target
                if (!path.isEmpty()) {
                    // so get the next square to move to
                    this.lastSquare = currentSquare;
                    this.currentSquare = path.peek();
                    this.currentSquare.setCreepPathDebug(false);
                    // and remove it from the Queue
                    moveTo(path.poll().getLocalTranslation());
                } else {


                    if (!orbEffects.isEmpty()) {
                        // remove orb effects if there are some
                        for (AbstractOrbEffect e : orbEffects) {
                            if (e instanceof RastaOrbEffect) {
                                removeOrbEffect(e);
                            }
                        }
                    }
                    // event otherwise we are attacking a tower or are 
                    // at the goal.
                    CreepHandler.getInstance().invokeCreepAction(
                            CreepEventType.ReachedEnd, this, null);
                    moving = false;
                    destroy();

                }
            } else {
                if (currentSquare.hasTower()) {
                    destroyTower(currentSquare.getTower());
                    moving = false;
                }
                // if not at the targeted position, move
                position.addLocal(dir);
            }
            // apply translation to the geometry
            collidableEntityNode.setLocalTranslation(position);
            debugGeometry.setLocalTranslation(position);
        }
    }

    /**
     * Updates the unit if in attacking state.
     * @param tpf the time-gap
     */
    private void attackUpdate(float tpf) {
        if (attacking && target != null && !target.isDead()) {
            attack(tpf);
        } else {
            moving = true;
            attacking = false;
            target = null;
        }
    }

    /**
     * Updates the orb-effects on this creep.
     * @param tpf the time-gap
     */
    private void orbEffectUpdate(float tpf) {
        HashSet<AbstractOrbEffect> clone = new HashSet<AbstractOrbEffect>(orbEffects);
        for (AbstractOrbEffect e : clone) {
            e.update(tpf);
        }
    }

    /**
     * Moves the creep to the target and attacks it if in front of it. 
     * @param tpf the time-gap
     */
    private void attack(float tpf) {
        position = collidableEntityNode.getLocalTranslation();

        Vector3f dir = target.getClickableEntityNode().getLocalTranslation().
                subtract(position);
        float distance = dir.length();
        dir.normalizeLocal();
        dir.multLocal(speed * tpf);

        if (distance < Tower.TOWER_SIZE + CREEP_GROUND_RADIUS) {
            intervalCounter += tpf;
            if (intervalCounter > damageInterval) {
                target.applyDamage(damage);
                intervalCounter = 0;
            }
        } else {
            // if not at the targeted position, move
            position.addLocal(dir);
            // apply translation to the geometry
            collidableEntityNode.setLocalTranslation(position);
            debugGeometry.setLocalTranslation(position);
        }

    }

    /**
     * Sets the moving-target of the creep.
     * @param target the desired position on the map
     */
    public void moveTo(Vector3f target) {
        this.destination = target;
        this.destination.y = 0;
        moving = true;
    }

    /**
     * Sets the target and state if a creep wont find a path through the maze.
     * @param target the tower to destroy
     */
    private void destroyTower(Tower target) {
        this.target = target;
        this.attacking = true;
    }

    /**
     * Stops the movement of the creep. You have to 
     * call moveTo() to move it again.
     */
    public void stop() {
        moving = false;
    }

    public void destroy() {
        this.healthPoints = 0;
        triggerDestroyedEmitter();
        decaying = true;
    }

    /**
     * Damages a creep by <code>amount</code> points.
     * @param amount the amount of received damaged
     */
    public void applyDamge(float amount) {
        this.healthPoints -= amount;
        if (isDead() && !decaying) {
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

    /**
     * Is triggered if the creep dies from a tower, not if the creep is removed
     * after reaching the end.
     */
    private void onDeath() {
        // set decaying
        decaying = true;
        // stop movement
        stop();
        // drop an orb if chance is high enough

        dropOrb();

        // Path debugging
        if (Pathfinder.DEBUG_PATH) {
            if (debugPathToggle) {
                for (MapSquare s : path) {
                    s.setCreepPathDebug(false);
                }
            }
        }
    }

    /**
     * Checks if the creep drops an orb.
     * @return true if orb will be dropped, false otherwise
     */
    public boolean isDropping() {
        return dropping;
    }

    /**
     * Sets the flag for orb-dropping.
     * @param dropOrb true if an orb should be droppen, false otherwise
     */
    public void setDropping(boolean dropOrb) {
        this.dropping = dropOrb;
    }

    /**
     * Drops an orb by random on creeps death.
     */
    private void dropOrb() {
        Random r = new Random(System.currentTimeMillis());

        if (dropping) {

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

    /**
     * Triggers the emitter for the gold-particles.
     */
    private void triggerGoldEmitter() {
        collidableEntityNode.attachChild(goldEmitter);
        goldEmitter.emitAllParticles();
        goldEmitter.setParticlesPerSec(0);
    }

    /**
     * Triggers the emitter for the destroy-particles.
     */
    private void triggerDestroyedEmitter() {
        collidableEntityNode.attachChild(destroyedEmitter);
        destroyedEmitter.emitAllParticles();
        destroyedEmitter.setParticlesPerSec(0);
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
    public HashSet<AbstractOrbEffect> getOrbEffects() {
        if (orbEffects.isEmpty()) {
            return null;
        }
        return orbEffects;
    }

    /**
     * Adds an effect to the creep.
     * @param effect the effect to add
     */
    public void addOrbEffect(AbstractOrbEffect effect) {
        for (AbstractOrbEffect e : orbEffects) {
            if (e.getEffectType() == effect.getEffectType()) {
                return;
            }
        }
        orbEffects.add(effect);
        effect.onStart(this);
    }

    /**
     * Removes an effect from the creep.
     * @param effect the effect to remove
     */
    public void removeOrbEffect(AbstractOrbEffect effect) {
        effect.onEnd(this);
        orbEffects.remove(effect);
    }

    /**
     * Applys a new path-Queue for the creep.
     * @param path the new path for the creeps movement
     */
    public void setPath(Queue<MapSquare> path) {
        if (path == null || path.peek() == null) {
            return;
        }

        if (Pathfinder.DEBUG_PATH) {
            for (MapSquare s : this.path) {
                s.setCreepPathDebug(false);
            }
        }
        this.path = path;
//        moveTo(path.poll().getLocalTranslation());
    }

    public boolean isOnSquare(MapSquare field) {
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
     * Gets the current movement-speed of the creep.
     * @return the amount to move per time-gap
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Sets the gold dropped by this creep on death.
     * @param value 
     */
    public void setGoldDrop(int value) {
        goldDrop = value;
    }

    /**
     * Gets the gold dropped by this creep on death.
     * @return the amount of gold to drop
     */
    public int getGoldDrop() {
        return goldDrop;
    }

    /**
     * Sets the orb drop rate by this creep on death.
     * @param value 
     */
    @Deprecated
    public void setOrbDropRate(float value) {
        orbDropRate = value;
    }

    /**
     * Gets the drop-rate for orbs of this creep.
     * @return the probability to drop an orb between 0..1
     */
    @Deprecated
    public float getOrbDropRate() {
        return orbDropRate;
    }

    /**
     * Sets the damage done to towers.
     * @param value the new value for the creeps damage
     */
    public void setDamage(float value) {
        damage = value;
    }

    /**
     * Gets the damage done to towers.
     * @return the damage one creep does in one attack action
     */
    public float getDamage() {
        return damage;
    }

    /**
     * Gets the maximum health this creep can have.
     * @return the max. hp amount
     */
    public float getMaxHealthPoints() {
        return maxHealthPoints;
    }

    //==========================================================================
    //===   Inner Classes
    //==========================================================================
    /**
     * The class HealthBar for the HP-visualization from creeps.
     * @author Hans Ferchland
     */
    private class HealthBar extends Node {
        //==========================================================================
        //===   Constants
        //==========================================================================
        public static final float BAR_HEIGHT = 0.1f;
        public static final float BAR_WIDTH = 0.5f;
        public static final float FRAME_HEIGHT = 0.15f;
        public static final float FRAME_WIDTH = 0.55f;
        //==========================================================================
        //===   Private Fields
        //==========================================================================
        private Material barMaterial;
        private Material frameMaterial;
        private Geometry barGeometry;
        private Geometry frameGeometry;
        private Creep creep;
        private Camera cam;
        //==========================================================================
        //===   Methods & Constructor
        //==========================================================================

        /**
         * Contructor for a new health bar for a given creep.
         * @param creep the creep to attach the hp-bar
         */
        public HealthBar(Creep creep) {
            super();
            this.creep = creep;
            createBar();
        }

        /**
         * Refreshes the bar in state and destroys itsself if the creep is dead.
         * @param tpf the time-gap
         */
        public void update(float tpf) {
            if (creep != null && creep.isDead()) {
                this.detachAllChildren();
                creep.collidableEntityNode.detachChild(this);
                return;
            }
            orientate();
            barGeometry.setLocalScale(creep.healthPoints / creep.maxHealthPoints, 1, 1);
        }

        /**
         * Positions the bar so that it always looks to the camera and stays 
         * horizontally.
         */
        private void orientate() {
            Vector3f barOffset =
                    new Vector3f(BAR_WIDTH / 2, 0.01f, -BAR_HEIGHT / 2);
            Vector3f frameOffset =
                    new Vector3f(FRAME_WIDTH / 2, 0, -FRAME_HEIGHT / 2);

            Vector3f up = cam.getUp().clone();
            Vector3f dir = cam.getDirection().
                    clone().negateLocal().normalizeLocal();
            Vector3f left = cam.getLeft().
                    clone().normalizeLocal().negateLocal();

            Quaternion look = new Quaternion();
            look.fromAxes(left, up, dir);

            frameGeometry.setLocalTransform(new Transform(frameOffset, look));
            barGeometry.setLocalTransform(new Transform(barOffset, look));
        }

        /**
         * Creates the geometry and material for the hp-bar.
         */
        private void createBar() {
            MazeTDGame game = MazeTDGame.getInstance();
            cam = game.getCamera();

            // Material
            barMaterial = new Material(game.getAssetManager(),
                    "Common/MatDefs/Misc/Unshaded.j3md");
            barMaterial.setColor("Color", new ColorRGBA(0, 1, 0, 0.6f));
            barMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

            frameMaterial = new Material(game.getAssetManager(),
                    "Common/MatDefs/Misc/Unshaded.j3md");
            frameMaterial.setColor("Color", new ColorRGBA(0, 0, 0, 0.6f));
            frameMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

            // Geometry

            Quad frame = new Quad(FRAME_WIDTH, FRAME_HEIGHT);

            frameGeometry = new Geometry("frameGeometry", frame);
            frameGeometry.setMaterial(frameMaterial);
            frameGeometry.setCullHint(CullHint.Inherit);
            frameGeometry.setShadowMode(ShadowMode.Off);
            frameGeometry.setQueueBucket(Bucket.Translucent);

            Quad bar = new Quad(BAR_WIDTH, BAR_HEIGHT);

            barGeometry = new Geometry("frameGeometry", bar);
            barGeometry.setMaterial(barMaterial);
            barGeometry.setCullHint(CullHint.Inherit);
            barGeometry.setShadowMode(ShadowMode.Off);
            barGeometry.setQueueBucket(Bucket.Translucent);

            this.attachChild(barGeometry);
            this.attachChild(frameGeometry);
            creep.collidableEntityNode.attachChild(this);

            this.setLocalTranslation(0, CREEP_HEIGHT + 0.4f, 0);
            orientate();
        }
    }
}
