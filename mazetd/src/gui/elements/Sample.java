/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.elements;

import entities.Creep;
import eventsystem.events.CreepEvent;
import eventsystem.listener.CreepListener;

/**
 *
 * @author $VPG000-AS97GL8M4U71
 */
public class Sample implements CreepListener {

    @Override
    public void onAction(CreepEvent e) {
        if (e.getType().equals(CreepEvent.CreepEventType.ReachedEnd)) {
            Creep c = e.getCreep();
        }
    }
    
}
