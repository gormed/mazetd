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
 * The class EntityNode.
 * @author Hans Ferchland
 * @version
 */
public class EntityNode extends Node {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    protected AbstractEntity entity;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public EntityNode(String name) {
        super(name);
    }

    public EntityNode(AbstractEntity entity) {
        this.entity = entity;
    }

    public EntityNode(AbstractEntity entity, String name) {
        super(name);
        this.entity = entity;
    }

    public AbstractEntity getEntity() {
        return entity;
    }
}
