/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eventsystem.listener;

import eventsystem.events.TimerEvent;
import java.util.EventListener;

/**
 * The listener interface for receiving timed events.
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
