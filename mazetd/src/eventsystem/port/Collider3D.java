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
 * File: Collider3D.java
 * Type: eventsystem.port.Collider3D
 * 
 * Documentation created: 23.05.2012 - 21:47:57 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package eventsystem.port;

import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import eventsystem.interfaces.Collidable3D;
import mazetd.MazeTDGame;

/**
 * The class Collider3D is a singleton for 3D collision checks and firing 
 * the connected collision events.
 * @author Hans Ferchland
 */
public class Collider3D {
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of Collider3D.
     */
    private Collider3D() {
    }

    /**
     * The static method to retrive the one and only instance of Collider3D.
     */
    public static Collider3D getInstance() {
        return Collider3DHolder.INSTANCE;
    }

    /**
     * The holder-class Collider3DHolder for the Collider3D.
     */
    private static class Collider3DHolder {

        private static final Collider3D INSTANCE = new Collider3D();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Node collisionNode;
    private CollisionResults collisionResults;
    private BoundingVolume currentCollidable;
    private MazeTDGame game = MazeTDGame.getInstance();
    private boolean initialized;
    //==========================================================================
    //===   Methods
    //==========================================================================

    /**
     * Initializes the class if not already done or it was destroyed.
     */
    public void initialize() {
        if (initialized) {
            return;
        }
        collisionNode = new Node("Collision3DNode");
        game.getRootNode().attachChild(collisionNode);
        initialized = true;
    }

    /**
     * Destroys the class, removes all resources from jme3.
     */
    public void destroy() {
        if (!initialized) {
            return;
        }
        collisionNode.detachAllChildren();
        game.getRootNode().detachChild(collisionNode);
        collisionNode = null;
        initialized = false;
    }

    /**
     * Checks if the class was already initialized.
     * @return true if initialize false otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Adds a node to the collisionalbe 3d objects.
     * @param object that will be clickable
     */
    public void addCollisonObject(Spatial object) {
        collisionNode.attachChild(object);
    }

    /**
     * Removes a specific node from the collisionalbe 3d objects.
     * @param object that wont be clickable anymore
     */
    public void removeCollisonObject(Spatial object) {

        collisionNode.detachChild(object);
    }

    /**
     * Lets any BoundingVolume collide with all geometry that was added as
     * collision object to get a colliding node. The object that collides 
     * (the BoundingVolume) does not have to be added to the collider!
     * 
     * @param boundingVolume the BoundingVolume to check for collision
     * @return the jme3-results of the collision
     */
    public CollisionResults objectCollides(BoundingVolume boundingVolume) {
        currentCollidable = boundingVolume;

        collisionResults = new CollisionResults();

        //collisionNode.collideWith(boundingVolume, collisionResults);
        for (Spatial s : collisionNode.getChildren()) {
            s.collideWith(boundingVolume, collisionResults);
        }

        if (collisionResults.size() > 0) {
            if (boundingVolume instanceof Collidable3D) {
                ((Collidable3D) boundingVolume).onCollision3D(collisionResults);
            }
            return collisionResults;
        } else {
            return null;
        }
    }

    /**
     * Gets the last calculated collision results.
     * @return the last results
     */
    public CollisionResults getCurrentCollisionResults() {
        return collisionResults;
    }

    /**
     * Gets the last object (BoundingVolume) that collided.
     * @return the last results
     */
    public BoundingVolume getCurrentCollidable() {
        return currentCollidable;
    }

    /**
     * The Node where all objects are stored that can be collided with.
     * @return the jme3-node
     */
    public Node getCollisionNode() {
        return collisionNode;
    }
//    private void searchForCollidable3D(Spatial spatial, CollisionResult collisionResult) {
//        if (spatial != null) {
//            Spatial parent;
//            invokeCollision(spatial);
//            parent = spatial.getParent();
//            while (parent != null) {
//                invokeCollision(parent);
//                parent = parent.getParent();
//            }
//        }
//    }
//    
//    private void invokeCollision(Spatial spatial) {
//        if (spatial instanceof Collidable3D) {
//            Collidable3D c = (Collidable3D) spatial;
//            c.onCollision3D(collisionResults);
//        }
//    }
}
