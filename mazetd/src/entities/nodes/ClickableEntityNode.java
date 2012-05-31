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
 * File: ClickableNode.java
 * Type: entities.nodes.ClickableNode
 * 
 * Documentation created: 23.05.2012 - 19:21:34 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.nodes;

import com.jme3.collision.CollisionResult;
import com.jme3.math.Vector2f;
import entities.base.ClickableEntity;
import eventsystem.EntityHandler;
import eventsystem.events.EntityEvent.EntityEventType;
import eventsystem.interfaces.Clickable3D;

/**
 * The class ClickableEntityNode for all the entitys geometry 
 * that will be clickable.
 * @author Hans Ferchland
 */
public class ClickableEntityNode extends EntityNode implements Clickable3D {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    protected ClickableEntity entity;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    //==========================================================================
    //===   Inner Classes
    //==========================================================================

    /**
     * Contructor of the node for the clickable geometry.
     * @param name the deseired node-name
     */
    public ClickableEntityNode(String name, ClickableEntity entity) {
        super(entity, name);
        this.entity = entity;
    }

    @Override
    public void onRayCastClick(Vector2f mouse, CollisionResult result) {
        entity.onClick();
        EntityHandler.getInstance().invokeEntityAction(
                EntityEventType.Click, entity, mouse, result);
    }

    @Override
    public void onRayCastMouseOver(Vector2f mouse, CollisionResult result) {
        entity.onMouseOver();
        EntityHandler.getInstance().invokeEntityAction(
                EntityEventType.MouseOver, entity, mouse, result);
    }

    @Override
    public void onRayCastMouseLeft(Vector2f mouse, CollisionResult result) {
        entity.onMouseLeft();
        EntityHandler.getInstance().invokeEntityAction(
                EntityEventType.MouseLeft, entity, mouse, result);
    }

    @Override
    public ClickableEntity getEntity() {
        return entity;
    }
}
