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
 * File: CollisionHandler.java
 * Type: eventsystem.CollisionHandler
 * 
 * Documentation created: 23.05.2012 - 21:04:33 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package eventsystem;

import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import eventsystem.events.CollisionEvent;
import eventsystem.listener.CollisionListener;
import eventsystem.port.Collider3D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * The class CollisionHandler for the handling of the collision-events.
 * @author Hans Ferchland
 * @version 0.2
 */
public class CollisionHandler {
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of CollisionHandler.
     */
    private CollisionHandler() {
    }

    /**
     * The static method to retrive the one and only instance of CollisionHandler.
     */
    public static CollisionHandler getInstance() {
        return CollisionHandlerHolder.INSTANCE;
    }

    /**
     * The holder-class CollisionHandlerHolder for the CollisionHandler.
     */
    private static class CollisionHandlerHolder {

        private static final CollisionHandler INSTANCE = new CollisionHandler();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private HashMap<BoundingVolume, HashSet<CollisionListener>> collisionListeners =
            new HashMap<BoundingVolume, HashSet<CollisionListener>>();
    private Collider3D collider3D = Collider3D.getInstance();
    //==========================================================================
    //===   Methods
    //==========================================================================

    /**
     * Adds a CollisionListener to the manager that listens to collision-events
     * from the list of BoundingVolumes given.
     * @param listener the listener to add
     * @param boundingVolumes the list of bv to listen to
     */
    void addCollisionListener(CollisionListener listener,
            BoundingVolume... boundingVolumes) {
        HashSet<CollisionListener> listeners;
        for (BoundingVolume c : boundingVolumes) {
            if (!collisionListeners.containsKey(c)) {
                listeners = new HashSet<CollisionListener>();
                listeners.add(listener);
                collisionListeners.put(c, listeners);
            } else {
                listeners = collisionListeners.get(c);
                listeners.add(listener);
            }
        }
    }

    /**
     * Removes a CollisionListener from the manager and the according 
     * BoundingVolumes if not listened to by other listeners.
     * @param listener the listener to remove
     */
    void removeCollisionListener(CollisionListener listener) {
        ArrayList<BoundingVolume> remove = new ArrayList<BoundingVolume>();

        HashSet<CollisionListener> listeners;
        for (Map.Entry<BoundingVolume, HashSet<CollisionListener>> e :
                collisionListeners.entrySet()) {
            listeners = e.getValue();
            if (listeners.contains(listener)) {
                listeners.remove(listener);
                if (listeners.isEmpty()) {
                    remove.add(e.getKey());
                }
            }
        }
        
        for (BoundingVolume bv : remove) {
            collisionListeners.remove(bv);
        }
    }

    /**
     * Updated the collision listeners by invoking a collision for each 
     * BoundingVolume.
     * @param tpf the time-gap
     */
    void update(float tpf) {
        for (Map.Entry<BoundingVolume, HashSet<CollisionListener>> e :
                collisionListeners.entrySet()) {
            invokeCollisionEvent(e.getKey(), e.getValue());
        }
    }

    /**
     * Invokes a collision-event for a given BoundingVolume. If a collision
     * happened all listeners are called.
     * @param boundingVolume the bv to check for
     * @param collisionListeners the set of listeners
     */
    private void invokeCollisionEvent(BoundingVolume boundingVolume, HashSet<CollisionListener> collisionListeners) {
        CollisionResults r;
        collider3D.objectCollides(boundingVolume);
        r = collider3D.getCurrentCollisionResults();

        //collidable.onCollision3D(r);

        CollisionEvent e = new CollisionEvent(boundingVolume, r);

        for (CollisionListener cl : collisionListeners) {
            cl.onCollision(e);
        }
    }
}
