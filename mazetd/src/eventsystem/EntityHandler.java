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
 * File: EnitiyHandler.java
 * Type: eventsystem.EnitiyHandler
 * 
 * Documentation created: 23.05.2012 - 16:19:33 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package eventsystem;

import com.jme3.collision.CollisionResult;
import com.jme3.math.Vector2f;
import entities.base.AbstractEntity;
import eventsystem.events.EntityEvent;
import eventsystem.listener.EntityListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * The class EntityHandler
 * @author Hans Ferchland
 */
public class EntityHandler {
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of EnitiyHandler.
     */
    private EntityHandler() {
    }

    /**
     * The static method to retrive the one and only instance of EnitiyHandler.
     */
    public static EntityHandler getInstance() {
        return EnitiyHandlerHolder.INSTANCE;
    }

    /**
     * The holder-class EnitiyHandlerHolder for the EnitiyHandler.
     */
    private static class EnitiyHandlerHolder {

        private static final EntityHandler INSTANCE = new EntityHandler();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private HashMap<AbstractEntity, HashSet<EntityListener>> entityListeners =
            new HashMap<AbstractEntity, HashSet<EntityListener>>();
    //==========================================================================
    //===   Methods
    //==========================================================================

    /**
     * Adds a EntityListener for any given set of AbstractEntity.
     * @param entityListener the listener
     */
    void addEntityListener(EntityListener entityListener, AbstractEntity... entitys) {

        for (AbstractEntity e : entitys) {
            HashSet<EntityListener> listeners;
            if (!entityListeners.containsKey(e)) {
                listeners = new HashSet<EntityListener>();
                listeners.add(entityListener);
                entityListeners.put(e, listeners);
            } else {
                listeners = entityListeners.get(e);
                listeners.add(entityListener);
            }
        }
    }

    void removeEntityListener(EntityListener listener) {
        EntityListener remove = null;
        for (Map.Entry<AbstractEntity, HashSet<EntityListener>> entry : entityListeners.entrySet()) {
            for (EntityListener entityListener : entry.getValue()) {
                if (entityListener.equals(listener)) {
                    remove = entityListener;
                    break;
                }
            }
            if (remove != null) {
                entry.getValue().remove(remove);
                remove = null;
            }
        }
    }

    //==========================================================================
    //===   Invocation Methods
    //==========================================================================
    /**
     * Invokes a given entity-event for a given entity. 
     * <p>
     * See EntityEventType in EntityEvent for more. Should only be called for 
     * special purposes; if an enity is clicked, killed, destructed or created. 
     * 
     * Whatever you do use this with care!
     * </p>
     * <p>
     * Have in mind that new events can be created if you add your type to the
     * enum EntityEventType and implement the usage of this event. Means that 
     * you have to call it in a special case, e.g. if a unit is at zero HP or
     * whatever. Hand over special variables as you wish, extend the EnityEvent
     * what you like (new constructor of fields+getter). You may also extend even
     * this function or class if necessary for calling the event.
     * </p>
     * 
     * @param actionType the desired event type to be fired
     * @param entity the firing entity
     * @param mouse the mouse position if its a mouse-event otherwise null
     * @param result the collision result if its a mouse-event or 
     * collision event
     * @see EntityEvent
     * @see EntityEvent.EntityEventType
     * @see ClickableEntity
     * @see ClickableEntity.ClickableEntityNode
     */
    public void invokeEntityAction(
            EntityEvent.EntityEventType actionType, AbstractEntity entity,
            Vector2f mouse, CollisionResult result) {
        if (entityListeners.isEmpty()) {
            return;
        }
        EntityEvent event = new EntityEvent(entity, actionType, mouse, result);

        if (entityListeners.containsKey(entity)) {
            HashSet<EntityListener> listeners = entityListeners.get(entity);
            for (EntityListener l : listeners) {
                l.onAction(event);
            }
        }
    }
}
