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
 * File: EntityEvent.java
 * Type: events.EntityEvent
 * 
 * Documentation created: 22.05.2012 - 21:46:30 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package eventsystem.events;

import com.jme3.collision.CollisionResult;
import com.jme3.math.Vector2f;
import entities.base.AbstractEntity;

/**
 * The class EntityEvent for all events of an entity.
 * @author Hans Ferchland
 * @version 1.0
 */
public class EntityEvent extends AbstractEvent {
    
    /**
     * The type of entity-event, that happened.
     */
    public enum EntityEventType {

        /** The Click. */
        Click,
        
        /** The Mouse over. */
        MouseOver,
        
        /** The Mouse left. */
        MouseLeft
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /** The entity. */
    private AbstractEntity entity;
    
    /** The event type. */
    private EntityEventType eventType;
    
    /** The mouse. */
    private Vector2f mouse;
    
    /** The result. */
    private CollisionResult result;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Creates a new entity-event.
     * @param source the firing source
     * @param entity the entity that invoked the event
     * @param eventType the type of event that happened
     */
    public EntityEvent(Object source, AbstractEntity entity, EntityEventType eventType) {
        super(source);
        this.entity = entity;
        this.eventType = eventType;
    }

    /**
     * Creates a new entity-event.
     * @param entity the entity that invoked the event
     * @param eventType the type of event that happened
     * @param mouse the mouse position while the event happened
     * @param result the collision results of the ray-cast
     */
    public EntityEvent(
            AbstractEntity entity,
            EntityEventType eventType,
            Vector2f mouse,
            CollisionResult result) {
        super(entity);
        this.entity = entity;
        this.eventType = eventType;
        this.mouse = mouse;
        this.result = result;
    }

    /**
     * Gets the entity that invoked the event.
     * @return the invoking entity
     */
    public AbstractEntity getEntity() {
        return entity;
    }

    /**
     * Gets the event type of the event.
     * @return the entity events type
     */
    public EntityEventType getEventType() {
        return eventType;
    }

    /**
     * Gets the events mouse-coordinate.
     *
     * @return the mouse
     */
    public Vector2f getMouse() {
        return mouse;
    }

    /**
     * Gets the events collision results.
     *
     * @return the result
     */
    public CollisionResult getResult() {
        return result;
    }
}
