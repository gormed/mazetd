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

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import mazetd.MazeTDGame;

/**
 * The class Map.
 * @author Hans Ferchland
 * @version 0.1
 */
public class Map extends Node {
    //==========================================================================
    //===   Private Fields
    //==========================================================================

    private Geometry groundPlane;
    private Material groundMaterial;
    private float totalHeight;
    private float totalWidth;
    private MazeTDGame game = MazeTDGame.getInstance();
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public Map() {
        super("GraphicalMap");
        totalHeight = 20;
        totalWidth = 20;
        createGround();
    }

    private void createGround() {
        Quad q = new Quad(totalHeight, totalWidth);
        groundPlane = new Geometry("GroundPlane", q);

        groundMaterial = new Material(game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        groundMaterial.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        groundMaterial.setColor("Specular", ColorRGBA.White);
        groundMaterial.setColor("Ambient", ColorRGBA.Gray);   // ... color of this object
        groundMaterial.setColor("Diffuse", ColorRGBA.Gray);   // ... color of light being reflected
        
        groundPlane.setMaterial(groundMaterial);
        
        float[] angles = {  3*(float)Math.PI/2, 0, 0};
        
        this.setLocalRotation(new Quaternion(angles));
        this.setLocalTranslation(-totalWidth/2, -1, totalHeight/2);
        
        this.attachChild(groundPlane);
    }
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
