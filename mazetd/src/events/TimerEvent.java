/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package events;

import java.util.EventObject;

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