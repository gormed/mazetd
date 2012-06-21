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

import com.jme3.scene.Node;
import entities.Map;
import entities.Map.MapSquare;
import entities.Stone;
import entities.Tower;
import entities.base.EntityManager;
import eventsystem.EventManager;
import eventsystem.port.Collider3D;
import eventsystem.port.ScreenRayCast3D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import logic.pathfinding.CreepAI;
import logic.pathfinding.Pathfinder;
import mazetd.MazeTDGame;

/**
 * The class Level is the main context of the game. It manages all 
 * enitiy creation and destruction.
 * @author Hans Ferchland
 */
public class Level {
    //==========================================================================
    //===   Constants
    //==========================================================================

    /** The Constant LEVEL_TOWER_GOLD_COST. */
    public static final int LEVEL_TOWER_GOLD_COST = 20;
    
    /** The Constant LEVEL_MAXIMUM_WAVES. */
    public static final int LEVEL_MAXIMUM_WAVES = 30;

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
     *
     * @return single instance of Level
     */
    public static Level getInstance() {
        return LevelHolder.INSTANCE;
    }

    /**
     * The holder-class LevelHolder for the Level.
     */
    private static class LevelHolder {

        /** The Constant INSTANCE. */
        private static final Level INSTANCE = new Level();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /** The game. */
    private MazeTDGame game = MazeTDGame.getInstance();
    
    /** The player. */
    private Player player = Player.getInstance();
    
    /** The initialized. */
    private boolean initialized = false;
    
    /** The entity manager. */
    private EntityManager entityManager = EntityManager.getInstance();
    
    /** The creep ai. */
    private CreepAI creepAI = CreepAI.getInstance();
    
    /** The ray cast3 d. */
    private ScreenRayCast3D rayCast3D = ScreenRayCast3D.getInstance();
    
    /** The collider3 d. */
    private Collider3D collider3D = Collider3D.getInstance();
    
    /** The wave manager. */
    private WaveManager waveManager = WaveManager.getInstance();
    
    /** The pathfinder. */
    private Pathfinder pathfinder = Pathfinder.getInstance();
    
    /** The main level node. */
    private Node mainLevelNode;
    
    /** The static level elements. */
    private Node staticLevelElements;
    
    /** The dynamic level elements. */
    private Node dynamicLevelElements;
    
    /** The map. */
    private Map map = Map.getInstance();
    
    /** The grid. */
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
        entityManager.initialize();

        mainLevelNode = new Node("MainLevelNode");
        staticLevelElements = new Node("StaticLevelElements");
        dynamicLevelElements = new Node("DynamicLevelElements");
        mainLevelNode.attachChild(staticLevelElements);
        mainLevelNode.attachChild(dynamicLevelElements);


        setupLevelContent();

        game.getIsoCameraControl().lookAtMapSquare(
                pathfinder.getStartField().getSquare());
        game.getRootNode().attachChild(mainLevelNode);
        initialized = true;
    }

    /**
     * Setup the level for a new game.
     */
    private void setupLevelContent() {
        // Setup Grid, Pathfinder and Map
        map.initialize();
        pathfinder.initialize();
        createStones(map.getMapSquares());
        pathfinder.setMainPath(pathfinder.createMainPath());
        // attach map
        staticLevelElements.attachChild(map);
        // setup waves
        waveManager.loadWaves(testWaves());
//        waveManager.setStartWave(0);
        waveManager.initialize();
        // finally init player
        player.initialize();
    }

    /**
     * Destroys all level attributes so it will have to be initialized again.
     */
    public void destroy() {
        if (!initialized) {
            return;
        }

        map.destroy();
        waveManager.destroy();
        player.destroy();

        rayCast3D.destroy();
        collider3D.destroy();
        entityManager.destroy();

        staticLevelElements.detachAllChildren();
        staticLevelElements = null;

        dynamicLevelElements.detachAllChildren();
        dynamicLevelElements = null;

        mainLevelNode.detachAllChildren();

        game.getRootNode().detachChild(mainLevelNode);
        mainLevelNode = null;
        
        initialized = false;
    }

    /**
     * Init waves for simple waves. 
     * Maybe import from XML or via (random) seed.
     * @return the waves to use for the game
     */
    private Queue<WaveManager.WaveDescription> testWaves() {
        LinkedList<WaveManager.WaveDescription> waveDescriptions =
                new LinkedList<WaveManager.WaveDescription>();

        WaveManager.WaveDescription description;

        for (int i = 0; i < LEVEL_MAXIMUM_WAVES; i++) {
            description = waveManager.new WaveDescription();
            description.creepCount = 5 + Math.round(0.1f * i * i);
            description.creepDamage = 80 + 15 * i;
            description.creepGoldDrop = 5 + Math.round(0.1f * i);
//            description.creepOrbDropRate = 0.08f + 0.01f * i;
            description.creepSpeed = 0.8f + 0.03f * i;
            description.maxCreepHealthPoints = 15 + Math.round(4.1f * i * i);
            description.numberOfOrbDrobs = 2 + Math.round(i * 0.17f);

            waveDescriptions.add(description);
        }
        return waveDescriptions;
    }

    /**
     * Updates the level components.
     *
     * @param tpf the tpf
     */
    public void update(float tpf) {
        player.update(tpf);
        pathfinder.update(tpf);
        creepAI.update(tpf);
        entityManager.update(tpf);
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
     * und allen Creeps übergeben.
     *
     * @param square the square
     */
    public void buildTower(MapSquare square) {
        // add tower
        if (enoughGold()) {
            Tower t = entityManager.createTower(
                    "FirstTower", square);
//        t.replaceOrb(Orb.ElementType.RED, 0);
//        t.replaceOrb(Orb.ElementType.RED, 1);
//        t.replaceOrb(Orb.ElementType.GREEN, 2);
            player.addGold(-LEVEL_TOWER_GOLD_COST);
            creepAI.addTowerToSquare(square, Pathfinder.TOWER_WEIGHT);
            return;
        }
    }

    /**
     * Enough gold.
     *
     * @return true, if successful
     */
    public boolean enoughGold() {
        return player.getGold() >= LEVEL_TOWER_GOLD_COST;
    }

    /**
     * Removes a tower from a given MapSquare and 
     * recalculates the creep-pathes.
     * 
     * @param square the square containing the tower
     */
    public void removeTower(MapSquare square) {
        if (square != null && square.getTower() != null) {
            creepAI.removeTowerFromSquare(square, -Pathfinder.TOWER_WEIGHT);
        }
    }

    /**
     * Creates the stones.
     *
     * @param mapSquares the map squares
     */
    private void createStones(HashSet<MapSquare> mapSquares) {
        for (MapSquare s : mapSquares) {
            if (Math.random() < 0.05) {
                s.buildStoneOnField();
            }
        }
    }

    /**
     * Erzeugt ein grafisches Stone-Objekt an der Position des übergebenen Squares
     * - Stone-Objekte sind unzerstoerbar und durchquerbar.
     *
     * @param square the square
     * @return the stone
     */
    public Stone buildStone(MapSquare square) {
        // add tower
        Stone s = entityManager.createStone(
                "Stone", square);

        getStaticLevelElements().attachChild(s.getGeometryNode());
        square.getFieldInfo().incrementWeight(Pathfinder.STONE_WEIGHT);
        return s;
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
