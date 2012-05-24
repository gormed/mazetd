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
 * The class CollidableEntity.
 * @author Hans Ferchland
 * @version
 */
public abstract class CollidableEntity extends AbstractEntity implements SimpleCollidable {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    protected CollidableEntityNode collidableEntityNode;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public CollidableEntity(String name) {
        super(name);
    }

    @Override
    protected abstract void update(float tpf);

    @Override
    public CollidableEntityNode createNode(MazeTDGame game) {

        collidableEntityNode =
                new CollidableEntityNode(name + "s_GeometryNode", this);
        super.createNode(game).attachChild(collidableEntityNode);

        return collidableEntityNode;
    }

    @Override
    public CollidableEntityNode getCollidableEntityNode() {
        return collidableEntityNode;
    }

    @Override
    public abstract void onCollision(CollisionResults collisionResults);
}
