/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gamestates.lib;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
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

    public MainmenuState() {
        super(GamestateManager.MAINMENU_STATE);
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    protected void loadContent(MazeTDGame game) {
        // Test init
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(game.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        game.getRootNode().attachChild(geom);
    }

    @Override
    protected void unloadContent() {
    }
}
