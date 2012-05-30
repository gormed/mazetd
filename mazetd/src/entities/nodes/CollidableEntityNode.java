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

import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import entities.base.CollidableEntity;
import eventsystem.interfaces.Collidable3D;

/**
 * The class CollidableNode.
 * @author Hans Ferchland
 * @version
 */
public class CollidableEntityNode extends EntityNode implements Collidable3D {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    protected CollidableEntity entity;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public CollidableEntityNode(String name) {
        super(name);
    }

    public CollidableEntityNode(String name, CollidableEntity entity) {
        super(entity, name);
    }
    
    public CollidableEntity getEntity() {
        return entity;
    }

    @Override
    public void onCollision3D(CollisionResults collisionResults) {
        entity.onCollision(collisionResults);
    }

    @Override
    public int collideWith(Collidable other, CollisionResults results) {
        CollidableEntityNode collidableEntityNode = entity.getCollidableEntityNode();
        if (collidableEntityNode != null)
            return entity.getCollidableEntityNode().collideWith(other, results);
        else
            return 0;
    }
}
