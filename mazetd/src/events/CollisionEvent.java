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
 * File: CollisionEvent.java
 * Type: events.CollisionEvent
 * 
 * Documentation created: 22.05.2012 - 23:56:25 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package events;

import com.jme3.collision.Collidable;

/**
 * The class CollisionEvent.
 * @author Hans Ferchland
 * @version
 */
public class CollisionEvent extends AbstractEvent {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Collidable self;
    private Collidable with;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    public CollisionEvent(Object source, Collidable self, Collidable with) {
        super(source);
        this.self = self;
        this.with = with;
    }

    public Collidable getSelf() {
        return self;
    }

    public Collidable getWith() {
        return with;
    }
}
