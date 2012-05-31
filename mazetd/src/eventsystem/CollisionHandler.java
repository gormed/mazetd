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
import eventsystem.interfaces.Collidable3D;
import eventsystem.listener.CollisionListener;
import eventsystem.port.Collider3D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The class CollisionHandler.
 * @author Hans Ferchland
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

    public void addCollisionListener(
            CollisionListener listener, BoundingVolume... boundingVolumes) {
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

    public void removeCollisionListener(
            CollisionListener listener) {
    }

    public void update(float tpf) {
        for (Map.Entry<BoundingVolume, HashSet<CollisionListener>> e : collisionListeners.entrySet()) {
            invokeCollisionEvent(e.getKey(), e.getValue());
        }
    }

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
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
