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
 * File: Map.java
 * Type: entities.Map
 * 
 * Documentation created: 14.05.2012 - 18:59:39 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.asset.AssetManager;
import entities.geometry.ClickableGeometry;
import eventsystem.port.ScreenRayCast3D;
import com.jme3.collision.CollisionResult;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import entities.base.EntityManager;
import eventsystem.EventManager;
import eventsystem.events.TimerEvent;
import eventsystem.listener.TimerEventListener;
import gui.elements.BuildTowerHUD;
import java.util.HashMap;
import java.util.HashSet;
import logic.Grid;
import logic.Grid.FieldInfo;
import logic.Level;
import logic.Player;
import logic.pathfinding.Pathfinder;
import mazetd.MazeTDGame;

/**
 * The class Map as graphical representation of the logical Grid-class.
 * Handles MapSquares and click-events for tower-build-selections.
 * @author Hans Ferchland & Hady Khalifa
 * @version 0.3
 */
public class Map extends Node {
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of the singleton.
     */
    private Map() {
        super("MainMap");
    }

    /**
     * Static method to retrieve the one and olny reference to the manager.
     * @return the reference of the EventManager
     */
    public static Map getInstance() {
        return MapHolder.INSTANCE;
    }

    /**
     * Holder class for the EventManager.
     */
    private static class MapHolder {

        /** The Constant INSTANCE. */
        private static final Map INSTANCE = new Map();
    }
    //==========================================================================
    //===   Static Fields and Methods
    //==========================================================================
    /** default color of a square. */
    public static ColorRGBA SQUARE_COLOR = new ColorRGBA(0, 1, 0, 0.0f);
    
    /** default size of a square. */
    public static float SQUARE_SIZE = 0.95f;
    
    /** running id of the squares. */
    private static int runningSquareID = 0;

    /**
     * Gets the next id for a square.
     * @return the next id
     */
    private static int getContinousSquareID() {
        return runningSquareID++;
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /** The ground plane. */
    private Geometry groundPlane;
    
    /** The ground material. */
    private Material groundMaterial;
    
    /** The height map. */
    private AbstractHeightMap heightMap;
    
    /** The height map material. */
    private Material heightMapMaterial;
    
    /** The terrain. */
    private TerrainQuad terrain;
    
    /** The clickable map elements. */
    private Node clickableMapElements;
    
    /** The decorative map elemetns. */
    private Node decorativeMapElemetns;
    
    /** The total height. */
    private float totalHeight;
    
    /** The total width. */
    private float totalWidth;
    
    /** The game. */
    private MazeTDGame game = MazeTDGame.getInstance();
    
    /** The grid. */
    private Grid grid = Grid.getInstance();
    
    /** The entity manager. */
    private EntityManager entityManager = EntityManager.getInstance();
    
    /** The map squares. */
    private HashSet<MapSquare> mapSquares = new HashSet<MapSquare>();
    
    /** The build tower hud. */
    private BuildTowerHUD buildTowerHUD = BuildTowerHUD.getInstance();
    
    /** The initialized. */
    private boolean initialized = false;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Creates the Map, background of the level and grid for tower-placement.
     */
    public void initialize() {
        if (initialized) {
            return;
        }
        grid.initialize();
        totalHeight = grid.getTotalHeight();
        totalWidth = grid.getTotalWidth();
        decorativeMapElemetns = new Node("DorativeMapElemetns");
        clickableMapElements = new Node("ClickableMapElements");

        buildTowerHUD.initialize();

        createGround(game);
        createHeightMap(game);
        createSquares();

        this.attachChild(decorativeMapElemetns);
        ScreenRayCast3D.getInstance().addClickableObject(clickableMapElements);
        initialized = true;
    }

    /**
     * Frees all resources aquired by the map.
     */
    public void destroy() {
        if (!initialized) {
            return;
        }
        
        for (MapSquare m : mapSquares) {
            EventManager.getInstance().removeTimerEventListener(m);
        }
        mapSquares.clear();
        decorativeMapElemetns.detachAllChildren();
        clickableMapElements.detachAllChildren();
        this.detachChild(decorativeMapElemetns);
        ScreenRayCast3D.getInstance().removeClickableObject(clickableMapElements);
        buildTowerHUD.destroy();
        initialized = false;
    }

    /**
     * Creates the ground-plane of the map/level.
     *
     * @param game the game
     */
    private void createGround(MazeTDGame game) {
        Quad q = new Quad(totalWidth, totalHeight);
        groundPlane = new Geometry("GroundPlane", q);

        groundMaterial = new Material(game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        groundMaterial.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        groundMaterial.setColor("Specular", ColorRGBA.White);
        groundMaterial.setColor("Ambient", new ColorRGBA(0.4f, 0.9f, 0.4f, 0.2f));   // ... color of this object
        groundMaterial.setColor("Diffuse", new ColorRGBA(0f, 0.5f, 0, 0.2f));   // ... color of light being reflected
        groundMaterial.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);

        groundPlane.setMaterial(groundMaterial);
        groundPlane.setQueueBucket(Bucket.Translucent);

        float[] angles = {3 * (float) Math.PI / 2, 0, 0};
        Vector3f pos = new Vector3f(
                -totalWidth / 2 - SQUARE_SIZE / 2,
                0.02f,
                (totalHeight / 2 - SQUARE_SIZE / 2));

        groundPlane.setLocalRotation(new Quaternion(angles));
        groundPlane.setLocalTranslation(pos);
        groundPlane.setShadowMode(ShadowMode.Off);
//        decorativeMapElemetns.attachChild(groundPlane);
    }

    /**
     * Creates the height map for level background.
     * @param game the mazetdgame reference
     */
    private void createHeightMap(MazeTDGame game) {
        AssetManager assetManager = game.getAssetManager();
        /** 1. Create terrain material and load four textures into it. */
        heightMapMaterial = new Material(assetManager,
                "Common/MatDefs/Terrain/Terrain.j3md");

        /** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
        heightMapMaterial.setTexture("Alpha", assetManager.loadTexture(
                "Textures/Terrain/alphamap.png"));

        /** 1.2) Add GRASS texture into the red layer (Tex1). */
        Texture grass = assetManager.loadTexture(
                "Textures/Terrain/grass.jpg");
        grass.setWrap(WrapMode.Repeat);
        heightMapMaterial.setTexture("Tex1", grass);
        heightMapMaterial.setFloat("Tex1Scale", 16f);

        /** 1.3) Add DIRT texture into the green layer (Tex2) */
        Texture dirt = assetManager.loadTexture(
                "Textures/Terrain/dirt.jpg");
        dirt.setWrap(WrapMode.Repeat);
        heightMapMaterial.setTexture("Tex2", dirt);
        heightMapMaterial.setFloat("Tex2Scale", 32f);

        /** 1.4) Add ROAD texture into the blue layer (Tex3) */
        Texture rock = assetManager.loadTexture(
                "Textures/Terrain/road.jpg");
        rock.setWrap(WrapMode.Repeat);
        heightMapMaterial.setTexture("Tex3", rock);
        heightMapMaterial.setFloat("Tex3Scale", 32f);

        /** 2. Create the height map */
        AbstractHeightMap heightmap = null;
        Texture heightMapImage = assetManager.loadTexture(
                "Textures/Terrain/mountains128.png");
        heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();

        int patchSize = 17;
        terrain = new TerrainQuad("MAZETD_Terrain", patchSize, 129, heightmap.getHeightMap());

        /** 4. We give the terrain its material, position & scale it, and attach it. */
        terrain.setMaterial(heightMapMaterial);
        terrain.setLocalTranslation(0, 0.0f, 0);
        terrain.setLocalScale(0.4f, 0.01f, 0.25f);
        terrain.setShadowMode(ShadowMode.Receive);
//        terrain.setQueueBucket(Bucket.Translucent);

        decorativeMapElemetns.attachChild(terrain);

    }

    /**
     * Creates all squares on the map.
     */
    private void createSquares() {
        // TODO: as parameter the array/list (or whatever) must be given, 
        // to generate the map.

        Vector3f offset = new Vector3f(-totalWidth / 2, 0, -totalHeight / 2);

        for (int x = 0; x < totalWidth; x++) {
            for (int z = 0; z < totalHeight; z++) {
                MapSquare m = new MapSquare(grid.getFieldInfo((((int) totalWidth - 1) - x), z));
                m.getFieldInfo().setMapSquare(m);
                Vector3f position = new Vector3f(x, 0, z);
                position.addLocal(offset);

                // do not touch the y-coord, go to the class MapSquare to change it!
                m.setLocalTranslation(position);

                clickableMapElements.attachChild(m);
                mapSquares.add(m);
                EventManager.getInstance().addTimerEventListener(m);
            }
        }
    }

    /**
     * Gets a copy of all map-squares.
     * @return a copy of all map-squares in a HashMap
     */
    public HashSet<MapSquare> getMapSquares() {
        return new HashSet<MapSquare>(mapSquares);
    }

    /**
     * Gets the node with all decorative elements.
     * @return the node
     */
    public Node getDecorativeMapElements() {
        return decorativeMapElemetns;
    }

    /**
     * Checks if the Map was already inititalized or not.
     * @return true if initialized, false otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }

    //==========================================================================
    //===   Inner Classes
    //==========================================================================
    /**
     * Class for a map-square that represents a grid-square.
     * @author Hans Ferchland
     */
    public class MapSquare extends Node implements TimerEventListener {
        //==========================================================================
        //===   Constants
        //========================================================================== 
        /**
         * The height over zero of a map square.
         */
        public static final float MAP_SQUARE_HEIGHT = 0.1f;
        /**
         * The maximum alpha fade of a map square.
         */
        public static final float MAX_ALPHA_FADE = 0.4f;
        //==========================================================================
        //===   Private Fields
        //==========================================================================
        /** The material. */
        private Material material;
        
        /** The geometry. */
        private ClickableGeometry geometry;
        
        /** The hovered. */
        private boolean hovered = false;
        
        /** The creep path. */
        private boolean creepPath;
        
        /** The creep on. */
        private boolean creepOn;
        
        /** The fade color. */
        private ColorRGBA fadeColor = SQUARE_COLOR.clone();
        
        /** The field. */
        private FieldInfo field;
        
        /** The tower. */
        private Tower tower;
        
        /** The stone. */
        private Stone stone;
        //==========================================================================
        //===   Methods & Constructor
        //==========================================================================

        /**
         * Contructor for a map square, name and mesh will be generated automaticly.
         *
         * @param field the field
         */
        public MapSquare(FieldInfo field) {
            super("MapSquare_" + getContinousSquareID());
            createGeometry();
            this.field = field;
        }

        /**
         * Gets the geometry that is clickable (the whole square^^).
         *
         * @return the geometry
         */
        public ClickableGeometry getGeometry() {
            return geometry;
        }

        /**
         * Method, that is invoked if this squares geometry is clicked.
         */
        public void squareClicked() {
            if (this.getFieldInfo().getWeight() < Pathfinder.TOWER_WEIGHT
                    && Level.getInstance().enoughGold()) {
                buildTowerHUD.show(this);
                Player.getInstance().setSelectedTower(null);
                Tower.TowerSelection.getInstance().detachFromTower();
            }
        }

        /**
         * Builds a Tower on this field if the icon on the 
         * field (BuildTowerHub) is clicked.
         */
        public void buildTowerOnField() {
            if (this.getFieldInfo().getWeight() < Pathfinder.TOWER_WEIGHT
                    && !isCreepOnField(this.getFieldInfo(),
                    entityManager.getCreepHashMap())
                    && !this.getFieldInfo().equals(grid.getEndField())
                    && !this.getFieldInfo().equals(grid.getStartField())) {
                Level.getInstance().buildTower(this);
                buildTowerHUD.hide();
            }
        }

        /**
         * Builds a Stone on this field
         * Used b.
         */
        public void buildStoneOnField() {
            if (this.getFieldInfo().getWeight() < Pathfinder.TOWER_WEIGHT
                    && !this.getFieldInfo().equals(grid.getEndField())
                    && !this.getFieldInfo().equals(grid.getStartField())
                    && !isCreepOnField(this.getFieldInfo(),
                    entityManager.getCreepHashMap())) {
                Level.getInstance().buildStone(this);
            }
        }

        /**
         * Checks if a Creep is on this map-sqaure.
         * TODO: fix this, not nessecary
         *
         * @param field the field
         * @param creeps the creeps
         * @return true, if is creep on field
         */
        private boolean isCreepOnField(FieldInfo field, HashMap<Integer, Creep> creeps) {
            for (Creep creep : creeps.values()) {
                if (creep.isOnSquare(field.getSquare())) {
                    creepOn = true;
                    return true;
                }

            }
            creepOn = false;
            return false;
        }

        /**
         * Creates the geometry for a square with default size and color.
         */
        private void createGeometry() {
            // Creates an anonymous inner class in the map square for simple
            // click event-handling
            geometry = new ClickableGeometry(name + "_Geometry",
                    new Quad(SQUARE_SIZE, SQUARE_SIZE)) {

                /**
                 * Will be called if the square is clicked.
                 */
                @Override
                public void onRayCastClick(Vector2f mouse, CollisionResult result) {
                    System.out.println(name + " clicked!");
                    System.out.println(field.toString());
                    squareClicked();
                }

                @Override
                public void onRayCastMouseOver(Vector2f mouse, CollisionResult result) {

                    hovered = true;
                }

                @Override
                public void onRayCastMouseLeft(Vector2f mouse, CollisionResult result) {
                    hovered = false;
                }
            };

            // assign material
            material = new Material(game.getAssetManager(),
                    "Common/MatDefs/Light/Lighting.j3md");
            material.setBoolean("UseMaterialColors", true);
            material.setColor("Specular", ColorRGBA.White);
            material.setColor("Ambient", SQUARE_COLOR);
            material.setColor("Diffuse", SQUARE_COLOR);

            material.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);

            geometry.setMaterial(material);
            geometry.setQueueBucket(Bucket.Translucent);

            float[] angles = {3 * (float) Math.PI / 2, 0, 0};

            geometry.setLocalRotation(new Quaternion(angles));
            geometry.setLocalTranslation(-SQUARE_SIZE / 2, MAP_SQUARE_HEIGHT, SQUARE_SIZE / 2);

            this.attachChild(geometry);
        }

        /**
         * Gets the pathfinding info from this mapsquare.
         *
         * @return the map sqaures equivalent grind-field info
         */
        public FieldInfo getFieldInfo() {
            return field;
        }

        /**
         * Shows the creeps path if debugging.
         *
         * @param value the new creep path debug
         */
        public void setCreepPathDebug(boolean value) {
            creepPath = value;
        }

        /**
         * Gets the position of the map square in 2D.
         * @return the x-z 2D vector of the position
         */
        public Vector2f getPosition() {
            Vector3f pos3d = getLocalTranslation();
            return new Vector2f(pos3d.x, pos3d.z);
        }

        /**
         * Checks if map-square bears a tower.
         *
         * @return true if tower on, false otherwise
         */
        public boolean hasTower() {
            return tower != null;
        }

        /**
         * Gets the tower on this map-sqaure.
         * @return the tower, if there is one, null otherwise
         */
        public Tower getTower() {
            return tower;
        }

        /**
         * Applys a stone to this map-square.
         *
         * @param stone the stont to apply
         */
        void setStone(Stone stone) {
            this.stone = stone;
        }

        /**
         * Sets the tower on this map-square if placed.
         *
         * @param t the tower to set
         */
        void setTower(Tower t) {
            this.tower = t;
        }

        /**
         * Applys the hovered flag from outside.
         * @param value true if field should be displayed as hovered, false otherwise
         */
        public void setHovered(boolean value) {
            hovered = value;
        }

        /* (non-Javadoc)
         * @see eventsystem.listener.TimerEventListener#onTimedEvent(eventsystem.events.TimerEvent)
         */
        @Override
        public void onTimedEvent(TimerEvent t) {
            // fades the field in if enough gold, hovered and not faded to max value
            if (Level.getInstance().enoughGold() && hovered && fadeColor.a < MAX_ALPHA_FADE) {

                fadeColor.a += 0.05f;
                //fadeColor.clamp();

                // else field fades out
            } else if (!hovered && fadeColor.a >= 0.0f) {
                if (creepOn) {
                    creepOn = false;
                }
                fadeColor.a -= 0.01f;
            }
            // show diffrent color for each case
            if (field.getWeight() < Pathfinder.TOWER_WEIGHT) {
                // creep is on field
                if (creepOn) {
                    material.setColor("Ambient", ColorRGBA.Red);   // ... color of this object
                    material.setColor("Diffuse", ColorRGBA.Red);   // ... color of light being reflected
                    // show the debug path in debug mode
                } else if (creepPath && Pathfinder.DEBUG_PATH) {
                    material.setColor("Ambient", ColorRGBA.Orange);   // ... color of this object
                    material.setColor("Diffuse", ColorRGBA.Orange);   // ... color of light being reflected
                    // fade normally otherwise
                } else {
                    material.setColor("Ambient", fadeColor);   // ... color of this object
                    material.setColor("Diffuse", fadeColor);   // ... color of light being reflected
                }
            } else {
                // if tower or stone on field, stay transparent
                material.setColor("Ambient", ColorRGBA.BlackNoAlpha);
                material.setColor("Diffuse", ColorRGBA.BlackNoAlpha);
            }

        }

        /* (non-Javadoc)
         * @see eventsystem.listener.TimerEventListener#getPeriod()
         */
        @Override
        public float getPeriod() {
            return 0.02f;
        }
    }
}
