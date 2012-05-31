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
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import entities.base.AbstractEntity;
import entities.base.ClickableEntity;
import entities.nodes.CollidableEntityNode;
import eventsystem.interfaces.Collidable3D;
import eventsystem.port.Collider3D;
import logic.Level;
import mazetd.MazeTDGame;

/**
 * The class Tower.
 * @author Hady Khalifa & Hans Ferchland
 * @version 0.3
 */
public class Tower extends ClickableEntity {

    //==========================================================================
    //===   Constants
    //========================================================================== 
    public static final float TOWER_BASE_DAMAGE_INTERVAL = 1.0f;
    public static final int TOWER_BASE_DAMAGE = 5;
    public static final int TOWER_BASE_RANGE = 10;
    public static final int TOWER_HP = 500;
    private static final float RANGE_CYLINDER_HEIGHT = 0.025f;
    private static final int TOWER_SAMPLES = 20;
    private static final float TOWER_HEIGHT = 1.4f;
    private static final float TOWER_SIZE = 0.5f;
    private static final float ROOF_SIZE = 0.6f;
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
    private Projectile projectile;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

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
        } else {
            if (projectile != null)
                projectile.destroyProjectile();
        }
    }

    private void attack(float tpf) {
        if (projectile != null) {
            projectile.update(tpf);
        } else {
            projectile = new Projectile(name + "'s_projectile", position.clone().setY(2f));
            Level.getInstance().getDynamicLevelElements().attachChild(projectile);

        }
//        intervalCounter += tpf;
//        if (intervalCounter > damageInterval) {
//            System.out.println(target.getName() + " recieved " + damage + " damage!");
//            target.receiveDamaged(damage);
//            intervalCounter = 0;
//        }
    }

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

    public Node getRangeCollisionNode() {
        return attackRangeCollisionNode;
    }

    void setTarget(Creep target) {
        this.target = target;
    }
    //==========================================================================
    //===   Inner Classes
    //==========================================================================

    private class Projectile extends Node {

        public static final float PROJECTILE_BASE_SPEED = 3.f;
        private Geometry geometry;
        private float speed = PROJECTILE_BASE_SPEED;
        private Vector3f position;

        public Projectile(String name, Vector3f position) {
            super(name);
            this.position = position;
            createGeometry();

        }

        private void createGeometry() {
            Sphere s = new Sphere(5, 5, 0.1f);

            geometry = new Geometry("ProjectileGeometry", s);
            geometry.setMaterial(projectileMaterial);
            this.attachChild(geometry);
            this.setLocalTranslation(position);
        }

        public void update(float tpf) {
            if (target != null && !target.isDecaying()) {
                move(tpf);
                checkForHit();
            } else if (target.isDecaying()) {
                
            }
        }

        private void move(float tpf) {
            Vector3f targetPos = target.getPosition();
            Vector3f projectilePos = this.getLocalTranslation();

            Vector3f dir = targetPos.subtract(projectilePos);
            dir.normalizeLocal();
            dir.multLocal(speed * tpf);

            this.setLocalTranslation(projectilePos.add(dir));
        }

        private void onHit() {
            target.receiveDamaged(damage);
            destroyProjectile();
        }

        private void destroyProjectile() {
            Level.getInstance().getDynamicLevelElements().detachChild(this);
            projectile = null;
        }

        private void checkForHit() {
            CollisionResults collisionResults =
                    Collider3D.getInstance().objectCollides(this.getWorldBound());
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
