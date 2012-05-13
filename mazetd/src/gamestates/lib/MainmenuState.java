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

import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
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
        // Test init
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        mat.setColor("Ambient", ColorRGBA.Blue);   // ... color of this object
        mat.setColor("Diffuse", ColorRGBA.Blue);   // ... color of light being reflected
        geom.setMaterial(mat);

        // add blue cube to scene
        game.getRootNode().attachChild(geom);
        
        // add two lights to scene
        ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.Gray);
        
        pointLight = new PointLight();
        pointLight.setColor(new ColorRGBA(1, 0.5f, 0.5f, 1f));
        pointLight.setRadius(20);
        pointLight.setPosition(new Vector3f(1, 10, 0));
        
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
}
