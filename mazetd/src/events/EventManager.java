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
 * File: EventManager.java
 * Type: events.EventManager
 * 
 * Documentation created: 13.05.2012 - 23:22:39 by Hans
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package events;

import com.jme3.collision.CollisionResult;
import events.listener.KeyInputListener;
import events.listener.TimerEventListener;
import events.listener.MouseInputListener;
import com.jme3.input.InputManager;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector2f;
import entities.base.AbstractEntity;
import entities.base.ClickableEntity;
import events.listener.EntityListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * The class EventManager for all events.
 * @author Hans Ferchland
 * @version 0.3
 */
public class EventManager {
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of the singleton.
     */
    private EventManager() {
        this.inputManager = mazetd.MazeTDGame.getInstance().getInputManager();
        this.timerEventListeners = new HashMap<TimerEventListener, Float>(25);
        this.entityListeners = new HashMap<AbstractEntity, HashSet<EntityListener>>();
    }

    /**
     * Static method to retrieve the one and olny reference to the manager.
     * @return the reference of the EventManager
     */
    public static EventManager getInstance() {
        return EventManagerHolder.INSTANCE;
    }

    /**
     * Holder class for the EventManager
     */
    private static class EventManagerHolder {

        private static final EventManager INSTANCE = new EventManager();
    }
    //==========================================================================
    //===   Static
    //==========================================================================
    /** The running eventid for all events */
    private static int runningEventID = 0;

    /**
     * Gets the next eventID. This function increments the eventID by each call.
     * There will never be a doubled eventid!
     * @return the next free and unused eventID
     */
    static int getContiniousEventID() {
        return runningEventID++;
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    //private HashMap<Integer, AbstractEvent> eventMap;
    private HashMap<TimerEventListener, Float> timerEventListeners;
    private HashMap<AbstractEntity, HashSet<EntityListener>> entityListeners;
    private InputManager inputManager;
    //==========================================================================
    //===   Methods
    //==========================================================================

    /**
     * Updates all timer events added to the manager.
     * @param tpf 
     */
    private void updateTimerEvents(float tpf) {
        TimerEvent t = new TimerEvent(this, tpf);
        for (Map.Entry<TimerEventListener, Float> entry : timerEventListeners.entrySet()) {
            // get us the times called and the listener itself
            float time = entry.getValue();

            // after that, raise the entry-value because 
            // we went further
            entry.setValue(time + tpf);
            TimerEventListener l = entry.getKey();
            // if the time has come we call it
            if (time >= l.getPeriod()) {
                l.onTimedEvent(t);
                entry.setValue(0f);
            }
        }
    }

    /**
     * Updates manager in general and the timer-events in special.
     * @param tpf the time-gap between two update ticks.
     */
    public void update(float tpf) {
        updateTimerEvents(tpf);
    }

    /**
     * Adds a TimerEventListener.
     * @param l the desired listener
     */
    public void addTimerEventListener(TimerEventListener l) {
        timerEventListeners.put(l, 0f);
    }

    /**
     * Removes a TimerEventListener.
     * @param l the desired listener
     */
    public void removeTimerEventListener(TimerEventListener l) {
        timerEventListeners.remove(l);
    }

    /**
     * Adds a key-input-mapping to the input-manager.
     * @param mapping the key-name of the mappig of the triggers
     * @param keyTriggers the triggers that will trigger 
     * the mapping for the listeners
     */
    public void addKeyInputEvent(String mapping, KeyTrigger... keyTriggers) {
        inputManager.addMapping(mapping, keyTriggers);
    }

    /**
     * Adds a mouse-button-mapping to the input-manager.
     * @param mapping the key-name of the mappig of the triggers
     * @param mouseButtonTriggers the triggers that will trigger 
     * the mapping for the listeners
     */
    public void addMouseButtonEvent(String mapping, MouseButtonTrigger... mouseButtonTriggers) {
        inputManager.addMapping(mapping, mouseButtonTriggers);
    }

    /**
     * Adds a mouse-movement-mapping to the input-manager.
     * @param mapping the key-name of the mappig of the triggers
     * @param mouseAxisTriggers the triggers that will trigger 
     * the mapping for the listeners
     */
    public void addMouseMovementEvent(String mapping, MouseAxisTrigger... mouseAxisTriggers) {
        inputManager.addMapping(mapping, mouseAxisTriggers);
    }

    /**
     * Adds a KeyInputListener for one or more mappings.
     * @param inputListener the listener to add
     * @param mappings the mappings to hear for the listener
     */
    public void addKeyInputListener(KeyInputListener inputListener, String... mappings) {
        inputManager.addListener(inputListener, mappings);
    }
    /**
     * Removes a KeyInputListener for all its mappings.
     * @param inputListener the listener to remove
     */
    public void removeKeyInputListener(KeyInputListener inputListener) {
        inputManager.removeListener(inputListener);
    }

    /**
     * Adds a MouseInputListener for one or more mappings.
     * @param inputListener the listener to add
     * @param mappings the mappings to hear for the listener
     */
    public void addMouseInputListener(MouseInputListener inputListener, String... mappings) {
        inputManager.addListener(inputListener, mappings);
    }

    /**
     * Removes a MouseInputListener for all its mappings.
     * @param inputListener the listener to remove
     */
    public void removeMouseInputListener(MouseInputListener inputListener) {
        inputManager.removeListener(inputListener);
    }

    /**
     * Adds a EntityListener any entity-event.
     * @param entityListener the listener
     */
    public void addEntityListener(EntityListener entityListener, AbstractEntity... entitys) {

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
     * this function if necessary for calling the event.
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
        if (entityListeners.isEmpty())
            return;
        EntityEvent event = new EntityEvent(this, entity, actionType, mouse, result);
        
        if (entityListeners.containsKey(entity)) {
            HashSet<EntityListener> listeners = entityListeners.get(entity);
            for (EntityListener l : listeners) {
                l.onAction(event);
            }
        }
    }
}
