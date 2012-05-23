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
 * File: Tower.java
 * Type: entities.Tower
 * 
 * Documentation created: 16.05.2012 - 17:41:15 by Hady Khalifa
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import entities.base.ClickableEntity;
import mazetd.MazeTDGame;

/**
 * The class Tower.
 * @author Hady Khalifa & Hans Ferchland
 * @version 0.2
 */
public class Tower extends ClickableEntity {
    //==========================================================================
    //===   Constants
    //========================================================================== 

    private static final int TOWER_SAMPLES = 20;
    private static final float TOWER_HEIGHT = 1.2f;
    private static final float TOWER_SIZE = 0.3f;
    private static final float ROOF_SIZE = 0.4f;
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Geometry roofGeometry;
    private Geometry wallGeometry;
    private Material roofMaterial;
    private Material wallMaterial;
    private Vector3f position;

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    public Tower(String name, Vector3f position) {
        super(name);
        this.position = position;
    }

    @Override
    public Node createNode(MazeTDGame game) {
        super.createNode(game);

        // materials
        roofMaterial = new Material(
                game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        roofMaterial.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        roofMaterial.setColor("Specular", ColorRGBA.White);
        roofMaterial.setColor("Ambient", ColorRGBA.Orange);   // ... color of this object
        roofMaterial.setColor("Diffuse", ColorRGBA.Orange);   // ... color of light being reflected

        wallMaterial = new Material(
                game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        wallMaterial.setBoolean("UseMaterialColors", true);  // Set some parameters, e.g. blue.
        wallMaterial.setColor("Specular", ColorRGBA.White);
        wallMaterial.setColor("Ambient", ColorRGBA.Gray);   // ... color of this object
        wallMaterial.setColor("Diffuse", ColorRGBA.Gray);   // ... color of light being reflected

        // geometry
        float[] angles = {(float) Math.PI / 2, 0, 0};
        // roof
        Cylinder roof = new Cylinder(
                TOWER_SAMPLES, 
                TOWER_SAMPLES, 
                ROOF_SIZE, 0, 
                ROOF_SIZE, false, false);

        roofGeometry = new Geometry(
                name + "_RoofGeometry", roof);
        roofGeometry.setMaterial(roofMaterial);
        //roofGeometry.setCullHint(CullHint.Never);
        roofGeometry.setLocalTranslation(0, TOWER_HEIGHT + ROOF_SIZE / 2, 0);
        roofGeometry.setLocalRotation(new Quaternion(angles));

        // wall
        Cylinder wall = new Cylinder(
                TOWER_SAMPLES,
                TOWER_SAMPLES,
                TOWER_SIZE,
                TOWER_HEIGHT,
                true);
        
        
        wallGeometry = new Geometry(
                name + "_WallGeometry", wall);
        wallGeometry.setMaterial(wallMaterial);
        //wallGeometry.setCullHint(CullHint.Never);
        wallGeometry.setLocalTranslation(0, TOWER_HEIGHT / 2, 0);
        wallGeometry.setLocalRotation(new Quaternion(angles));
        
        // hierarchy
        clickableEntityNode.attachChild(wallGeometry);
        clickableEntityNode.attachChild(roofGeometry);
        // apply position to main node
        clickableEntityNode.setLocalTranslation(position);

        return clickableEntityNode;
    }

    @Override
    protected void update(float tpf) {
    }

    @Override
    public void onClick() {
        System.out.println("You clicked tower: #" + getEntityId() + " - " + getName());
    }

    @Override
    public void onMouseOver() {
        
    }

    @Override
    public void onMouseLeft() {
        
    }

    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
