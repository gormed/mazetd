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
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Cylinder;
import entities.base.AbstractEntity;
import entities.base.ClickableEntity;
import entities.base.EntityManager;
import entities.effects.AbstractOrbEffect;
import entities.nodes.CollidableEntityNode;
import eventsystem.port.Collider3D;
import eventsystem.port.ScreenRayCast3D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import logic.Level;
import mazetd.MazeTDGame;

/**
 * The class Tower for a basical tower in MazeTD.
 * @author Hady Khalifa & Hans Ferchland
 * @version 0.6
 */
public class Tower extends ClickableEntity {

    //==========================================================================
    //===   Constants
    //========================================================================== 
    public static final float TOWER_BASE_DAMAGE_INTERVAL = 1.2f;
    public static final int TOWER_BASE_DAMAGE = 5;
    public static final int TOWER_BASE_RANGE = 3;
    public static final int TOWER_DECAY = 2;
    public static final int TOWER_HP = 500;
    public static final float TOWER_ORB_ROTATION_SPEED = 0.5f;
    public static final float TOWER_RANGE_RADIUS_HEIGHT = 0.15f;
    private static final float RANGE_CYLINDER_HEIGHT = 0.01f;
    private static final int TOWER_SAMPLES = 15;
    public static final float TOWER_HEIGHT = 1.0f;
    public static final float TOWER_SIZE = 0.3f;
    private static final float ROOF_SIZE = 0.35f;
    private static final MazeTDGame GAME = MazeTDGame.getInstance();
    private static Tower hoveredTower = null;
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    //visual
    private Geometry roofGeometry;
    private Geometry wallGeometry;
    private Geometry collisionCylinder;
    private Material roofMaterial;
    private Material wallMaterial;
    private Material collisionMaterial;
    private ColorRGBA projectileColor = new ColorRGBA(0.5f, 0.5f, 0.5f, 1f);
    private Vector3f position;
    private boolean deacying = false;
    private float decayTime = 0;
    //logic
    private int towerRange = TOWER_BASE_RANGE;
    private Creep target;
    private Map.MapSquare square;
    private float healthPoints = TOWER_HP;
    private float maxHealthPoints = TOWER_HP;
    private float damage = TOWER_BASE_DAMAGE;
    private float damageInterval = TOWER_BASE_DAMAGE_INTERVAL;
    private float intervalCounter = 0;
    private Orb firstOrb;
    private Orb secondOrb;
    private Orb thirdOrb;
    private Node orbNodePos;
    private Node orbNodeRot;
    private float orbRotation = 0;
    private ArrayList<AbstractOrbEffect> orbEffects =
            new ArrayList<AbstractOrbEffect>();
    //jme3
    private Node attackRangeCollisionNode;
    //particle
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Contructor of a basical tower for MazeTD.
     * @param name the name of the tower
     * @param position the desired position
     */
    public Tower(String name, Map.MapSquare square) {
        super(name);
        this.square = square;
    }

    @Override
    public Node createNode(MazeTDGame game) {
        super.createNode(game);
        createGeometry(game);
        // create collision for tower attacking range
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

        wallMaterial = new Material(
                game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        wallMaterial.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        wallMaterial.setColor("Specular", ColorRGBA.White);
        wallMaterial.setColor("Ambient", ColorRGBA.Gray);   // ... color of this object
        wallMaterial.setColor("Diffuse", ColorRGBA.Gray);   // ... color of light being reflected

        // Geometry
        float[] angles = {(float) Math.PI / 2, 0, 0};
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
        clickableEntityNode.attachChild(wallGeometry);
        clickableEntityNode.attachChild(roofGeometry);
        // apply position to main node
        clickableEntityNode.setLocalTranslation(position);
        clickableEntityNode.setShadowMode(ShadowMode.CastAndReceive);

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
        collisionMaterial.setColor("Color", new ColorRGBA(1, 0, 0, 0.075f));
        collisionMaterial.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);

        float[] angles = {(float) Math.PI / 2, 0, 0};

        collisionCylinder = new Geometry("CollisionCylinderGeometry", c);
        collisionCylinder.setMaterial(collisionMaterial);
        collisionCylinder.setLocalTranslation(0, TOWER_RANGE_RADIUS_HEIGHT, 0);
        collisionCylinder.setLocalRotation(new Quaternion(angles));
        collisionCylinder.setQueueBucket(Bucket.Transparent);

        attackRangeCollisionNode =
                new Node("AttackCollisionCylinderNode");
        attackRangeCollisionNode.setLocalTranslation(position);
        attackRangeCollisionNode.attachChild(collisionCylinder);
    }

    @Override
    protected void update(float tpf) {
        // if dead and therfore decaying
        if (deacying) {
            decayTime += tpf;
            if (decayTime > TOWER_DECAY) {
                // finally destroy
                destroyed();
            }
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
        updateOrbs(tpf);
    }

    @Override
    public void onClick() {
        System.out.println("You clicked tower: #" + getEntityId() + " - " + getName());
    }

    @Override
    public void onMouseOver() {
        if (hoveredTower != null) {
            hoveredTower.getRangeCollisionNode().setCullHint(CullHint.Always);
        }
        attackRangeCollisionNode.setCullHint(CullHint.Never);
        hoveredTower = this;
    }

    @Override
    public void onMouseLeft() {
        hoveredTower = null;
        attackRangeCollisionNode.setCullHint(CullHint.Always);
    }

    private void updateOrbs(float tpf) {
        orbRotation += tpf * TOWER_ORB_ROTATION_SPEED;
        float[] angles = {0, orbRotation, 0};
        Quaternion q = new Quaternion(angles);
        orbNodeRot.setLocalRotation(q);
        orbNodePos.setLocalTranslation(position);

        if (firstOrb != null) {
            firstOrb.update(tpf);
        }
        if (secondOrb != null) {
            secondOrb.update(tpf);
        }
        if (thirdOrb != null) {
            thirdOrb.update(tpf);
        }
    }

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
            Projectile p =
                    new Projectile(
                    name + "'s_projectile",
                    position.clone().setY(1.f),
                    target,
                    damage,
                    projectileColor,
                    getOrbEffects());
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

    private void demolish() {
        this.healthPoints = 0;
        onDestroy();
    }

    private void onDestroy() {
        deacying = true;
        roofMaterial.setColor("Ambient", ColorRGBA.Red);
        wallMaterial.setColor("Ambient", ColorRGBA.Red);
    }

    private void destroyed() {
        EntityManager.getInstance().removeEntity(id);
        ScreenRayCast3D.getInstance().removeClickableObject(clickableEntityNode);
        Level.getInstance().removeTower(square);

        Level.getInstance().getDynamicLevelElements().detachChild(orbNodePos);
        square.setTower(null);

    }

    /**
     * Checks if a creep entered the range of the tower and sets target 
     * if found a creep.
     */
    private Creep checkForRangedEnter() {
        // collide with the current collidables
        CollisionResults collisionResults =
                Collider3D.getInstance().objectCollides(
                attackRangeCollisionNode.getWorldBound());
        // if there are creeps
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
                    // check if creep entity
                    if (e instanceof Creep) {
                        Creep c = (Creep) e;
                        if (!c.isDead() && !creeps.contains(c)) {
                            // Creep was found
                            creeps.add(c);
                            //System.out.println(c.getName() + " was detected.");
                        }
                    }
                }
            }
            if (creeps.isEmpty()) {
                return null;
            }

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
     * @param target 
     */
    void setTarget(Creep target) {
        this.target = target;
    }

    public boolean isDead() {
        return healthPoints <= 0;
    }

    public float getHealthPoints() {
        return healthPoints;
    }

    public float getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public void setMaxHealthPoints(float maxHealthPoints) {
        this.healthPoints = maxHealthPoints;
        this.maxHealthPoints = maxHealthPoints;
    }

    public void addOrbEffect(AbstractOrbEffect e) {
        orbEffects.add(e);
        e.onStart(this);
    }

    public void removeOrbEffect(AbstractOrbEffect e) {
        e.onEnd(this);
        orbEffects.remove(e);
    }

    /**
     * Places an orb at the next free position (max. three orbs.
     * @param type the desired orb-type to add
     */
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
        calculateProjectileColor();
    }

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

        for (ColorRGBA c : colors) {
            if (c != null) {
                newColor.addLocal(c);
                //newColor.clamp();
            }
        }
        newColor.clamp();
        projectileColor = newColor;
    }

    /**
     * Created an orb for the tower.
     * @param type
     * @return 
     */
    private Orb createTowerOrb(Orb.ElementType type, int slot) {
        Orb o;
        switch (slot) {
            case 0:
                o = new Orb(name + "Orb_1",
                        new Vector3f(
                        TOWER_SIZE,
                        TOWER_HEIGHT + 0.1f,
                        -TOWER_SIZE / 2),
                        type);
                break;
            case 1:
                o = new Orb(name + "Orb_2",
                        new Vector3f(
                        -TOWER_SIZE,
                        TOWER_HEIGHT + 0.1f,
                        -TOWER_SIZE / 2),
                        type);
                break;
            case 2:
            default:
                o = new Orb(name + "Orb_3",
                        new Vector3f(
                        0,
                        TOWER_HEIGHT + 0.1f,
                        TOWER_SIZE),
                        type);
                break;
        }
        orbNodeRot.attachChild(o.createNode(GAME));
        return o;
    }

    /**
     * Replaces a tower orb by its number and a new orb-type to use instead.
     * @param replaceType the type of orb to use instead the current one at 
     * position orbNumber
     * @param slot the number of orb to replace range from 0-2 incl.
     * @return the removed orb-type if the orb was replaced and the 
     * given replacement type otherwise
     */
    public Orb.ElementType replaceOrb(Orb.ElementType replaceType, int slot) {
        switch (slot) {
            case 0:
                if (firstOrb != null) {
                    Orb.ElementType type = firstOrb.getElementType();
                    orbNodeRot.detachChild(firstOrb.getClickableEntityNode());
                    firstOrb = createTowerOrb(replaceType, slot);
                    orbNodeRot.attachChild(firstOrb.createNode(GAME));
                    calculateProjectileColor();
                    return type;
                }
                return replaceType;
            case 1:
                if (secondOrb != null) {
                    Orb.ElementType type = secondOrb.getElementType();
                    orbNodeRot.detachChild(secondOrb.getClickableEntityNode());
                    secondOrb = createTowerOrb(replaceType, slot);
                    orbNodeRot.attachChild(secondOrb.createNode(GAME));
                    calculateProjectileColor();
                    return type;
                }
                return replaceType;
            case 2:
                if (thirdOrb != null) {
                    Orb.ElementType type = thirdOrb.getElementType();
                    orbNodeRot.detachChild(thirdOrb.getClickableEntityNode());
                    thirdOrb = createTowerOrb(replaceType, slot);
                    orbNodeRot.attachChild(thirdOrb.createNode(GAME));
                    calculateProjectileColor();
                    return type;
                }
                return replaceType;
            default:

                return replaceType;
        }
    }

    private AbstractOrbEffect[] getOrbEffects() {
        ArrayList<AbstractOrbEffect> effects =
                new ArrayList<AbstractOrbEffect>();
        Orb.ElementType[] elements = new Orb.ElementType[3];
        int[] orbTypeCount = {0, 0, 0, 0, 0};


        if (firstOrb != null) {
            elements[0] = firstOrb.getElementType();
        }
        if (secondOrb != null) {
            elements[1] = secondOrb.getElementType();
        }
        if (thirdOrb != null) {
            elements[2] = thirdOrb.getElementType();
        }


        for (Orb.ElementType e : elements) {
            if (e != null) {
                orbTypeCount[e.ordinal()]++;
            }
        }

        for (int i = 0; i < orbTypeCount.length; i++) {
            if (orbTypeCount[i] > 0 && orbTypeCount[i] <= 3) {
                effects.add(
                        AbstractOrbEffect.getOrbEffect(
                        Orb.ElementType.values()[i], orbTypeCount[i] - 1));
            }
        }

        AbstractOrbEffect[] effectArray = new AbstractOrbEffect[3];
        return effects.toArray(effectArray);
    }
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
