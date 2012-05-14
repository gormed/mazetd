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
 * time it is occurred and the firing object, the application.
 * </p>
 * 
 * @author Hans Ferchland
 */
public class TimerEvent extends EventObject {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    /** The execution time. */
    float executionTime = 0;

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
        executionTime = time;
    }

    /**
     * Gets the execution time.
     * 
     * @return the execution time
     */
    public float getExecutionTime() {
        return executionTime;
    }
}