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
 * File: Stone.java
 * Type: entities.Stone
 * 
 * Documentation created: 13.06.2012 - 19:17:35 by Hady Khalifa
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import entities.Map.MapSquare;
import entities.base.AbstractEntity;
import jme3tools.optimize.GeometryBatchFactory;
import logic.Level;
import mazetd.MazeTDGame;

/**
 * The class Stone.
 * @author Hady Khalifa
 * @version
 */
public class Stone extends AbstractEntity {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private final MapSquare square;
    private Vector3f position;
    private Material material;
    private Geometry geometry;
    private ColorRGBA color;

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    /**
     * Constructor
     * 
     * @param name
     * @param square 
     */
    public Stone(String name, MapSquare square) {
        super(name);
        this.square = square;
        this.color = new ColorRGBA(0, 0, 0, 1f);
    }

    @Override
    protected void update(float tpf) {
    }

    @Override
    public Node createNode(MazeTDGame game) {
        super.createNode(game);
        createGeometry(game);
        return geometryNode;
    }

    /**
     * Creates the towers material and geometry.
     * @param game the game reference
     */
    private void createGeometry(MazeTDGame game) {
        // apply map square
        Vector3f pos = square.getLocalTranslation();
        this.position = new Vector3f(pos.x, 0.40f, pos.z);
        square.setStone(this);

   
        material = new Material(game.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        material.setBoolean("UseMaterialColors", true);
        material.setColor("Specular", new ColorRGBA(ColorRGBA.Gray));

        if (material.getMaterialDef().getName().equals("Phong Lighting")) {
            Texture t = game.getAssetManager().loadTexture("Textures/Shader/toon.png");
                t.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
                t.setMagFilter(Texture.MagFilter.Nearest);
            material.setTexture("ColorRamp", t);
            material.setColor("Diffuse", ColorRGBA.Gray);
//          material.setColor("Diffuse", new ColorRGBA(0.25f, 0.25f, 0.25f, 1.0f));
            material.setBoolean("VertexLighting", true);
        }
        
        // Geometry
        float[] angles = {(float) -Math.PI / 2f, 0, 0};

        // Stone Model
        Spatial stone =
                game.getAssetManager().loadModel("Models/Stone/rock1.j3o");
                    //TODO Rotate randomly 
        double rotation =360* Math.random();
        stone.rotate(0  ,  0,(float)rotation);
        stone.setLocalScale(0.25f);
             //
        stone.setMaterial(material);
        stone.setLocalRotation(new Quaternion(angles));

        Node n = new Node("BatchNode");
        n.attachChild(stone);

        stone = GeometryBatchFactory.optimize(n);
        stone.setQueueBucket(Bucket.Translucent);

        
        // apply position to main node
        geometryNode.attachChild(stone);
        geometryNode.setLocalTranslation(position);
        stone.setShadowMode(ShadowMode.CastAndReceive);

    }
}
