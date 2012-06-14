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
 * File: TowerListener.java
 * Type: logic.TowerListener
 * 
 * Documentation created: 12.06.2012 - 23:04:12 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import entities.Orb.ElementType;
import entities.Tower;
import entities.base.AbstractEntity;
import eventsystem.EventManager;
import eventsystem.events.EntityEvent;
import eventsystem.events.EntityEvent.EntityEventType;
import eventsystem.listener.EntityListener;

/**
 * The class TowerListener.
 * @author Hans Ferchland
 * @version
 */
public class TowerListener implements EntityListener {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Tower selectedTower;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public TowerListener() {
        // a listener with null as entity to listenen listens to all events, hopefully
        EventManager.getInstance().addEntityListener(this, (AbstractEntity) null);
    }

    @Override
    public void onAction(EntityEvent entityEvent) {
        // get the entity
        AbstractEntity e = entityEvent.getEntity();
        // check if tower an clicked
        if (e instanceof Tower
                && entityEvent.getEventType().equals(EntityEventType.Click)) {
            // cast to tower
            Tower tower = (Tower) e;
            selectedTower = tower;
            System.out.println("Tower clicked:" + tower.getName());
        }

        ElementType e1 = ElementType.BLUE;

        int elementIDX = e1.ordinal();

        ElementType e2 = ElementType.values()[elementIDX];


    }
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
