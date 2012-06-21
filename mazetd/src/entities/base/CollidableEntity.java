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
 * File: CollidableEntity.java
 * Type: entities.base.CollidableEntity
 * 
 * Documentation created: 23.05.2012 - 16:45:05 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.base;

import com.jme3.collision.CollisionResults;
import entities.base.low.SimpleCollidable;
import entities.nodes.CollidableEntityNode;
import mazetd.MazeTDGame;

/**
 * The class CollidableEntity for entities that will be collidable with others.
 * @author Hans Ferchland
 * @version 0.2
 */
public abstract class CollidableEntity extends AbstractEntity implements SimpleCollidable {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /** The collidable entity node. */
    protected CollidableEntityNode collidableEntityNode;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Contructs a collidable entity.
     * @param name the desired name of the entity
     */
    public CollidableEntity(String name) {
        super(name);
    }

    /* (non-Javadoc)
     * @see entities.base.AbstractEntity#update(float)
     */
    @Override
    protected abstract void update(float tpf);

    /* (non-Javadoc)
     * @see entities.base.AbstractEntity#createNode(mazetd.MazeTDGame)
     */
    @Override
    public CollidableEntityNode createNode(MazeTDGame game) {

        collidableEntityNode =
                new CollidableEntityNode(name + "s_GeometryNode", this);
        super.createNode(game).attachChild(collidableEntityNode);

        return collidableEntityNode;
    }

    /* (non-Javadoc)
     * @see entities.base.low.SimpleCollidable#getCollidableEntityNode()
     */
    @Override
    public CollidableEntityNode getCollidableEntityNode() {
        return collidableEntityNode;
    }

    /* (non-Javadoc)
     * @see entities.base.low.SimpleCollidable#onCollision(com.jme3.collision.CollisionResults)
     */
    @Override
    public abstract void onCollision(CollisionResults collisionResults);
}
