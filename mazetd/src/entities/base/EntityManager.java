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
 * File: EntityManager.java
 * Type: entities.base.EntityManager
 * 
 * Documentation created: 21.05.2012 - 18:05:04 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.base;

import events.ScreenRayCast3D;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import entities.Tower;
import java.util.HashMap;
import java.util.Map;
import mazetd.MazeTDGame;

/**
 *
 * @author Hans Ferchland
 */
public class EntityManager {
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of EntityManager.
     */
    private EntityManager() {
    }

    /**
     * The static method to retrive the one and only instance of EntityManager.
     */
    public static EntityManager getInstance() {
        return EntityManagerHolder.INSTANCE;
    }

    /**
     * The holder-class EntityManagerHolder for the EntityManager.
     */
    private static class EntityManagerHolder {

        private static final EntityManager INSTANCE = new EntityManager();
    }    
    //==========================================================================
    //===   Static Fields & Methods
    //==========================================================================
    private static int runningEntityID = 0;
    static int getContinousEntityID() {
        return runningEntityID++;
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private HashMap<Integer, AbstractEntity> entityHashMap = new HashMap<Integer, AbstractEntity>();
    private MazeTDGame game = MazeTDGame.getInstance();
    private ScreenRayCast3D rayCast3D = ScreenRayCast3D.getInstance();
    //==========================================================================
    //===   Methods
    //==========================================================================
    /**
     * This method adds any given AbstractEntity to the EntityManagers hashmap, 
     * so it will be updated!
     * @param entity the entity inheriting AbstractEntity to add
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>.)
     */
    private AbstractEntity addEntity(AbstractEntity entity) {
        return entityHashMap.put(entity.getEntityId(), entity);
    }
    /**
     * This method will remove a given entity from the EntityManagers hashmap. 
     * It wont be updated anymore!
     * @param id the id of the entity to remove
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>.)
     */
    public AbstractEntity removeEntity(int id) {
        return entityHashMap.remove(id);
    }
    
    /**
     * Updates all the entitys in game.
     * @param tpf the time between the last update call
     */
    public void update(float tpf) {
        for (Map.Entry<Integer, AbstractEntity> e : entityHashMap.entrySet()) {
            e.getValue().update(tpf);
        }
    }
    
    public Tower createTower(String name, Vector3f position) {
        Tower t = new Tower(name, position);
        
        addEntity(t);
        Node geometryNode = t.createGeometryNode(game);
        rayCast3D.addCollisonObject(geometryNode);
        
        return t;
    }
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}