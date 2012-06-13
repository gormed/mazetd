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
 * File: CreepHandler.java
 * Type: eventsystem.CreepHandler
 * 
 * Documentation created: 01.06.2012 - 16:15:49 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package eventsystem;

import entities.Creep;
import entities.Tower;
import eventsystem.events.CreepEvent;
import eventsystem.events.EntityEvent;
import eventsystem.listener.CreepListener;
import eventsystem.listener.EntityListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author Hans Ferchland
 */
public class CreepHandler {
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of CreepHandler.
     */
    private CreepHandler() {
    }

    /**
     * The static method to retrive the one and only instance of CreepHandler.
     */
    public static CreepHandler getInstance() {
        return CreepHandlerHolder.INSTANCE;
    }

    /**
     * The holder-class CreepHandlerHolder for the CreepHandler.
     */
    private static class CreepHandlerHolder {

        private static final CreepHandler INSTANCE = new CreepHandler();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private HashMap<Creep, HashSet<CreepListener>> creepListeners =
            new HashMap<Creep, HashSet<CreepListener>>();
    //==========================================================================
    //===   Methods
    //==========================================================================

    /**
     * Adds a CreepListener for any given set of Creep.
     * @param creepListener the listener
     */
    void addCreepListener(CreepListener creepListener, Creep... creeps) {
        for (Creep c : creeps) {
            HashSet<CreepListener> listeners;
            if (!creepListeners.containsKey(c)) {
                listeners = new HashSet<CreepListener>();
                listeners.add(creepListener);
                creepListeners.put(c, listeners);
            } else {
                listeners = creepListeners.get(c);
                listeners.add(creepListener);
            }
        }
    }

    void removeCreepListener(CreepListener listener) {
        CreepListener remove = null;
        for (Map.Entry<Creep, HashSet<CreepListener>> entry :
                creepListeners.entrySet()) {
            for (CreepListener entityListener : entry.getValue()) {
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
    //===   Inner Classes
    //==========================================================================

    public void invokeCreepAction(
            CreepEvent.CreepEventType actionType, Creep creep,
            Tower target) {
        if (creepListeners.isEmpty()) {
            return;
        }
        CreepEvent event = new CreepEvent(actionType, creep, target);

        if (creepListeners.containsKey(creep)) {
            HashSet<CreepListener> listeners = creepListeners.get(creep);
            for (CreepListener l : listeners) {
                l.onAction(event);
            }
        }
        if (creepListeners.containsKey(null)) {
            HashSet<CreepListener> listeners = creepListeners.get(null);
            for (CreepListener l : listeners) {
                l.onAction(event);
            }
        }
    }
}
