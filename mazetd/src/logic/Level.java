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

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import entities.Map;
import entities.Tower;
import entities.base.EntityManager;
import events.EntityEvent;
import events.EntityEvent.EntityEventType;
import events.EventManager;
import events.listener.EntityListener;
import events.raycast.ScreenRayCast3D;
import mazetd.MazeTDGame;

/**
 *
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
    private EntityManager entityManager = EntityManager.getInstance();
    private EventManager eventManager = EventManager.getInstance();
    private ScreenRayCast3D rayCast3D = ScreenRayCast3D.getInstance();
    private Node mainLevelNode;
    private Node staticLevelElements;
    private Node dynamicLevelElements;
    //==========================================================================
    //===   Methods
    //==========================================================================

    /**
     * initializes the level for the first time.
     */
    public void initialize() {
        mainLevelNode = new Node("MainLevelNode");
        staticLevelElements = new Node("StaticLevelElements");
        dynamicLevelElements = new Node("DynamicLevelElements");
        mainLevelNode.attachChild(staticLevelElements);
        mainLevelNode.attachChild(dynamicLevelElements);

        setupLevelContent();
        
        
    }

    private void setupLevelContent() {


        Map m = new Map();
        game.getRootNode().attachChild(m.getDecorativeMapElemetns());

        Tower t = entityManager.createTower(
                "FirstTower", new Vector3f(0, 0, 0));
        rayCast3D.addCollisonObject(t.getGeometryNode());
        
        eventManager.addEntityListener(this, t);
    }

    @Override
    public void onAction(EntityEvent entityEvent) {
        if (entityEvent.getEventType() == EntityEventType.Click) {
            System.out.println(
                    "Level says that the entity: " + 
                    entityEvent.getEntity().getName() + " was clicked.");
        }
    }
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
