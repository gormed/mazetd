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

import com.jme3.math.Vector2f;
import eventsystem.port.ScreenRayCast3D;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import entities.Creep;
import entities.Map.MapSquare;
import entities.Orb;
import entities.Stone;
import entities.Tower;
import eventsystem.CreepHandler;
import eventsystem.events.CreepEvent.CreepEventType;
import eventsystem.port.Collider3D;
import java.util.HashMap;
import java.util.Map;
import logic.Level;
import mazetd.MazeTDGame;

/**
 * The class EntityManager is a singleton that handles all entities in MazeTD, 
 * the updating, the resources, the IDs and all events.
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
     * The static method to retrieve the one and only instance of EntityManager.
     *
     * @return single instance of EntityManager
     */
    public static EntityManager getInstance() {
        return EntityManagerHolder.INSTANCE;
    }

    /**
     * The holder-class EntityManagerHolder for the EntityManager.
     */
    private static class EntityManagerHolder {

        /** The Constant INSTANCE. */
        private static final EntityManager INSTANCE = new EntityManager();
    }
    //==========================================================================
    //===   Static Fields & Methods
    //==========================================================================
    /** The running entity id. */
    private static int runningEntityID = 0;

    /**
     * Gets the continuous entity id.
     *
     * @return the continuous entity id
     */
    static int getContinousEntityID() {
        return runningEntityID++;
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /** The entity hash map. */
    private HashMap<Integer, AbstractEntity> entityHashMap = new HashMap<Integer, AbstractEntity>();
    
    /** The tower hash map. */
    private HashMap<Integer, Tower> towerHashMap = new HashMap<Integer, Tower>();
    
    /** The creep hash map. */
    private HashMap<Integer, Creep> creepHashMap = new HashMap<Integer, Creep>();
    
    /** The orb hash map. */
    private HashMap<Integer, Orb> orbHashMap = new HashMap<Integer, Orb>();
    
    /** The stone hash map. */
    private HashMap<Integer, Stone> stoneHashMap = new HashMap<Integer, Stone>();
    
    /** The game. */
    private MazeTDGame game = MazeTDGame.getInstance();
    
    /** The ray cast3d. */
    private ScreenRayCast3D rayCast3D = ScreenRayCast3D.getInstance();
    
    /** The collider3d. */
    private Collider3D collider3D = Collider3D.getInstance();
    
    /** The initialized. */
    private boolean initialized = false;
    //==========================================================================
    //===   Methods
    //==========================================================================

    /**
     * Initializes the singleton the first time or after destroyed.
     */
    public void initialize() {
        if (initialized) {
            return;
        }
        entityHashMap.clear();
        towerHashMap.clear();
        creepHashMap.clear();
        orbHashMap.clear();
        stoneHashMap.clear();
        initialized = true;
    }

    /**
     * Destroys the singleton if initialized and frees all resources so if can
     * be reinitialized.
     */
    public void destroy() {
        if (!initialized) {
            return;
        }
        entityHashMap.clear();
        towerHashMap.clear();
        creepHashMap.clear();
        orbHashMap.clear();
        stoneHashMap.clear();
        initialized = false;
    }

    /**
     * This method adds any given AbstractEntity to the EntityManagers hashmap,
     * so it will be updated!.
     *
     * @param entity the entity inheriting AbstractEntity to add
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * (A <tt>null</tt> return can also indicate that the map
     * previously associated <tt>null</tt> with <tt>key</tt>.)
     */
    public AbstractEntity addEntity(AbstractEntity entity) {
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
        creepHashMap.remove(id);
        orbHashMap.remove(id);
        towerHashMap.remove(id);
        return entityHashMap.remove(id);
    }

    /**
     * Updates all the entitys in game.
     * @param tpf the time between the last update call
     */
    public void update(float tpf) {

        rayCast3D.update(tpf);
        HashMap<Integer, AbstractEntity> clone = new HashMap<Integer, AbstractEntity>(entityHashMap);
        for (Map.Entry<Integer, AbstractEntity> e : clone.entrySet()) {
            e.getValue().update(tpf);
        }
    }

    /**
     * Creates a tower on a given position.
     * @param name the towers name
     * @param square the map square to posit the tower
     * @return the tower just created
     */
    public Tower createTower(String name, MapSquare square) {
        Tower t = new Tower(name, square);

        addEntity(t);
        towerHashMap.put(t.getEntityId(), t);
        Node geometryNode = t.createNode(game);
        rayCast3D.addClickableObject(geometryNode);
        Level.getInstance().getStaticLevelElements().attachChild(
                t.getRangeCollisionNode());
        return t;
    }

    /**
     * Creates a creep on a given position.
     * @param name the creeps name
     * @param position the creeps position in 3D
     * @param healthPoints the current HP of the creep
     * @param maxHealthPoints the maximum HP of the creep
     * @return the creep just created
     */
    public Creep createCreep(String name, Vector3f position, float healthPoints, float maxHealthPoints) {
        Creep c = new Creep(name, position, healthPoints, maxHealthPoints);

        addEntity(c);
        creepHashMap.put(c.getEntityId(), c);
        Node geometryNode = c.createNode(game);
        collider3D.addCollisonObject(geometryNode);
        CreepHandler.getInstance().
                invokeCreepAction(
                CreepEventType.Created, c, null);
        return c;
    }

    /**
     * Creates a creep on a given position.
     * @param name the creeps name
     * @param position the creeps position in 2D with y = 0
     * @param healthPoints the current HP of the creep
     * @param maxHealthPoints the maximum HP of the creep
     * @return the creep just created
     */
    public Creep createCreep(String name, Vector2f position, float healthPoints, float maxHealthPoints) {
        return createCreep(
                name,
                new Vector3f(position.x, 0, position.y),
                healthPoints,
                maxHealthPoints);
    }

    /**
     * Creates an orb on a given position.
     * @param name the orbs name
     * @param position the orbs position
     * @param elementType the type of orb to create
     * @return the orb just created
     */
    public Orb createOrb(String name, Vector3f position, Orb.ElementType elementType) {
        Orb o = new Orb(name, position, elementType);

        addEntity(o);
        orbHashMap.put(o.getEntityId(), o);
        Node geometryNode = o.createNode(game);
        rayCast3D.addClickableObject(geometryNode);

        return o;
    }

    /**
     * Creates a stone on a given position.
     * @param name the stones name
     * @param square the map square to posit the stone
     * @return the stone just created
     */
    public Stone createStone(String name, MapSquare square) {
        Stone s = new Stone(name, square);

        addEntity(s);
        stoneHashMap.put(s.getEntityId(), s);
        Node geometryNode = s.createNode(game);
        Level.getInstance().getStaticLevelElements().attachChild(geometryNode);

        return s;


    }

    /**
     * Gets the hashmap with all creeps with their entityid as key.
     * @return all the creeps in a hashmap keyed by its entity-ids
     */
    public HashMap<Integer, Creep> getCreepHashMap() {
        return creepHashMap;
    }

    /**
     * Gets the hashmap with all orbs with their entityid as key.
     * @return all the orbs in a hashmap keyed by its entity-ids
     */
    public HashMap<Integer, Orb> getOrbHashMap() {
        return orbHashMap;
    }

    /**
     * Gets the hashmap with all towers with their entityid as key.
     * @return all the towers in a hashmap keyed by its entity-ids
     */
    public HashMap<Integer, Tower> getTowerHashMap() {
        return towerHashMap;
    }
}
