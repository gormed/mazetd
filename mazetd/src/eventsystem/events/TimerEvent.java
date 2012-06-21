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
 * File: TimerEvent.java
 * Type: events.events.TimerEvent
 * 
 * Documentation created: 22.05.2012 - 21:52:23 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package eventsystem.events;

/**
 * The Class TimedEvent is an Event special for timed things.
 * <p>
 * Occurs if a timed-control is listening for a special time. It stores the
 * time-gap when it is occurred and the firing object, the eventmanager.
 * </p>
 * 
 * @author Hans Ferchland
 */
public class TimerEvent extends AbstractEvent {

    /** The execution time. */
    float timeGap = 0;

    /**
     * Instantiates a new timed event.
     * 
     * @param source
     *            the source
     * @param time
     *            the time
     */
    public TimerEvent(Object source, float time) {
        super(source);
        timeGap = time;
    }

    /**
     * Gets the execution time.
     * 
     * @return the execution time
     */
    public float getTimeGap() {
        return timeGap;
    }
}