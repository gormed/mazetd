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
 * File: EntityNode.java
 * Type: entities.nodes.EntityNode
 * 
 * Documentation created: 23.05.2012 - 19:24:16 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.nodes;

import com.jme3.scene.Node;
import entities.base.AbstractEntity;

/**
 * The class EntityNode is the base class of all nodes that carry geometry or 
 * bv or clickables for entities.
 * @author Hans Ferchland
 * @version 0.2
 */
public class EntityNode extends Node {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    /** The entity. */
    protected AbstractEntity entity;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Creates the entity-node by a name and an entity.
     * @param entity the desired entity to represent
     * @param name the deseired node-name
     */
    public EntityNode(AbstractEntity entity, String name) {
        super(name);
        this.entity = entity;
    }

    /**
     * Gets the entity of the EntityNode. Childs of this class will return their
     * proper Entity; e.g. so that ClickableEntity is returned by the 
     * ClickableEntityNodes getEntity().
     * 
     * @return the entity represented by this node.
     */
    public AbstractEntity getEntity() {
        return entity;
    }
}
