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
 * File: Collidable3D.java
 * Type: eventsystem.interfaces.Collidable3D
 * 
 * Documentation created: 23.05.2012 - 20:53:42 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package eventsystem.interfaces;

import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import eventsystem.port.Collider3D;

/**
 * The interface Collidable3D marks any implementing object as collidable.
 * Those objects can be added to the Collider3D.
 * 
 * @author Hans Ferchland
 * 
 * @see Collider3D
 */
public interface Collidable3D extends Collidable {
    /**
     * This method is invoked by the Collider3D if the object was added to the 
     * Collider3D and a collision with this object happens.
     * @param collisionResults the results of the collision
     */
    public void onCollision3D(CollisionResults collisionResults);
}
