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
 * File: SingleplayerState.java
 * Type: gamestates.lib.SingleplayerState
 * 
 * Documentation created: 13.05.2012 - 23:12:22 by Hans
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gamestates.lib;

import com.jme3.collision.CollisionResult;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import entities.geometry.ClickableGeometry;
import gamestates.Gamestate;
import gamestates.GamestateManager;
import logic.Level;
import mazetd.IsoCameraControl;
import mazetd.MazeTDGame;

/**
 * The class SingleplayerState is the main state the game will process.
 * @author Hans Ferchland
 * @version 0.3
 */
public class SingleplayerState extends Gamestate {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    /** The game. */
    private MazeTDGame game;
    
    /** The ambient light. */
    private AmbientLight ambientLight;
    
    /** The point light. */
    private PointLight pointLight;
    
    /** The level. */
    private Level level;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Creates an instance of the state.
     */
    public SingleplayerState() {
        super(GamestateManager.SINGLEPLAYER_STATE);
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#update(float)
     */
    @Override
    public void update(float tpf) {

        updateLightsAndShadows(tpf);
        level.update(tpf);

    }
    
    /**
     * Updates the shadows and light position.
     * @param tpf the time-gap
     */
    private void updateLightsAndShadows(float tpf) {
        pointLight.setPosition(game.getCamera().getLocation().clone());
        Vector3f camLoc = game.getCamera().getLocation().clone();
        camLoc.y = IsoCameraControl.CAMERA_HEIGHT / 2;
        Vector3f dir = Vector3f.ZERO.clone().subtract(camLoc);
        if (game.getPssmShadowRenderer() != null) {
            game.getPssmShadowRenderer().setDirection(dir.normalize());
        }
    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#loadContent(mazetd.MazeTDGame)
     */
    @Override
    protected void loadContent(MazeTDGame game) {
        this.game = game;


        game.getIsoCameraControl().reset();

        // setup level
        this.level = Level.getInstance();
        level.initialize();

        // add two lights to scene
        ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.DarkGray);

        pointLight = new PointLight();
        pointLight.setColor(new ColorRGBA(1f, 1f, 1f, 1f));
        pointLight.setRadius(60);
        pointLight.setPosition(new Vector3f(0, 20, -10));

        game.getRootNode().addLight(pointLight);
        game.getRootNode().addLight(ambientLight);

        game.setPause(false);
        //test();
    }

    /*
     * Only for testing purposes.
     */
    /**
     * Test.
     */
    private void test() {
        ////////////////////////////////////////////////////////////////
        //                      TESTING CODE
        ////////////////////////////////////////////////////////////////
        // Test init blue cube
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        mat.setColor("Ambient", ColorRGBA.Blue);   // ... color of this object
        mat.setColor("Diffuse", ColorRGBA.Blue);   // ... color of light being reflected
        geom.setMaterial(mat);


        Sphere s = new Sphere(10, 10, 1);
        ClickableGeometry geom2 = new ClickableGeometry("Sphere", s) {

            @Override
            public void onRayCastClick(Vector2f mouse, CollisionResult result) {
            }

            @Override
            public void onRayCastMouseOver(Vector2f mouse, CollisionResult result) {
            }

            @Override
            public void onRayCastMouseLeft(Vector2f mouse, CollisionResult result) {
            }
        };
        geom2.setLocalTranslation(5, 0, 0);

        Material mat2 = new Material(game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat2.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        mat2.setColor("Ambient", ColorRGBA.Red);   // ... color of this object
        mat2.setColor("Diffuse", ColorRGBA.Red);   // ... color of light being reflected
        geom2.setMaterial(mat2);

        // add blue cube to scene

        //game.getRootNode().attachChild(geom);
        // add red sphere to clickable 3d objects

        //ScreenRayCast3D.getInstance().addCollisonObject(geom2);


    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#unloadContent()
     */
    @Override
    protected void unloadContent() {

        level.destroy();
        // remove all content from sg, gc will take care of the rest
        game.getRootNode().detachAllChildren();
        game.getRootNode().removeLight(ambientLight);
        game.getRootNode().removeLight(pointLight);

    }

    /* (non-Javadoc)
     * @see gamestates.Gamestate#terminate()
     */
    @Override
    public void terminate() {
        super.terminate();
        unloadContent();
    }
}
