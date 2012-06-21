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
 * File: TimerHandler.java
 * Type: eventsystem.TimerHandler
 * 
 * Documentation created: 23.05.2012 - 16:19:24 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package eventsystem;

import eventsystem.events.TimerEvent;
import eventsystem.listener.TimerEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The class TimerHandler that handles all time events through the update call.
 * @author Hans Ferchland
 */
public class TimerHandler {
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of TimerHandler.
     */
    private TimerHandler() {
    }

    /**
     * The static method to retrive the one and only instance of TimerHandler.
     */
    public static TimerHandler getInstance() {
        return TimerHandlerHolder.INSTANCE;
    }

    /**
     * The holder-class TimerHandlerHolder for the TimerHandler.
     */
    private static class TimerHandlerHolder {

        private static final TimerHandler INSTANCE = new TimerHandler();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private HashMap<TimerEventListener, Float> timerEventListeners =
            new HashMap<TimerEventListener, Float>();
    private ArrayList<TimerEventListener> removedEventListeners =
            new ArrayList<TimerEventListener>();
    private ArrayList<TimerEventListener> addedEventListeners =
            new ArrayList<TimerEventListener>();

    //==========================================================================
    //===   Methods
    //==========================================================================
    /**
     * Updates all timer events added to the manager/handler.
     * @param tpf the time-gap
     */
    private void updateTimerEvents(float tpf) {
        TimerEvent t = new TimerEvent(this, tpf);


        for (TimerEventListener l : addedEventListeners) {
            timerEventListeners.put(l, 0f);
        }
        addedEventListeners.clear();
        for (TimerEventListener l : removedEventListeners) {
            timerEventListeners.remove(l);
        }
        removedEventListeners.clear();

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
     * Updates handler in general and the timer-events in special.
     * @param tpf the time-gap between two update ticks.
     */
    void update(float tpf) {
        updateTimerEvents(tpf);
    }

    /**
     * Adds a TimerEventListener.
     * @param l the desired listener
     */
    void addTimerEventListener(TimerEventListener l) {
        addedEventListeners.add(l);
    }

    /**
     * Removes a TimerEventListener.
     * @param l the desired listener
     */
    void removeTimerEventListener(TimerEventListener l) {
        removedEventListeners.add(l);
    }
}
