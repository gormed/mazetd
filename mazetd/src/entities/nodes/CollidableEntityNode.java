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
 * File: CollidableNode.java
 * Type: entities.nodes.CollidableNode
 * 
 * Documentation created: 23.05.2012 - 19:21:47 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.nodes;

import com.jme3.collision.CollisionResults;
import entities.base.CollidableEntity;
import eventsystem.interfaces.Collidable3D;

/**
 * The class CollidableEntityNode which has as child all geometry that will be
 * collidable.
 * @author Hans Ferchland
 * @version 0.2
 */
public class CollidableEntityNode extends EntityNode implements Collidable3D {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    /** The entity. */
    protected CollidableEntity entity;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Constructs a entity node that will be collidable.
     * @param name the deseired node-name
     * @param entity the entity connected to the collidable
     */
    public CollidableEntityNode(String name, CollidableEntity entity) {
        super(entity, name);
        this.entity = entity;
    }

    /* (non-Javadoc)
     * @see eventsystem.interfaces.Collidable3D#onCollision3D(com.jme3.collision.CollisionResults)
     */
    @Override
    public void onCollision3D(CollisionResults collisionResults) {
        entity.onCollision(collisionResults);
    }

    /* (non-Javadoc)
     * @see entities.nodes.EntityNode#getEntity()
     */
    @Override
    public CollidableEntity getEntity() {
        return entity;
    }
}
