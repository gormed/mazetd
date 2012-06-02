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
 * File: CreepEvent.java
 * Type: eventsystem.events.CreepEvent
 * 
 * Documentation created: 01.06.2012 - 16:16:55 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package eventsystem.events;

import entities.Creep;
import entities.Tower;

/**
 * The class CreepEvent.
 * @author Hans Ferchland
 * @version
 */
public class CreepEvent extends AbstractEvent {
    public enum CreepEventType {
        Death,
        ReachedEnd,
        Attacks,
        Created
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private CreepEventType type;
    private Tower target;
    private Creep creep;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    public CreepEvent(CreepEventType type, Creep creep, Tower target) {
        super(creep);
        this.creep = creep;
        this.target = target;
        this.type = type;
    }
    
    public Creep getCreep() {
        return creep;
    }

    public Tower getTarget() {
        return target;
    }

    public CreepEventType getType() {
        return type;
    }
}
