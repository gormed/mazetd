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
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Cylinder;
import entities.Map.MapSquare;
import entities.base.CollidableEntity;
import entities.effects.OrbEffect;
import entities.nodes.CollidableEntityNode;
import eventsystem.port.Collider3D;
import java.util.HashSet;
import java.util.Queue;
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

    public static final float CREEP_BASE_SPEED = 1.1f;
    private static final float CREEP_GROUND_RADIUS = 0.25f;
    private static final float CREEP_HEIGHT = 0.5f;
    public static final int CREEP_MAX_HP = 100;
    private static final float CREEP_MIN_DISTANCE = 0.1f;
    private static final int CREEP_SAMPLES = 10;
    private static final float CREEP_TOP_RADIUS = 0.1f;
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Geometry geometry;
    private Material material;
    private float healthPoints = CREEP_MAX_HP;
    private float maxHealthPoints = CREEP_MAX_HP;
    private boolean deacying = false;
    private float decayTime = 0;
    private Tower attacker;
    private HashSet<OrbEffect> orbEffects = new HashSet<OrbEffect>();
    private Vector3f position;
    private Vector3f target;
    private float speed = CREEP_BASE_SPEED;
    private boolean moving = true;
    private Queue<Map.MapSquare> path;
    private MapSquare currentSquare;
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

    void start(Vector3f firstTarget) {
        moveTo(firstTarget);
    }

    @Override
    protected void update(float tpf) {
        if (deacying) {

            decayTime += tpf;
            if (decayTime > 5) {
                Collider3D.getInstance().removeCollisonObject(collidableEntityNode);
            }
        }
        // if moving do this part
        moveUpdate(tpf);
    }

    private void moveUpdate(float tpf) {
        if (moving) {
            position = collidableEntityNode.getLocalTranslation();

            Vector3f dir = target.subtract(position);
            float distance = dir.length();
            dir.normalizeLocal();
            dir.multLocal(speed * tpf);

            if (distance < CREEP_MIN_DISTANCE) {
                //position = target;
                // moving ended because the creep is at the target
                if (!path.isEmpty()) {
                    this.currentSquare = path.peek();
                    moveTo(path.poll().getLocalTranslation());
                } else {
                    moving = false;
                }
            } else {
                position.addLocal(dir);
            }
            collidableEntityNode.setLocalTranslation(position);
        }
    }

    @Override
    public void onCollision(CollisionResults collisionResults) {
    }

    @Override
    public CollidableEntityNode createNode(MazeTDGame game) {
        super.createNode(game);

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
        geometry.setLocalTranslation(0, CREEP_HEIGHT * 0.5f, 0);
        geometry.setLocalRotation(new Quaternion(angles));

        collidableEntityNode.attachChild(geometry);
        collidableEntityNode.setLocalTranslation(position);

        return collidableEntityNode;
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

    public void stop() {
        moving = false;
    }

    /**
     * Damages a creep by <code>amount</code> points.
     * @param amount the amount of receiveDamaged
     */
    void receiveDamaged(float amount) {
        this.healthPoints -= amount;
        if (isDead()) {
            deacying = true;
            // stop movement
            stop();
            // visualize the death
            material.setColor("Ambient", ColorRGBA.Red);
            // signal that the creep died
            attacker.setTarget(null);
        }
    }

    /**
     * Checks if the creep is dead
     * @return true if below or at 0 HP, false otherwise
     */
    public boolean isDead() {
        return healthPoints <= 0;
    }

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
     * Checks if a creep is dead/decaying.
     * @return true if dead, false otherwise
     */
    public boolean isDecaying() {
        return deacying;
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

    public void setPath(Queue<MapSquare> path) {
        this.path = path;
    }
    
    
    //==========================================================================
    //===   Inner Classes
    //==========================================================================

    public MapSquare getCurrentSquare() {
        return currentSquare;
    }
}
