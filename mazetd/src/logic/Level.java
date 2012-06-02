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
import entities.Creep;
import entities.Map;
import entities.Map.MapSquare;
import entities.Orb;
import entities.Tower;
import entities.base.EntityManager;
import eventsystem.EventManager;
import eventsystem.port.Collider3D;
import eventsystem.port.ScreenRayCast3D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import logic.pathfinding.Pathfinder;
import mazetd.MazeTDGame;

/**
 * The class Level is the main context of the game. It manages all 
 * enitiy creation and destruction.
 * @author Hans Ferchland
 */
public class Level {

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
    private Collider3D collider3D = Collider3D.getInstance();
    private WaveManager waveManager = WaveManager.getInstance();
    private Pathfinder pathfinder = Pathfinder.getInstance();
    private Node mainLevelNode;
    private Node staticLevelElements;
    private Node dynamicLevelElements;
    private Map map = Map.getInstance();
    private Grid grid = Grid.getInstance();
    //==========================================================================
    //===   Methods
    //==========================================================================

    /**
     * Initializes the level for the first time or after destroyed.
     */
    public void initialize() {
        if (initialized) {
            return;
        }

        rayCast3D.initialize();
        collider3D.initialize();

        mainLevelNode = new Node("MainLevelNode");
        staticLevelElements = new Node("StaticLevelElements");
        dynamicLevelElements = new Node("DynamicLevelElements");
        mainLevelNode.attachChild(staticLevelElements);
        mainLevelNode.attachChild(dynamicLevelElements);

        map.initialize();
        pathfinder.initialize();

        setupLevelContent();

        game.getRootNode().attachChild(mainLevelNode);
        initialized = true;
    }

    /**
     * Setup the level for a new game.
     */
    private void setupLevelContent() {

        // Setup Grid and Map

        staticLevelElements.attachChild(map);

        waveManager.loadWaves(testWaves());
        waveManager.initialize();

        Orb o = entityManager.createOrb(
                "FirstOrb", new Vector3f(2, 0, 1), Orb.ElementType.GREEN);

//        Creep c = entityManager.createCreep("FirstCreep",
//                Pathfinder.getInstance().getStartField().getSquare().getLocalTranslation(),
//                100, 100);


    }

    private Queue<WaveManager.WaveDescription> testWaves() {
        LinkedList<WaveManager.WaveDescription> waveDescriptions =
                new LinkedList<WaveManager.WaveDescription>();

        WaveManager.WaveDescription description;

        for (int i = 0; i < 30; i++) {
            description = waveManager.new WaveDescription();
            description.creepCount = 7 + 3 * i;
            description.creepDamage = 50 + 10 * i;
            description.creepGoldDrop = 10 + 5 * i;
            description.creepOrbDropRate = 0.05f + 0.01f * i;
            description.creepSpeed = 0.8f + 0.05f * i;
            description.maxCreepHealthPoints = 35 + 7.5f * i;

            waveDescriptions.add(description);
        }
        return waveDescriptions;
    }

    /**
     * Updates the level components.
     * @param tpf 
     */
    public void update(float tpf) {
        pathfinder.update(tpf);
        entityManager.update(tpf);
    }

    /**
     * Destroys all level attributes so it will have to be initialized again.
     */
    public void destroy() {

        collider3D.destroy();
        rayCast3D.destroy();

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

    /**
     * Erzeugt ein grafisches Tower-Objekt an der Position des übergebenen Squares
     * - Liegt der erzeugte Tower auf dem Path wird ein neuer Path generiert 
     *   und allen Creeps übergeben
     * 
     * @param map 
     */
    public void buildTower(MapSquare map) {
        // add tower
        Tower t = entityManager.createTower(
                "FirstTower", map.getLocalTranslation());
        staticLevelElements.attachChild(t.getRangeCollisionNode());
        map.getFieldInfo().incrementWeight(10000);
        //Liegt der Turm auf dem aktuellen MainPath?
        if (Pathfinder.getInstance().getMainPath().contains(map)) {
            //NeuerPfad wird generiert
            Pathfinder.getInstance().setMainPath(Pathfinder.getInstance().createMainPath());
            checkCreeps(entityManager.getCreepHashMap(), Pathfinder.getInstance().getMainPath());
        }

    }

    /**
     * Ermittelt für alle Creeps den neuen Path
     * - wenn der Creep auf dem MainPath ist wird der MainPath ab der Creepposition übergeben
     * - sonst wird von seiner Position zum Ziel ein eigener Pfad erstellt
     * 
     * @param alle creeps
     * @param mainPath 
     */
    private void checkCreeps(HashMap<Integer, Creep> creeps, Queue<MapSquare> mainPath) {
        for (Creep creep : creeps.values()) {
            Queue<MapSquare> path = mainPath;
            //Ist der Creep auf dem MainPath?
            if (mainPath.contains(creep.getCurrentSquare())) {
                //Ziehe alle Squares aus der Queue bis das richtige am anfang steht
                while (!path.poll().equals(creep.getCurrentSquare())) {
                }
                creep.setPath(path);
            } else {
                Queue<MapSquare> uniquePath = Pathfinder.getInstance().createCreepPath(creep.getCurrentSquare().getFieldInfo());
                creep.setPath(uniquePath);
            }

        }
    }

    /**
     * Retrieves the node where all dynamic elements are stored.
     * @return the jme3 node
     */
    public Node getDynamicLevelElements() {
        return dynamicLevelElements;
    }

    /**
     * Retrieves the node where all static elements are stored.
     * @return the jme3 node
     */
    public Node getStaticLevelElements() {
        return staticLevelElements;
    }
}
