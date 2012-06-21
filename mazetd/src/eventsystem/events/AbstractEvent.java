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
 * File: AbstractEvent.java
 * Type: events.AbstractEvent
 * 
 * Documentation created: 13.05.2012 - 23:22:39 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package eventsystem.events;

import java.util.EventObject;

/**
 * The class AbstractEvenet for all events in MazeTD.
 * @author Hans Ferchland
 */
public abstract class AbstractEvent extends EventObject {
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
    /**
     * The events unique id.
     */
    private int eventID = getContiniousEventID();
    //==========================================================================
    //===   Constructor & Methods
    //==========================================================================
    /**
     * Creates an event.
     * @param source the firing object.
     */
    public AbstractEvent(Object source) {
        super(source);
    }

    /**
     * Returns the event-id of the event.
     * @return the events id
     */
    public int getEventID() {
        return eventID;
    }
}
