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
 * File: CollisionEvent.java
 * Type: events.CollisionEvent
 * 
 * Documentation created: 22.05.2012 - 23:56:25 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package eventsystem.events;

import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;

/**
 * The class CollisionEvent that capsules the collision data for listeners.
 * @author Hans Ferchland
 * @version 1.0
 */
public class CollisionEvent extends AbstractEvent {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /** The self. */
    private Collidable self;
    
    /** The with. */
    private CollisionResults with;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    /**
     * Creates a new collision event.
     * @param self the object that collides
     * @param with the list of resulst of the collision
     */
    public CollisionEvent(Collidable self, CollisionResults with) {
        super(self);
        this.self = self;
        this.with = with;
    }

    /**
     * Gets the main actor in the collision.
     *
     * @return the main collidable
     */
    public Collidable getSelf() {
        return self;
    }

    /**
     * Gets the list of collision objects, the object collides with.
     * @return the collision results
     */
    public CollisionResults getWith() {
        return with;
    }
}
