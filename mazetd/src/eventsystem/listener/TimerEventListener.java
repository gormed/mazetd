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
 * File: TimerEventListener.java
 * Type: events.listener.TimerEventListener
 * 
 * Documentation created: 22.05.2012 - 21:52:23 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package eventsystem.listener;

import eventsystem.events.TimerEvent;
import java.util.EventListener;

/**
 * The listener interface TimerEventListener for receiving timed events.
 * <p>
 * The class that is interested in processing a timed event implements this
 * interface, and the object created with that class is registered with a
 * component using the applications
 * <code>Application.getInstance().addTimedObject(this)</code> method. When the
 * timed event occurs, that object's appropriate method
 * <code>onTimedEvent</code> is invoked.
 * </p>
 * 
 * <p>
 * A timed event can occur every millisecond, but screen-drawing is really
 * expensive, so 10 milliseconds should be the lowest value! Just define a field
 * for your event-period e.g. <code>private int period;</code> and return that
 * value in the <code>getPeriod()</code> method.
 * </p>
 * 
 * @author Hans Ferchland
 * @see TimerEvent
 * @see EventListener
 */
public interface TimerEventListener extends EventListener {

    /**
     * On timed event that will be executed all getPeriod()-milliseconds.
     * 
     * @param t
     *            the t
     */
    public void onTimedEvent(TimerEvent t);

    /**
     * Gets the period. This is the times to tick the listener wants to hear.
     * e.g. 100 for 10 times per second. Every base tick occurs every
     * millisecond.
     * 
     * @return the period
     */
    public float getPeriod();
}
