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

import eventsystem.interfaces.Collidable3D;
import eventsystem.listener.CollisionListener;
import java.util.HashMap;
import java.util.HashSet;

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
    private HashMap<Collidable3D, HashSet<CollisionListener>> collisionListeners;
    //==========================================================================
    //===   Methods
    //==========================================================================

    public void addCollisionListener(
            CollisionListener listener, Collidable3D... collidable3Ds) {
        HashSet<CollisionListener> listeners;
        for (Collidable3D c : collidable3Ds) {
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
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
