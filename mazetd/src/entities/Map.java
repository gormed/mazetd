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

import entities.geometry.ClickableGeometry;
import events.ScreenRayCast3D;
import com.jme3.collision.CollisionResult;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import events.EventManager;
import events.TimerEvent;
import events.listener.TimerEventListener;
import mazetd.MazeTDGame;

/**
 * The class Map.
 * @author Hans Ferchland
 * @version 0.1
 */
public class Map extends Node {
    //==========================================================================
    //===   Static Fields and Methods
    //==========================================================================

    /** default color of a square */
    private static ColorRGBA SQUARE_COLOR = new ColorRGBA(0, 1, 0, 0.1f);
    /** default size of a square */
    private static float SQUARE_SIZE = 0.9f;
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
    private Node clickableMapElements;
    private Node decorativeMapElemetns;
    private float totalHeight;
    private float totalWidth;
    private MazeTDGame game = MazeTDGame.getInstance();
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public Map() {
        super("MainMap");
        totalHeight = 20;
        totalWidth = 20;
        decorativeMapElemetns = new Node("DorativeMapElemetns");
        clickableMapElements = new Node("ClickableMapElements");

        createGround();
        createSquares();

        this.attachChild(decorativeMapElemetns);
        ScreenRayCast3D.getInstance().addCollisonObject(clickableMapElements);
    }

    /**
     * Creates the ground-plane of the map/level.
     */
    private void createGround() {
        Quad q = new Quad(totalHeight, totalWidth);
        groundPlane = new Geometry("GroundPlane", q);

        groundMaterial = new Material(game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        groundMaterial.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        groundMaterial.setColor("Specular", ColorRGBA.White);
        groundMaterial.setColor("Ambient", ColorRGBA.Gray);   // ... color of this object
        groundMaterial.setColor("Diffuse", ColorRGBA.Gray);   // ... color of light being reflected

        groundPlane.setMaterial(groundMaterial);

        float[] angles = {3 * (float) Math.PI / 2, 0, 0};

        groundPlane.setLocalRotation(new Quaternion(angles));
        groundPlane.setLocalTranslation(-totalWidth / 2, 0, totalHeight / 2);

        decorativeMapElemetns.attachChild(groundPlane);
    }

    /**
     * Creates all squares on the map.
     */
    private void createSquares() {
        // TODO: as parameter the array/list (or whatever) must be given, 
        // to generate the map.

        Vector3f offset = new Vector3f(-totalWidth / 2, 0, -totalHeight / 2);

        for (int x = 1; x < totalWidth - 1; x++) {
            for (int z = 1; z < totalHeight - 1; z++) {
                MapSquare m = new MapSquare();
                Vector3f position = new Vector3f(x, 0, z);
                position.addLocal(offset);

                // do not touch the y-coord, go to the class MapSquare to change it!
                m.setLocalTranslation(position);

                clickableMapElements.attachChild(m);
                EventManager.getInstance().addTimerEventListener(m);
            }
        }
    }

    /**
     * Gets the node with all decorative elements.
     * @return the node
     */
    public Node getDecorativeMapElemetns() {
        return decorativeMapElemetns;
    }

    //==========================================================================
    //===   Inner Classes
    //==========================================================================
    /**
     * Class for a map-square that represents a grid-square.
     * @author Hans Ferchland
     */
    class MapSquare extends Node implements TimerEventListener {
        //==========================================================================
        //===   Private Fields
        //==========================================================================

        private Material material;
        private ClickableGeometry geometry;
        private boolean hovered = false;
        private ColorRGBA fadeColor = SQUARE_COLOR.clone();
        //==========================================================================
        //===   Methods & Constructor
        //==========================================================================

        /**
         * Contructor for a map square, name and mesh will be generated automaticly.
         */
        public MapSquare() {
            super("MapSquare_" + getContinousSquareID());
            createGeometry();
        }

        public ClickableGeometry getGeometry() {
            return geometry;
        }

        /**
         * creates the geometry for a square with default size and color.
         */
        private void createGeometry() {
            geometry = new ClickableGeometry(name + "_Geometry", new Quad(SQUARE_SIZE, SQUARE_SIZE)) {

                /**
                 * Will be called if the square is clicked.
                 */
                @Override
                public void onRayCastClick(Vector2f mouse, CollisionResult result) {
                    // TODO: implement handling if a square is clicked
                    System.out.println(name + " clicked!");
                }

                @Override
                public void onRayCastMouseOver(Vector2f mouse, CollisionResult result) {
                    System.out.println(name + " hovered!");
                    hovered = true;
                }

                @Override
                public void onRayCastMouseLeft(Vector2f mouse, CollisionResult result) {
                    hovered = false;
                }
            };

            // assign material
            material = new Material(game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
            material.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
            material.setColor("Specular", ColorRGBA.White);
            material.setColor("Ambient", SQUARE_COLOR);   // ... color of this object
            material.setColor("Diffuse", SQUARE_COLOR);   // ... color of light being reflected
            material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
            geometry.setMaterial(material);
            geometry.setQueueBucket(Bucket.Translucent);

            float[] angles = {3 * (float) Math.PI / 2, 0, 0};

            geometry.setLocalRotation(new Quaternion(angles));
            geometry.setLocalTranslation(-SQUARE_SIZE / 2, 0.05f, SQUARE_SIZE / 2);

            this.attachChild(geometry);
        }

        @Override
        public void onTimedEvent(TimerEvent t) {
            if (hovered && fadeColor.a < 0.5f) {

                fadeColor.a += 0.05f;
                //fadeColor.clamp();

            } else if (!hovered && fadeColor.a >= 0.1f) {
                fadeColor.a -= 0.05f;
            }
            material.setColor("Ambient", fadeColor);   // ... color of this object
            material.setColor("Diffuse", fadeColor);   // ... color of light being reflected

        }

        @Override
        public float getPeriod() {
            return 0.05f;
        }
    }
}
