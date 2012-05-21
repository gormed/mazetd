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
 * File: MainmenuState.java
 * Type: gamestates.lib.MainmenuState
 * 
 * Documentation created: 13.05.2012 - 23:13:37 by Hans
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gamestates.lib;

import events.RayCast3DNode;
import events.ScreenRayCast3D;
import com.jme3.collision.CollisionResult;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import entities.Map;
import entities.base.EntityManager;
import gamestates.Gamestate;
import gamestates.GamestateManager;
import mazetd.MazeTDGame;

/**
 * Mainmenu state represets the main menu with all its content, interactions and updates.
 * @author Hans Ferchland
 * @version 0.1
 */
public class MainmenuState extends Gamestate {

    private MazeTDGame game;
    private AmbientLight ambientLight;
    private PointLight pointLight;

    public MainmenuState() {
        super(GamestateManager.MAINMENU_STATE);
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    protected void loadContent(MazeTDGame game) {
        this.game = game;
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
        ClickableGeometry geom2 = new ClickableGeometry("Sphere", s);
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

        EntityManager.getInstance().createTower(
                "FirstTower", new Vector3f(0, 0, 0));
        
        Map m = new Map();
        game.getRootNode().attachChild(m.getDecorativeMapElemetns());

        // add two lights to scene
        ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.Gray);

        pointLight = new PointLight();
        pointLight.setColor(new ColorRGBA(1, 1, 1, 1f));
        pointLight.setRadius(40);
        pointLight.setPosition(new Vector3f(0, 10, 0));

        game.getRootNode().addLight(pointLight);
        game.getRootNode().addLight(ambientLight);
    }

    @Override
    protected void unloadContent() {
        // remove all content from sg, gc will take care of the rest
        game.getRootNode().detachAllChildren();
        game.getRootNode().removeLight(ambientLight);
        game.getRootNode().removeLight(pointLight);
    }

    class ClickableGeometry extends Geometry implements RayCast3DNode {

        public ClickableGeometry(String name, Mesh mesh) {
            super(name, mesh);
        }

        @Override
        public void onRayCastClick(Vector2f mouse, CollisionResult result) {
            System.out.println("Hello Picking!");
        }

        @Override
        public void onRayCastMouseOver(Vector2f mouse, CollisionResult result) {
        }

        @Override
        public void onRayCastMouseLeft(Vector2f mouse, CollisionResult result) {
            
        }
    }
}
