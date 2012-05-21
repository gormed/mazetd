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

import events.listener.KeyInputListener;
import events.listener.TimerEventListener;
import events.listener.MouseInputListener;
import com.jme3.input.InputManager;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import java.util.HashMap;
import java.util.Map;

/**
 * The class EventManager for all events.
 * @author Hans Ferchland
 * @version 0.1
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
        this.timerEventListener = new HashMap<TimerEventListener, Float>(25);
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
    private HashMap<TimerEventListener, Float> timerEventListener;
    private InputManager inputManager;
    //==========================================================================
    //===   Methods
    //==========================================================================

    private void updateTimerEvents(float tpf) {
        TimerEvent t = new TimerEvent(this, tpf);
        for (Map.Entry<TimerEventListener, Float> entry : timerEventListener.entrySet()) {
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

    public void update(float tpf) {
        updateTimerEvents(tpf);
    }

    public void addTimerEventListener(TimerEventListener l) {
        timerEventListener.put(l, 0f);
    }

    public void removeTimerEventListener(TimerEventListener l) {
        timerEventListener.remove(l);
    }

    public void addKeyInputEvent(String mapping, KeyTrigger... keyTriggers) {
        inputManager.addMapping(mapping, keyTriggers);
    }

    public void addMouseButtonEvent(String mapping, MouseButtonTrigger... mouseButtonTriggers) {
        inputManager.addMapping(mapping, mouseButtonTriggers);
    }

    public void addMouseMovementEvent(String mapping, MouseAxisTrigger... mouseAxisTriggers) {
        inputManager.addMapping(mapping, mouseAxisTriggers);
    }

    public void addKeyInputListener(KeyInputListener inputListener, String... mappings) {
        inputManager.addListener(inputListener, mappings);
    }

    public void addMouseInputListener(MouseInputListener inputListener, String... mappings) {
        inputManager.addListener(inputListener, mappings);
    }
}
