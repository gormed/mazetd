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
 * File: Level.java
 * Type: logic.Level
 * 
 * Documentation created: 22.05.2012 - 20:38:11 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import entities.Creep;
import entities.Map;
import entities.Orb;
import entities.Tower;
import entities.base.EntityManager;
import eventsystem.events.EntityEvent;
import eventsystem.events.EntityEvent.EntityEventType;
import eventsystem.EventManager;
import eventsystem.listener.EntityListener;
import eventsystem.port.ScreenRayCast3D;
import logic.pathfinding.Pathfinder;
import mazetd.MazeTDGame;

/**
 * The class Level is the main context of the game. It manages all 
 * enitiy creation and destruction.
 * @author Hans Ferchland
 */
public class Level implements EntityListener {

    //==========================================================================
    //===   Singleton
    //==========================================================================
    /**
     * The hidden constructor of Level.
     */
    private Level() {
    }

    /**
     * The static method to retrive the one and only instance of Level.
     */
    public static Level getInstance() {
        return LevelHolder.INSTANCE;
    }

    /**
     * The holder-class LevelHolder for the Level.
     */
    private static class LevelHolder {

        private static final Level INSTANCE = new Level();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private MazeTDGame game = MazeTDGame.getInstance();
    private boolean initialized = false;
    private EntityManager entityManager = EntityManager.getInstance();
    private EventManager eventManager = EventManager.getInstance();
    private ScreenRayCast3D rayCast3D = ScreenRayCast3D.getInstance();
    private Node mainLevelNode;
    private Node staticLevelElements;
    private Node dynamicLevelElements;
    private Map map;
    private Grid grid;
    //==========================================================================
    //===   Methods
    //==========================================================================

    /**
     * Initializes the level for the first time or after destroyed.
     */
    public void initialize() {
        if (isInitialized()) {
            return;
        }
        mainLevelNode = new Node("MainLevelNode");
        staticLevelElements = new Node("StaticLevelElements");
        dynamicLevelElements = new Node("DynamicLevelElements");
        mainLevelNode.attachChild(staticLevelElements);
        mainLevelNode.attachChild(dynamicLevelElements);

        setupLevelContent();

        game.getRootNode().attachChild(mainLevelNode);
        initialized = true;
    }

    /**
     * Setup the level for a new game.
     */
    private void setupLevelContent() {

        // Setup Grid and Map
        map = new Map();
        Grid grid = Grid.getInstance();
        staticLevelElements.attachChild(map);
        // add tower (for test)
        Tower t = entityManager.createTower(
                "FirstTower", new Vector3f(0, 0, 0));
        staticLevelElements.attachChild(t.getRangeCollisionNode());
        
        Orb o = entityManager.createOrb(
                "FirstOrb", new Vector3f(2, 0, 1), Orb.ElementType.GREEN);

        Creep c = entityManager.createCreep("FirstCreep", grid.getFieldInfo(0, 10).getSquare().getLocalTranslation(), 100, 100);
        // add the level as a entity-listener
        eventManager.addEntityListener(this, t);
        
    }

    /**
     * Updates the level components.
     * @param tpf 
     */
    public void update(float tpf) {
        entityManager.update(tpf);
    }

    /**
     * Destroys all level attributes so it will have to be initialized again.
     */
    public void destroy() {
        staticLevelElements.detachAllChildren();
        dynamicLevelElements.detachAllChildren();
        mainLevelNode.detachAllChildren();

        game.getRootNode().detachChild(mainLevelNode);
        initialized = false;
    }

    /**
     * Checks if the level is initiliazed already.
     * @return true if initialize() was called, false if destroy() was called
     * or initialize() was not called.
     */
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void onAction(EntityEvent entityEvent) {
        if (entityEvent.getEventType() == EntityEventType.Click) {
            System.out.println(
                    "Level says that the entity: "
                    + entityEvent.getEntity().getName() + " was clicked.");
        }
    }
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
