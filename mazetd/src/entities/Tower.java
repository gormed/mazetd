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
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import entities.base.AbstractEntity;
import entities.base.ClickableEntity;
import entities.base.EntityManager;
import entities.nodes.CollidableEntityNode;
import eventsystem.port.Collider3D;
import logic.Level;
import mazetd.MazeTDGame;

/**
 * The class Tower for a basical tower in MazeTD.
 * @author Hady Khalifa & Hans Ferchland
 * @version 0.4
 */
public class Tower extends ClickableEntity {

    //==========================================================================
    //===   Constants
    //========================================================================== 
    public static final float TOWER_BASE_DAMAGE_INTERVAL = 1.0f;
    public static final int TOWER_BASE_DAMAGE = 0;
    public static final int TOWER_BASE_RANGE = 5;
    public static final int TOWER_HP = 500;
    private static final float RANGE_CYLINDER_HEIGHT = 0.025f;
    private static final int TOWER_SAMPLES = 15;
    private static final float TOWER_HEIGHT = 1.0f;
    private static final float TOWER_SIZE = 0.3f;
    private static final float ROOF_SIZE = 0.35f;
    private static final MazeTDGame GAME = MazeTDGame.getInstance();
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Geometry roofGeometry;
    private int towerRange = TOWER_BASE_RANGE;
    private Geometry wallGeometry;
    private Material roofMaterial;
    private Material wallMaterial;
    private Material projectileMaterial;
    private Vector3f position;
    private Creep target;
    private float healthPoints = TOWER_HP;
    private Node attackRangeCollisionNode;
    private Geometry collisionCylinder;
    private float damage = TOWER_BASE_DAMAGE;
    private float damageInterval = TOWER_BASE_DAMAGE_INTERVAL;
    private float intervalCounter = 0;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Contructor of a basical tower for MazeTD.
     * @param name the name of the tower
     * @param position the desired position
     */
    public Tower(String name, Vector3f position) {
        super(name);
        this.position = position;
    }

    @Override
    public Node createNode(MazeTDGame game) {
        super.createNode(game);

        // Materials
        projectileMaterial = new Material(
                game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        projectileMaterial.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        projectileMaterial.setColor("Specular", ColorRGBA.White);
        projectileMaterial.setColor("Ambient", ColorRGBA.DarkGray);   // ... color of this object
        projectileMaterial.setColor("Diffuse", ColorRGBA.DarkGray);   // ... color of light being reflected


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
        //roofGeometry.setQueueBucket(Bucket.Translucent);


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
        //wallGeometry.setQueueBucket(Bucket.Translucent);

        // Hierarchy
        clickableEntityNode.attachChild(wallGeometry);
        clickableEntityNode.attachChild(roofGeometry);
        // apply position to main node
        clickableEntityNode.setLocalTranslation(position);

        // create collision for tower attacking range
        createCollision(game);

        return clickableEntityNode;
    }

    @Override
    protected void update(float tpf) {
        // TODO: fix collision
        if (target == null) {
            // if there is no target atm search for it
            checkForRangedEnter();
        } else if (!target.isDecaying()) {
            // if tower has target do damage
            if (checkForRangedLeave()) {
                return;
            }
            attack(tpf);
        }
    }

    @Override
    public void onClick() {
        System.out.println("You clicked tower: #" + getEntityId() + " - " + getName());
    }

    @Override
    public void onMouseOver() {
    }

    @Override
    public void onMouseLeft() {
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
                    target);
            p.createNode(GAME);
            EntityManager.getInstance().addEntity(p);
            intervalCounter = 0;
        }
    }

    /**
     * Checks if a creep entered the range of the tower and sets target 
     * if found a creep.
     */
    private void checkForRangedEnter() {
        // collide with the current collidables
        CollisionResults collisionResults =
                Collider3D.getInstance().objectCollides(
                attackRangeCollisionNode.getWorldBound());
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
                        if (!c.isDead()) {
                            // Creep was found, so set it for both
                            c.setAttacker(this);
                            target = c;
                            //System.out.println(c.getName() + " was detected.");
                        }
                    }
                }
            }

        }
    }

    /**
     * Checks if the targeted creep is still in range.
     * @return true if in range, false otherwise
     */
    private boolean checkForRangedLeave() {
        // look if still in range
        float dist = target.getPosition().subtract(position).length();
        if (dist > towerRange) {
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

        Material m = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", new ColorRGBA(1, 0, 0, 0.0f));
        m.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        float[] angles = {(float) Math.PI / 2, 0, 0};

        collisionCylinder = new Geometry("CollisionCylinderGeometry", c);
        collisionCylinder.setMaterial(m);
        collisionCylinder.setLocalTranslation(0, 0.1f, 0);
        collisionCylinder.setLocalRotation(new Quaternion(angles));
        collisionCylinder.setQueueBucket(Bucket.Transparent);

        attackRangeCollisionNode =
                new Node("AttackCollisionCylinderNode");
        attackRangeCollisionNode.setLocalTranslation(position);
        attackRangeCollisionNode.attachChild(collisionCylinder);
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
    //==========================================================================
    //===   Inner Classes
    //==========================================================================

    /**
     * The class Projectile for the fired graphical and logical instance of a 
     * projectile fired by a tower.
     * @author Hans Ferchland
     */
    private class Projectile extends AbstractEntity {
        //==========================================================================
        //===   COnstants
        //==========================================================================

        public static final float PROJECTILE_BASE_SPEED = 3.f;
        //==========================================================================
        //===   Private Fields
        //==========================================================================
        private Geometry geometry;
        private float speed = PROJECTILE_BASE_SPEED;
        private Vector3f position;
        private Creep target;
        private float initialDistance = 0;
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
        public Projectile(String name, Vector3f position, Creep target) {
            super(name);
            this.target = target;
            this.position = position;
            this.initialDistance = target.getPosition().subtract(position).length();
        }

        @Override
        public void update(float tpf) {
            if (target != null) {
                move(tpf);
                checkForHit();
            }
            if (target == null || (target != null && target.isDead())) {
                destroy();
            }
        }

        @Override
        public Node createNode(MazeTDGame game) {
            super.createNode(game);

            createGeometry();
            Level.getInstance().getDynamicLevelElements().attachChild(geometryNode);
            return geometryNode;
        }

        /**
         * Creates the geometry for the Projectirl.
         */
        private void createGeometry() {
            Sphere s = new Sphere(5, 5, 0.1f);

            geometry = new Geometry("ProjectileGeometry", s);
            geometry.setMaterial(projectileMaterial);
            geometryNode.attachChild(geometry);
            geometryNode.setLocalTranslation(position);
        }

        /**
         * Moves the projectile each update call.
         * @param tpf the time-gap
         */
        private void move(float tpf) {


            float currentDistance = target.getPosition().subtract(position).length();
            float percentage = (initialDistance - currentDistance) / initialDistance;
            Vector3f targetPos = target.getPosition();
            Vector3f projectilePos = geometryNode.getLocalTranslation();

            Vector3f dir = targetPos.subtract(projectilePos);
            dir.normalizeLocal();
            dir.multLocal(speed * tpf);
            projectilePos.addLocal(dir);
            // TODO: add curved flying
            //projectilePos.y += (1-percentage);

            geometryNode.setLocalTranslation(projectilePos);
        }

        /*
         * Is called from the projectile if the target was hit. Does the 
         * proper damage and destroys the projectile.
         */
        private void onHit() {
            System.out.println(target.getName() + " recieved " + damage + " damage!");
            target.receiveDamaged(damage);
            destroy();
        }

        /**
         * Destroys the projectile due removing from entity-manager and
         * from dynamic level elements.
         */
        private void destroy() {
            EntityManager.getInstance().removeEntity(this.getEntityId());
            Level.getInstance().getDynamicLevelElements().detachChild(geometryNode);
        }

        /**
         * Checks for collision with the targeted creep and fires onHit().
         */
        private void checkForHit() {
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
                            }
                        }
                    }
                }

            }
        }
    }
}
