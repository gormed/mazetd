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
 * File: Tower.java
 * Type: entities.Tower
 * 
 * Documentation created: 16.05.2012 - 17:41:15 by Hady Khalifa
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Line;
import com.jme3.texture.Texture;
import entities.Orb.ElementType;
import entities.base.AbstractEntity;
import entities.base.ClickableEntity;
import entities.base.EntityManager;
import entities.effects.AbstractOrbEffect;
import entities.effects.OrbEffectManager;
import entities.nodes.CollidableEntityNode;
import eventsystem.port.Collider3D;
import eventsystem.port.ScreenRayCast3D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import logic.Level;
import mazetd.MazeTDGame;
import jme3tools.optimize.GeometryBatchFactory;
import logic.Player;

/**
 * The class Tower for a basical tower in MazeTD.
 * @author Hady Khalifa & Hans Ferchland
 * @version 0.6
 */
public class Tower extends ClickableEntity {

    //==========================================================================
    //===   Constants
    //========================================================================== 
    /**
     * The base-interval for a tower to apply damage to a creep.
     */
    public static final float TOWER_BASE_DAMAGE_INTERVAL = 1.2f;
    /**
     * The base-damage to do on a creep. This plus the TOWER_ADDITIONAL_DAMAGE
     * is the maximum damage a tower can do without orbs. This is the number of
     * sides of the dice that is rolled each attack.
     */
    public static final int TOWER_BASE_DAMAGE = 6;
    /**
     * The additional damage added to the base damage to get the maximum value.
     */
    public static final int TOWER_ADDITIONAL_DAMAGE = 2;
    /**
     * The standard range of a tower without orbs.
     */
    public static final float TOWER_BASE_RANGE = 2;
    /**
     * The time to fadeout the tower on destruction.
     */
    public static final int TOWER_DECAY = 2;
    /**
     * The base-hp of a tower.
     */
    public static final int TOWER_HP = 200;
    /**
     * Used for animation of orbs.
     * @deprecated
     */
    @Deprecated
    public static final float TOWER_ORB_ROTATION_SPEED = 0.5f;
    /**
     * The height of the visual for the tower-range display.
     */
    public static final float TOWER_RANGE_RADIUS_HEIGHT = 0.15f;
    /**
     * The base-height of the tower.
     */
    public static final float TOWER_HEIGHT = 1.0f;
    /**
     * The base-width of the tower.
     */
    public static final float TOWER_SIZE = 0.3f;
    
    /** The Constant RANGE_CYLINDER_HEIGHT. */
    private static final float RANGE_CYLINDER_HEIGHT = 0.01f;
    
    /** The Constant TOWER_SAMPLES. */
    private static final int TOWER_SAMPLES = 15;
    
    /** The Constant ROOF_SIZE. */
    private static final float ROOF_SIZE = 0.35f;
    
    /** The Constant GAME. */
    private static final MazeTDGame GAME = MazeTDGame.getInstance();
    
    /** The hovered tower. */
    private static Tower hoveredTower = null;
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    //visual
    /** The roof geometry. */
    private Geometry roofGeometry;
    
    /** The wall geometry. */
    private Geometry wallGeometry;
    
    /** The tower geometry. */
    private Spatial towerGeometry;
    
    /** The collision cylinder. */
    private Geometry collisionCylinder;
    
    /** The roof material. */
    private Material roofMaterial;
    
    /** The wall material. */
    private Material wallMaterial;
    
    /** The collision material. */
    private Material collisionMaterial;
    
    /** The projectile color. */
    private ColorRGBA projectileColor = new ColorRGBA(0.5f, 0.5f, 0.5f, 1f);
    
    /** The position. */
    private Vector3f position;
    
    /** The deacying. */
    private boolean deacying = false;
    
    /** The decay time. */
    private float decayTime = 0;
    
    /** The fade color. */
    private ColorRGBA fadeColor = new ColorRGBA();
    //logic
    /** The tower range. */
    private float towerRange = TOWER_BASE_RANGE;
    
    /** The target. */
    private Creep target;
    
    /** The square. */
    private Map.MapSquare square;
    
    /** The health points. */
    private float healthPoints = TOWER_HP;
    
    /** The max health points. */
    private float maxHealthPoints = TOWER_HP;
    
    /** The damage max. */
    private float damageMax = TOWER_BASE_DAMAGE;
    
    /** The additional damage. */
    private float additionalDamage = TOWER_ADDITIONAL_DAMAGE;
    
    /** The damage interval. */
    private float damageInterval = TOWER_BASE_DAMAGE_INTERVAL;
    
    /** The interval counter. */
    private float intervalCounter = 0;
    
    /** The first orb. */
    private Orb firstOrb;
    
    /** The second orb. */
    private Orb secondOrb;
    
    /** The third orb. */
    private Orb thirdOrb;
    
    /** The orb node pos. */
    private Node orbNodePos;
    
    /** The orb node rot. */
    private Node orbNodeRot;
    
    /** The orb rotation. */
    private float orbRotation = 0;
    
    /** The orb effects. */
    private ArrayList<AbstractOrbEffect> orbEffects =
            new ArrayList<AbstractOrbEffect>();
    
    /** The effect manager. */
    private OrbEffectManager effectManager = OrbEffectManager.getInstance();
    //jme3
    /** The attack range collision node. */
    private Node attackRangeCollisionNode;
    //particle

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    /**
     * Contructor of a basical tower for MazeTD.
     *
     * @param name the name of the tower
     * @param square the square
     */
    public Tower(String name, Map.MapSquare square) {
        super(name);
        this.square = square;
    }

    /* (non-Javadoc)
     * @see entities.base.ClickableEntity#createNode(mazetd.MazeTDGame)
     */
    @Override
    public Node createNode(MazeTDGame game) {
        super.createNode(game);
        createGeometry(game);
        // create collision for tower attacking range

        attackRangeCollisionNode =
                new Node("AttackCollisionCylinderNode");
        createCollision(game);

        orbNodePos = new Node("OrbNodeRos");
        orbNodeRot = new Node("OrbNodeRot");
        orbNodePos.setLocalTranslation(position);
        orbNodePos.attachChild(orbNodeRot);
        Level.getInstance().getDynamicLevelElements().attachChild(orbNodePos);

        return clickableEntityNode;
    }

    /**
     * Creates the towers material and geometry.
     * @param game the game reference
     */
    private void createGeometry(MazeTDGame game) {
        // apply map square
        Vector3f pos = square.getLocalTranslation();
        this.position = new Vector3f(pos.x, 0, pos.z);
        square.setTower(this);

        // Materials
        roofMaterial = new Material(
                game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        roofMaterial.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        roofMaterial.setColor("Specular", ColorRGBA.White);
        roofMaterial.setColor("Ambient", ColorRGBA.Orange);   // ... color of this object
        roofMaterial.setColor("Diffuse", ColorRGBA.Orange);   // ... color of light being reflected

//        wallMaterial = new Material(
//                game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
//        wallMaterial.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
//        wallMaterial.setColor("Specular", ColorRGBA.White);
//        wallMaterial.setColor("Ambient", ColorRGBA.Gray);   // ... color of this object
//        wallMaterial.setColor("Diffuse", ColorRGBA.Gray);   // ... color of light being reflected#
//        Texture tex =
//                game.getAssetManager().loadTexture(
//                "Textures/Tower/brick.jpg");
//        tex.setWrap(Texture.WrapMode.Repeat);
//        wallMaterial.setTexture("DiffuseMap", tex);
//        Texture normal =
//                game.getAssetManager().loadTexture(
//                "Textures/Tower/brick_normal.jpg");
//        normal.setWrap(Texture.WrapMode.MirroredRepeat);
//        
//        wallMaterial.setTexture("NormalMap", normal);


        wallMaterial = new Material(game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        wallMaterial.setBoolean("UseMaterialColors", true);
        wallMaterial.setColor("Specular", new ColorRGBA(ColorRGBA.Gray));

        if (wallMaterial.getMaterialDef().getName().equals("Phong Lighting")) {
            Texture t = game.getAssetManager().loadTexture("Textures/Shader/toon.png");
            t.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
            t.setMagFilter(Texture.MagFilter.Nearest);
            wallMaterial.setTexture("ColorRamp", t);
            wallMaterial.setColor("Diffuse", fadeColor = ColorRGBA.Gray.clone());
//                wallMaterial.setColor("Diffuse", new ColorRGBA(0.25f, 0.25f, 0.25f, 1.0f));
            wallMaterial.setBoolean("VertexLighting", true);
        }

        // Geometry
        float[] angles = {(float) -Math.PI / 2f, 0, 0};

        // Tower Model

        towerGeometry =
                game.getAssetManager().loadModel("Models/Towers/tower.j3o");
        towerGeometry.setMaterial(wallMaterial);
        towerGeometry.setLocalRotation(new Quaternion(angles));

        Node n = new Node("BatchNode");
        n.attachChild(towerGeometry);

        towerGeometry = GeometryBatchFactory.optimize(n);
        towerGeometry.setQueueBucket(Bucket.Translucent);

        // Roof
        Cylinder roof = new Cylinder(
                TOWER_SAMPLES,
                TOWER_SAMPLES,
                ROOF_SIZE, 0,
                ROOF_SIZE, false, false);

        roofGeometry = new Geometry(
                name + "_RoofGeometry", roof);
        roofGeometry.setMaterial(roofMaterial);
        roofGeometry.setLocalTranslation(0, TOWER_HEIGHT + ROOF_SIZE / 2, 0);
        roofGeometry.setLocalRotation(new Quaternion(angles));
        roofGeometry.setQueueBucket(Bucket.Inherit);


        // Wall
        Cylinder wall = new Cylinder(
                TOWER_SAMPLES,
                TOWER_SAMPLES,
                TOWER_SIZE,
                TOWER_HEIGHT,
                true);


        wallGeometry = new Geometry(
                name + "_WallGeometry", wall);
        wallGeometry.setMaterial(wallMaterial);
        wallGeometry.setLocalTranslation(0, TOWER_HEIGHT / 2, 0);
        wallGeometry.setLocalRotation(new Quaternion(angles));
        wallGeometry.setQueueBucket(Bucket.Inherit);

        // Hierarchy
//        clickableEntityNode.attachChild(wallGeometry);
//        clickableEntityNode.attachChild(roofGeometry);
        clickableEntityNode.attachChild(towerGeometry);
        // apply position to main node
        clickableEntityNode.setLocalTranslation(position);
        clickableEntityNode.setShadowMode(ShadowMode.Cast);

    }

    /**
     * Creates the bounding-volume for the range-checks.
     * @param game the MazeTDGame singleton
     */
    private void createCollision(MazeTDGame game) {

        Cylinder c = new Cylinder(
                TOWER_SAMPLES,
                TOWER_SAMPLES,
                towerRange,
                RANGE_CYLINDER_HEIGHT,
                true);

        collisionMaterial = new Material(game.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        collisionMaterial.setColor("Color", new ColorRGBA(1, 1, 1, 0.25f));
        collisionMaterial.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);

        float[] angles = {(float) Math.PI / 2, 0, 0};

        collisionCylinder = new Geometry("CollisionCylinderGeometry", c);
        collisionCylinder.setMaterial(collisionMaterial);
        collisionCylinder.setLocalTranslation(0, TOWER_RANGE_RADIUS_HEIGHT, 0);
        collisionCylinder.setLocalRotation(new Quaternion(angles));
        collisionCylinder.setQueueBucket(Bucket.Transparent);

        attackRangeCollisionNode.setLocalTranslation(position);
        attackRangeCollisionNode.attachChild(collisionCylinder);
    }

    /* (non-Javadoc)
     * @see entities.base.AbstractEntity#update(float)
     */
    @Override
    protected void update(float tpf) {
        // if dead and therfore decaying
        if (deacying) {
            decayTime += tpf;

            if (fadeColor.a >= 0.0f) {
                fadeColor.a -= tpf * 1f;

                wallMaterial.setColor("Ambient", fadeColor);   // ... color of this object
                wallMaterial.setColor("Diffuse", fadeColor);   // ... color of light being reflected
                towerGeometry.setMaterial(wallMaterial);
            }

            if (decayTime > TOWER_DECAY) {
                // finally destroy
                destroyed();
            }
            return;
        }
        // TODO: fix collision
        if (target == null) {
            // if there is no target atm search for it
            target = checkForRangedEnter();
            if (target != null) {
                target.setAttacker(this);
                attack(tpf);
            }
        } else if (!target.isDead()) {
            // if tower has target do damage
            if (checkForRangedLeave()) {
                return;
            }
            attack(tpf);
        } else if (target.isDead()) {
            target = null;
        }
        // refresheds the effects on orbs
        updateOrbEffects(tpf);
        // refreshes the orbs hovering over the tower
        updateOrbs(tpf);
    }

    /* (non-Javadoc)
     * @see entities.base.ClickableEntity#onClick()
     */
    @Override
    public void onClick() {
        System.out.println("You clicked tower: #" + getEntityId() + " - " + getName());
    }

    /* (non-Javadoc)
     * @see entities.base.ClickableEntity#onMouseOver()
     */
    @Override
    public void onMouseOver() {
        if (hoveredTower != null) {
            hoveredTower.getRangeCollisionNode().setCullHint(CullHint.Always);
        }
        attackRangeCollisionNode.setCullHint(CullHint.Never);
        hoveredTower = this;
    }

    /* (non-Javadoc)
     * @see entities.base.ClickableEntity#onMouseLeft()
     */
    @Override
    public void onMouseLeft() {
        hoveredTower = null;
        attackRangeCollisionNode.setCullHint(CullHint.Always);
    }

    /**
     * Updates the orbs attached to the tower every tick.
     * @param tpf the time-gap
     */
    private void updateOrbs(float tpf) {
//        orbRotation += tpf * TOWER_ORB_ROTATION_SPEED;
//        float[] angles = {0, orbRotation, 0};
//        Quaternion q = new Quaternion(angles);
//        orbNodeRot.setLocalRotation(q);
//        orbNodePos.setLocalTranslation(position);
//        if (firstOrb != null) {
//            firstOrb.update(tpf);
//        }
//        if (secondOrb != null) {
//            secondOrb.update(tpf);
//        }
//        if (thirdOrb != null) {
//            thirdOrb.update(tpf);
//        }
    }

    /**
     * Update orb effects.
     *
     * @param tpf the tpf
     */
    private void updateOrbEffects(float tpf) {
        for (AbstractOrbEffect e : orbEffects) {
            e.update(tpf);
        }
    }

    /**
     * Attacks a creep in a give time interval defined by 
     * <code>damageInterval</code> and <code>intervalCounter</code> and does 
     * <code>damage</code> amount of damage if the fired projectile hits the 
     * target.
     * @param tpf the time-gap
     */
    private void attack(float tpf) {
        intervalCounter += tpf;
        if (intervalCounter > damageInterval) {
            Random r = new Random(System.currentTimeMillis());
            float rand_damage = additionalDamage + r.nextFloat() * damageMax;

            Projectile p =
                    new Projectile(
                    name + "'s_projectile",
                    position.clone().setY(1.f),
                    target,
                    rand_damage,
                    projectileColor,
                    effectManager.getOrbEffects(firstOrb, secondOrb, thirdOrb));
            p.createNode(GAME);
            EntityManager.getInstance().addEntity(p);
            intervalCounter = 0;
        }
    }

    /**
     * Damages a tower by <code>amount</code> points.
     * @param amount the amount of received damaged
     */
    void applyDamage(float amount) {
        this.healthPoints -= amount;
        if (isDead() && !deacying) {
            onDestroy();
        }
    }

    /**
     * Needs to be called by the gui to signal that the player 
     * demolishes the tower and all resources need to be freed.
     */
    public void demolish() {
        this.healthPoints = 0;
        onDestroy();
    }

    /**
     * Is called if the tower is destroyed by a creep or 
     * demolished by the player.
     */
    private void onDestroy() {
        deacying = true;

        wallMaterial = new Material(GAME.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        wallMaterial.setBoolean("UseMaterialColors", true);
        wallMaterial.setColor("Specular", new ColorRGBA(ColorRGBA.Gray));
        wallMaterial.setColor("Diffuse", ColorRGBA.Gray.clone());
//        roofMaterial.setColor("Ambient", ColorRGBA.Red);
        wallMaterial.setColor("Ambient", ColorRGBA.Gray.clone());
        wallMaterial.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);

        if (firstOrb != null) {
            firstOrb.explodes();
        }
        if (secondOrb != null) {
            secondOrb.explodes();
        }
        if (thirdOrb != null) {
            thirdOrb.explodes();
        }

        if (TowerSelection.getInstance().selectedTower.equals(this)) {
            TowerSelection.getInstance().detachFromTower();
            Player.getInstance().setSelectedTower(null);
        }

        towerGeometry.setQueueBucket(Bucket.Translucent);
        towerGeometry.setShadowMode(ShadowMode.Off);
        towerGeometry.setMaterial(wallMaterial);
    }

    /**
     * Is called if the tower is finally out of the scene and 
     * all resources need to be freed.
     */
    private void destroyed() {
        EntityManager.getInstance().removeEntity(id);
        ScreenRayCast3D.getInstance().removeClickableObject(clickableEntityNode);
        Level.getInstance().
                getStaticLevelElements().detachChild(attackRangeCollisionNode);
        Level.getInstance().
                getDynamicLevelElements().detachChild(orbNodePos);
        Level.getInstance().removeTower(square);
        square.setTower(null);

    }

    /**
     * Checks if a creep entered the range of the tower and sets target
     * if found a creep.
     *
     * @return the creep
     */
    private Creep checkForRangedEnter() {
        // collide with the current collidables
        CollisionResults collisionResults =
                Collider3D.getInstance().objectCollides(
                attackRangeCollisionNode.getWorldBound());
        // if there are collidables
        if (collisionResults != null) {
            Node n;
            ArrayList<Creep> creeps = new ArrayList<Creep>();
            // find each and
            for (CollisionResult result : collisionResults) {
                n = result.getGeometry().getParent();
                // check if collidable entity
                if (n instanceof CollidableEntityNode) {
                    CollidableEntityNode col = (CollidableEntityNode) n;
                    AbstractEntity e = col.getEntity();
                    // check if its a creep entity
                    if (e instanceof Creep) {
                        Creep c = (Creep) e;
                        if (!c.isDead() && !creeps.contains(c)) {
                            // Creep was found
                            creeps.add(c);
                        }
                    }
                }
            }
            // if no creep was found return
            if (creeps.isEmpty()) {
                return null;
            }

            // else sort the creeps by distance to the tower
            Collections.sort(creeps, new Comparator<Creep>() {

                @Override
                public int compare(Creep o1, Creep o2) {
                    float dist1 = o1.getPosition().subtract(position).length();
                    float dist2 = o2.getPosition().subtract(position).length();

                    if (dist1 < dist2) {
                        return -1;
                    } else if (dist1 > dist2) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
            // and return the closest
            return creeps.get(0);
        }
        return null;
    }

    /**
     * Checks if the targeted creep is still in range.
     * @return true if in range, false otherwise
     */
    private boolean checkForRangedLeave() {
        // look if still in range
        float dist = target.getPosition().subtract(position).length();
        if (dist > towerRange + Creep.CREEP_GROUND_RADIUS) {
            // tower is no more attacking

            //projectile.destroyProjectile();
            target.setAttacker(null);
            target = null;
            intervalCounter = 0;
            return true;
        }
        return false;
    }

    /**
     * Gets the collison node and bounding-volume for range checks.
     * @return the node containing the bv
     */
    public Node getRangeCollisionNode() {
        return attackRangeCollisionNode;
    }

    /**
     * Sets the creep-target for the tower.
     *
     * @param target the new target
     */
    void setTarget(Creep target) {
        this.target = target;
    }

    /**
     * Checks if the tower is below zero health.
     * @return true if healthPoints below or at zero, false otherwise
     */
    public boolean isDead() {
        return healthPoints <= 0;
    }

    /**
     * Gets the current HP of the tower,.
     *
     * @return the numeric value of the HP
     */
    public float getHealthPoints() {
        return healthPoints;
    }

    /**
     * Gets the initial HP of a tower.
     * @return the maximum value of HP a tower can have
     */
    public float getMaxHealthPoints() {
        return maxHealthPoints;
    }

    /**
     * Sets the new HP of a tower, both max and current HP to the new value.
     * @param maxHealthPoints the new HP of the tower
     */
    public void setMaxHealthPoints(float maxHealthPoints) {
        this.healthPoints = maxHealthPoints;
        this.maxHealthPoints = maxHealthPoints;
    }

    /**
     * gets the towers attack rate, means the time between two attacks.
     * @return the current rate
     */
    public float getAttackRate() {
        return damageInterval;
    }

    /**
     * Applys a new attack-rate to the tower for the next attacks.
     * @param attackRate the new rate
     */
    public void setAttackRate(float attackRate) {
        this.damageInterval = attackRate;
    }

    /**
     * Gets the current range of the tower in OpenGL world distance.
     * @return the current range
     */
    public float getTowerRange() {
        return towerRange;
    }

    /**
     * Gets the first orb placed in this tower.
     *
     * @return the orb in slot 0
     */
    public Orb getFirstOrb() {
        return firstOrb;
    }

    /**
     * Gets the second orb placed in this tower.
     *
     * @return the orb in slot 1
     */
    public Orb getSecondOrb() {
        return secondOrb;
    }

    /**
     * Gets the third orb placed in this tower.
     *
     * @return the orb in slot 2
     */
    public Orb getThirdOrb() {
        return thirdOrb;
    }

    /**
     * Sets the new range of a tower. and calculates new collision-volume.
     * @param towerRange the new range
     */
    public void setTowerRange(float towerRange) {
        this.towerRange = towerRange;
        attackRangeCollisionNode.detachAllChildren();
        createCollision(GAME);
    }

    /**
     * Attaches a tower-orb-effect on target.
     * @param e the effect to attach
     */
    public void addOrbEffect(AbstractOrbEffect e) {
        orbEffects.add(e);
        e.onStart(this);
    }

    /**
     * Removes an effect from the tower, e.g. if 
     * orb is removed or tower is destroyed.
     * @param e the effect to remove
     */
    public void removeOrbEffect(AbstractOrbEffect e) {
        e.onEnd(this);
        orbEffects.remove(e);
    }

    /**
     * Refreshes all OrbEffects attached to the tower.
     * First removes all and than recalculates the Orb levels and 
     * finally attaches the orb-effects to tower.
     */
    private void refreshTowerOrbEffects() {
        ArrayList<AbstractOrbEffect> clone = new ArrayList<AbstractOrbEffect>(orbEffects);
        for (AbstractOrbEffect e : clone) {
            removeOrbEffect(e);
        }
        AbstractOrbEffect[] effects = effectManager.getOrbEffects(firstOrb, secondOrb, thirdOrb);
        for (AbstractOrbEffect aoe : effects) {
            if (aoe != null && OrbEffectManager.isTowerElement(aoe.getElementType())) {
                addOrbEffect(aoe);
            }
        }
    }

    /**
     * Please use replaceOrb().
     * Places an orb at the next free position (max. three orbs.
     *
     * @param type the desired orb-type to add
     * @param slot the slot
     */
    @Deprecated
    public void placeOrb(Orb.ElementType type, int slot) {
        switch (slot) {
            case 1:
                if (secondOrb == null) {
                    secondOrb = createTowerOrb(type, slot);
                }
                break;
            case 2:
                if (thirdOrb == null) {
                    thirdOrb = createTowerOrb(type, slot);
                }
                break;
            case 0:
            default:
                if (firstOrb == null) {
                    firstOrb = createTowerOrb(type, slot);
                }
                break;
        }
        // refreshes the towers orb effects if there are some
        refreshTowerOrbEffects();
        // recalculates the projectiles and its particles color
        calculateProjectileColor();
    }

    /**
     * Replaces a tower orb by its number and a new orb-type to use instead.
     * @param replaceType the type of orb to use instead the current one at 
     * position slot
     * @param slot the number of orb to replace range from 0-2 incl.
     * @return the removed orb-type if the orb was replaced and the 
     * given replacement type otherwise
     */
    public ElementType replaceOrb(ElementType replaceType, int slot) {
        ElementType removedOrbType = null;
        switch (slot) {
            case 0:
                if (firstOrb != null) {
                    removedOrbType = firstOrb.getElementType();
                    orbNodeRot.detachChild(firstOrb.getClickableEntityNode());
                    firstOrb = createTowerOrb(replaceType, slot);

                } else {
                    firstOrb = createTowerOrb(replaceType, slot);
                    removedOrbType = null;
                }
                break;
            case 1:
                if (secondOrb != null) {
                    removedOrbType = secondOrb.getElementType();
                    orbNodeRot.detachChild(secondOrb.getClickableEntityNode());
                    secondOrb = createTowerOrb(replaceType, slot);

                } else {
                    secondOrb = createTowerOrb(replaceType, slot);
                    removedOrbType = null;
                }
                break;
            case 2:
                if (thirdOrb != null) {
                    removedOrbType = thirdOrb.getElementType();
                    orbNodeRot.detachChild(thirdOrb.getClickableEntityNode());
                    thirdOrb = createTowerOrb(replaceType, slot);

                } else {
                    thirdOrb = createTowerOrb(replaceType, slot);
                    removedOrbType = null;
                }
                break;
            default:
                removedOrbType = null;
        }

        // refreshes the towers orb effects if there are some
        refreshTowerOrbEffects();
        // recalculates the projectiles and its particles color
        calculateProjectileColor();
        return removedOrbType;

    }

    /**
     * Calculates the color of the towers projectiles according to the current
     * orbs placed.
     */
    private void calculateProjectileColor() {
        ColorRGBA[] colors = new ColorRGBA[3];
        if (firstOrb != null) {
            colors[0] = firstOrb.getOrbColor();
        }
        if (secondOrb != null) {
            colors[1] = secondOrb.getOrbColor();
        }
        if (thirdOrb != null) {
            colors[2] = thirdOrb.getOrbColor();
        }

        ColorRGBA newColor = new ColorRGBA(0, 0, 0, 1);

        ArrayList<ColorRGBA> colorList = new ArrayList<ColorRGBA>();

        for (ColorRGBA c : colors) {
            if (c != null) {
                colorList.add(c);
            }
        }

        if (colorList.size() > 0) {
            float multi = 1.f / colorList.size();
            for (ColorRGBA c : colorList) {
                newColor.addLocal(c.mult(multi));
            }
        }
        newColor.clamp();
        projectileColor = newColor;
    }

    /**
     * Created an orb for the tower.
     *
     * @param type the type to create
     * @param slot the slot
     * @return the created orb
     */
    private Orb createTowerOrb(Orb.ElementType type, int slot) {
        Orb o;
        switch (slot) {
            case 0:
                o = new Orb(name + "Orb_1",
                        new Vector3f(
                        0,
                        0,
                        -TOWER_SIZE + 0.0f),
                        type);
                break;
            case 1:
                o = new Orb(name + "Orb_2",
                        new Vector3f(
                        0,
                        TOWER_HEIGHT - 0.7f,
                        -TOWER_SIZE + 0.025f),
                        type);
                break;
            case 2:
            default:
                o = new Orb(name + "Orb_3",
                        new Vector3f(
                        0,
                        TOWER_HEIGHT - 0.4f,
                        -TOWER_SIZE + 0.05f),
                        type);
                break;
        }
        orbNodeRot.attachChild(o.createNode(GAME));
        o.applyTowerOrbSize();
//        o.applyTowerOrbMaterial();
        return o;
    }
    //==========================================================================
    //===   Inner Classes
    //==========================================================================

    /**
     * The class TowerSelection is a singleton that displays the selection-
     * square for the selected tower.
     * @author Hans Ferchland
     */
    public static final class TowerSelection extends Node {
        //==========================================================================
        //===   Singleton
        //==========================================================================

        /**
         * The hidden constructor of TowerSelection.
         */
        private TowerSelection() {
            super("TowerSelection");
            createGeometry(GAME);
        }

        /**
         * The static method to retrive the one and only instance of TowerSelection.
         *
         * @return single instance of TowerSelection
         */
        public static TowerSelection getInstance() {
            return TowerSelectionHolder.INSTANCE;
        }

        /**
         * The holder-class TowerSelectionHolder for the TowerSelection.
         */
        private static class TowerSelectionHolder {

            /** The Constant INSTANCE. */
            private static final TowerSelection INSTANCE = new TowerSelection();
        }
        //==========================================================================
        //===   Private Fields
        //==========================================================================
        /** The selected tower. */
        private Tower selectedTower;
        
        /** The material. */
        private Material material;
        
        /** The color. */
        private ColorRGBA color = new ColorRGBA(ColorRGBA.Green);
        
        /** The height. */
        private float height = 0.2f;
        //==========================================================================
        //===   Methods
        //==========================================================================

        /**
         * Attaches the TowerSelection to a tower.
         * @param t the tower where the selection should be attached
         */
        public void attachToTower(Tower t) {
            selectedTower = t;
            selectedTower.clickableEntityNode.attachChild(this);
        }

        /**
         * Detaches the selection from the current tower.
         */
        public void detachFromTower() {
            if (selectedTower != null) {
                selectedTower.clickableEntityNode.detachChild(this);
            }
        }

        /**
         * Creates the geometry for the TowerSelection indicator.
         * @param game the reference to the MazeTDGame app
         * @return the TowerSelection-node instance
         */
        private Node createGeometry(MazeTDGame game) {

            // selection Material        
            material = new Material(
                    game.getAssetManager(),
                    "Common/MatDefs/Misc/Unshaded.j3md");
            material.setColor("Color", color);
            material.setColor("GlowColor", color);
            material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

            Vector3f tl = new Vector3f(-0.5f, height, 0.5f);
            Vector3f tr = new Vector3f(0.5f, height, 0.5f);
            Vector3f bl = new Vector3f(-0.5f, height, -0.5f);
            Vector3f br = new Vector3f(0.5f, height, -0.5f);

            Vector3f tl2 = new Vector3f(-0.51f, height, 0.51f);
            Vector3f tr2 = new Vector3f(0.51f, height, 0.51f);
            Vector3f bl2 = new Vector3f(-0.51f, height, -0.51f);
            Vector3f br2 = new Vector3f(0.51f, height, -0.51f);

            Line[] lines = new Line[8];
            lines[0] = new Line(tl, tr);
            lines[1] = new Line(bl, br);
            lines[2] = new Line(tl, bl);
            lines[3] = new Line(tr, br);
            lines[4] = new Line(tl2, tr2);
            lines[5] = new Line(bl2, br2);
            lines[6] = new Line(tl2, bl2);
            lines[7] = new Line(tr2, br2);


            for (Line l : lines) {
                Geometry line = new Geometry("line1", l);
                line.setMaterial(material);
                line.setLocalTranslation(0, 0, 0);
                line.setQueueBucket(Bucket.Transparent);
                line.setShadowMode(ShadowMode.Off);
                this.attachChild(line);
            }
            return this;
        }
    }
}
