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
 * File: SimpleCollidable.java
 * Type: entities.base.SimpleCollidable
 * 
 * Documentation created: 23.05.2012 - 20:13:04 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.base.low;

import com.jme3.collision.CollisionResults;
import entities.nodes.CollidableEntityNode;

/**
 * The interface SimpleCollidable for all objects that collide in MazeTD.
 * @author Hans Ferchland
 */
public interface SimpleCollidable {
    
    /**
     * Gets the entity node with the collsion-geometry for a collision-check.
     * @return the node with the collision-geometry
     */
    public CollidableEntityNode getCollidableEntityNode();
    
    /**
     * This method is called if the object is in a collision with another 
     * object.
     * @param collisionResults the results of the collision just happened
     */
    public void onCollision(CollisionResults collisionResults);

}
