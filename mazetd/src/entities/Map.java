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
     * Holder class for the EventManager
     */
    private static class MapHolder {

        private static final Map INSTANCE = new Map();
    }
    //==========================================================================
    //===   Static Fields and Methods
    //==========================================================================
    /** default color of a square */
    public static ColorRGBA SQUARE_COLOR = new ColorRGBA(0, 1, 0, 0.0f);
    /** default size of a square */
    public static float SQUARE_SIZE = 0.9f;
    /** running id of the squares */
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
    private Geometry groundPlane;
    private Material groundMaterial;
    private AbstractHeightMap heightMap;
    private Material heightMapMaterial;
    private TerrainQuad terrain;
    private Node clickableMapElements;
    private Node decorativeMapElemetns;
    private float totalHeight;
    private float totalWidth;
    private MazeTDGame game = MazeTDGame.getInstance();
    private Grid grid = Grid.getInstance();
    private EntityManager entityManager = EntityManager.getInstance();
    private HashSet<MapSquare> mapSquares = new HashSet<MapSquare>();
    private BuildTowerHUD buildTowerHUD = BuildTowerHUD.getInstance();
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Creates the Map, background of the level and grid for tower-placement.
     */
    public void initialize() {
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
    }

    /**
     * Frees all resources aquired by the map.
     */
    public void destroy() {
        decorativeMapElemetns.detachAllChildren();
        clickableMapElements.detachAllChildren();
        this.detachChild(decorativeMapElemetns);
        ScreenRayCast3D.getInstance().removeClickableObject(clickableMapElements);
        buildTowerHUD.destroy();
    }

    /**
     * Creates the ground-plane of the map/level.
     */
    private void createGround(MazeTDGame game) {
        Quad q = new Quad(totalWidth, totalHeight);
        groundPlane = new Geometry("GroundPlane", q);

        groundMaterial = new Material(game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        groundMaterial.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        groundMaterial.setColor("Specular", ColorRGBA.White);
        groundMaterial.setColor("Ambient", new ColorRGBA(0.4f, 0.9f, 0.4f, 0.2f));   // ... color of this object
        groundMaterial.setColor("Diffuse", new ColorRGBA(0f, 0.5f, 0, 0.2f));   // ... color of light being reflected
        groundMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        groundPlane.setMaterial(groundMaterial);
        groundPlane.setQueueBucket(Bucket.Translucent);
        
        float[] angles = {3 * (float) Math.PI / 2, 0, 0};
        Vector3f pos = new Vector3f(
                -totalWidth / 2 - SQUARE_SIZE / 2,
                0.1f,
                (totalHeight / 2 - SQUARE_SIZE / 2));

        groundPlane.setLocalRotation(new Quaternion(angles));
        groundPlane.setLocalTranslation(pos);
        groundPlane.setShadowMode(ShadowMode.Off);
//        decorativeMapElemetns.attachChild(groundPlane);
    }

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

        public static final float MAP_SQUARE_HEIGHT = 0.1f;
        public static final float MAX_ALPHA_FADE = 0.4f;
        //==========================================================================
        //===   Private Fields
        //==========================================================================
        private Material material;
        private ClickableGeometry geometry;
        private boolean hovered = false;
        private boolean mainPath;
        private boolean creepPath;
        private boolean creepOn;
        private ColorRGBA fadeColor = SQUARE_COLOR.clone();
        private FieldInfo field;
        private Tower tower;
        private Stone stone;
        //==========================================================================
        //===   Methods & Constructor
        //==========================================================================

        /**
         * Contructor for a map square, name and mesh will be generated automaticly.
         */
        public MapSquare(FieldInfo field) {
            super("MapSquare_" + getContinousSquareID());
            createGeometry();
            this.field = field;
        }

        /**
         * Gets the geometry that is clickable (the whole square^^).
         * @return 
         */
        public ClickableGeometry getGeometry() {
            return geometry;
        }

        /**
         * Method, that is invoked if this squares geometry is clicked.
         */
        public void squareClicked() {
            buildTowerHUD.show(this);
        }

        /**
         * Builds a Tower on this field if the icon on the 
         * field (BuildTowerHub) is clicked.
         */
        public void buildTowerOnField() {
            if (this.getFieldInfo().getWeight() < Pathfinder.TOWER_WEIGHT
                    && !isCreepOnField(this.getFieldInfo(),
                    entityManager.getCreepHashMap())) {
                Level.getInstance().buildTower(this);
                buildTowerHUD.hide();
            }
        }

        /**
         * Builds a Stone on this field 
         * Used b
         * 
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
                    // TODO: implement handling if a square is clicked #done
                    System.out.println(name + " clicked!");
                    System.out.println(field.toString());
                    squareClicked();
                }

                @Override
                public void onRayCastMouseOver(Vector2f mouse, CollisionResult result) {
                    System.out.println(name + " hovered!");
                    hovered = true;
                }

                @Override
                public void onRayCastMouseLeft(Vector2f mouse, CollisionResult result) {
                    hovered = false;
//                    buildTowerHUD.hide();
                }
            };

            // assign material
            material = new Material(game.getAssetManager(),
                    "Common/MatDefs/Light/Lighting.j3md");
            material.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
            material.setColor("Specular", ColorRGBA.White);
            material.setColor("Ambient", SQUARE_COLOR);   // ... color of this object
            material.setColor("Diffuse", SQUARE_COLOR);   // ... color of light being reflected

//            material = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//            material.setColor("Color", SQUARE_COLOR);
//            material.setColor("GlowColor", SQUARE_COLOR);
            material.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);

            geometry.setMaterial(material);
            geometry.setQueueBucket(Bucket.Translucent);

            float[] angles = {3 * (float) Math.PI / 2, 0, 0};

            geometry.setLocalRotation(new Quaternion(angles));
            geometry.setLocalTranslation(-SQUARE_SIZE / 2, MAP_SQUARE_HEIGHT, SQUARE_SIZE / 2);

            this.attachChild(geometry);
        }

        /**
         * TODO: Hady
         */
        public FieldInfo getFieldInfo() {
            return field;
        }

        public void setMainPathDebug(boolean value) {
            mainPath = value;
        }

        public void setCreepPathDebug(boolean value) {
            creepPath = value;
        }

        public Vector2f getPosition() {
            Vector3f pos3d = getLocalTranslation();
            return new Vector2f(pos3d.x, pos3d.z);
        }

        public boolean hasTower() {
            return tower != null;
        }

        public Tower getTower() {
            return tower;
        }

        void setStone(Stone stone) {
            this.stone = stone;
        }

        void setTower(Tower t) {
            this.tower = t;
        }

        public void setHovered(boolean value) {
            hovered = value;
        }

        @Override
        public void onTimedEvent(TimerEvent t) {
            if (hovered && fadeColor.a < MAX_ALPHA_FADE) {

                fadeColor.a += 0.05f;
                //fadeColor.clamp();

            } else if (!hovered && fadeColor.a >= 0.0f) {
                if (creepOn) {
                    creepOn = false;
                }
                fadeColor.a -= 0.01f;
            }

            if (field.getWeight() < Pathfinder.TOWER_WEIGHT) {
                material.setColor("Ambient", fadeColor);   // ... color of this object
                material.setColor("Diffuse", fadeColor);   // ... color of light being reflected
            } else {
                material.setColor("Ambient", ColorRGBA.BlackNoAlpha);   // ... color of this object
                material.setColor("Diffuse", ColorRGBA.BlackNoAlpha);   // ... color of light being reflected
            }
            if (creepOn) {

                material.setColor("Ambient", ColorRGBA.Red);   // ... color of this object
                material.setColor("Diffuse", ColorRGBA.Red);   // ... color of light being reflected

            } else if (creepPath && Pathfinder.DEBUG_PATH) {
                material.setColor("Ambient", ColorRGBA.Orange);   // ... color of this object
                material.setColor("Diffuse", ColorRGBA.Orange);   // ... color of light being reflected

            } else if (mainPath && Pathfinder.DEBUG_PATH) {
                material.setColor("Ambient", ColorRGBA.Cyan);   // ... color of this object
                material.setColor("Diffuse", ColorRGBA.Cyan);   // ... color of light being reflected

            }
        }

        @Override
        public float getPeriod() {
            return 0.02f;
        }
    }
}
