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
 * File: ClickableEntity.java
 * Type: entities.base.ClickableEntity
 * 
 * Documentation created: 22.05.2012 - 20:57:41 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.base;

import com.jme3.collision.CollisionResult;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import events.EntityEvent.EntityEventType;
import events.EventManager;
import events.raycast.RayCast3DNode;
import mazetd.MazeTDGame;

/**
 * The class ClickableEntity.
 * @author Hans Ferchland
 * @version 
 */
public abstract class ClickableEntity extends AbstractEntity {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    protected ClickableEntityNode clickableGeometryNode;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public ClickableEntity(String name) {
        super(name);
    }

    @Override
    public Node createGeometryNode(MazeTDGame game) {
        clickableGeometryNode = 
                new ClickableEntityNode(name + "s_GeometryNode", this);
        return clickableGeometryNode;
    }

    @Override
    public ClickableEntityNode getGeometryNode() {
        return clickableGeometryNode;
    }

    /**
     * Is called if geometry is clicked.
     */
    public abstract void onClick();

    /**
     * Is called if geometry is hovered.
     */
    public abstract void onMouseOver();

    /**
     * Is called if geometry is not hovered anymore.
     */
    public abstract void onMouseLeft();
    //==========================================================================
    //===   Inner Classes
    //==========================================================================

    /**
     * The class ClickableEntityNode for all the entitys geometry 
     * that will be clickable.
     * @author Hans Ferchland
     */
    public class ClickableEntityNode extends Node implements RayCast3DNode {

        private ClickableEntity entity;
        
        /**
         * Contructor of the node for the clickable geometry.
         * @param name the name of the node
         */
        public ClickableEntityNode(String name, ClickableEntity entity) {
            super(name);
            this.entity = entity;
        }

        @Override
        public void onRayCastClick(Vector2f mouse, CollisionResult result) {
            onClick();
            EventManager.getInstance().invokeEntityAction(
                    EntityEventType.Click, entity, mouse, result);
        }

        @Override
        public void onRayCastMouseOver(Vector2f mouse, CollisionResult result) {
            onMouseOver();
            EventManager.getInstance().invokeEntityAction(
                    EntityEventType.MouseOver, entity, mouse, result);
        }

        @Override
        public void onRayCastMouseLeft(Vector2f mouse, CollisionResult result) {
            onMouseLeft();
            EventManager.getInstance().invokeEntityAction(
                    EntityEventType.MouseLeft, entity, mouse, result);
        }

        public ClickableEntity getEntity() {
            return entity;
        }
    }
}
