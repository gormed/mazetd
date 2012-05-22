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
 * File: AbstractEntity.java
 * Type: entities.base.AbstractEntity
 * 
 * Documentation created: 21.05.2012 - 17:57:13 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.base;

import com.jme3.scene.Node;
import mazetd.MazeTDGame;

/**
 * The class AbstractEntity. This class is the parent of all entitys!
 * @author Hans Ferchland
 * @version 0.1
 */
public abstract class AbstractEntity {
    //==========================================================================
    //===   private, package & protected Fields
    //==========================================================================

    /** the unique id of the entity! */
    int id;
    /** the desired name of an entity, could be twice in the game. */
    protected String name;
    /** The Node for the geometry of this entity. */
    Node geometryNode;

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    /**
     * Hidden construnctor for internal creation. Maybe this will be usefull.
     * @param id the id given by a gerator for running ids
     * @param name the desired name of an entity, could be twice in the game.
     */
    AbstractEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Contructor of the basic entity parameters. Needs to be 
     * enhanced for child-classes.
     * @param name the desired name of an entity, could be twice in the game.
     */
    public AbstractEntity(String name) {
        this.name = name;
        this.id = EntityManager.getContinousEntityID();
    }

    /**
     * Creates the geometry node and returns it. Generally all geometry of an 
     * entity is placed unter this node! This node will be added to se SG.
     * @return the created main-geometry node
     */
    public Node createGeometryNode(MazeTDGame game) {
        geometryNode = new Node(name + "s_GeometryNode");
        return geometryNode;
    }

    /**
     * Updates the entity every game-tick.
     * @param tpf the time between the last update call
     */
    protected abstract void update(float tpf);

    /**
     * Gets the enities node where the geometry is attached.
     * @return the node with the geometry of this entity
     */
    public Node getGeometryNode() {
        return geometryNode;
    }

    /**
     * Gets the entity-id from this entity; this id is unique!
     * @return the entities unique id
     */
    public int getEntityId() {
        return id;
    }

    /**
     * Gets the entity-name from this entity, could be twice in the game.
     * @return the enities name
     */
    public String getName() {
        return name;
    }


    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
