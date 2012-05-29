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
 * File: Orb.java
 * Type: entities.Orb
 * 
 * Documentation created: 21.05.2012 - 22:02:25 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import entities.base.ClickableEntity;
import mazetd.MazeTDGame;

/**
 * The class Orb.
 * @author Hans Ferchland
 * @version
 */
public class Orb extends ClickableEntity {
    //==========================================================================
    //===   Constants
    //==========================================================================

    public static final float HEIGHT_OVER_GROUND = 0.4f;
    private static final int ORB_SAMPLES = 10;
    private static final float ORB_SIZE = 0.125f;
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Geometry orbGeometry;
    private Material orbMaterial;
    private Vector3f position;
    private ColorRGBA color;
    private Element element;
    private float height = 0.4f;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    public Orb(String name, Vector3f position, ColorRGBA color) {
        super(name);
        this.position = position;
        this.color = color;
    }

    @Override
    public Node createNode(MazeTDGame game) {
        super.createNode(game);

        // orbs Material        
        orbMaterial = new Material(
                game.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        orbMaterial.setColor("Color", color.add(new ColorRGBA(0.2f, 0.2f, -0.1f, 0)));
        orbMaterial.setColor("GlowColor", color);
        orbMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        Sphere s = new Sphere(ORB_SAMPLES, ORB_SAMPLES, ORB_SIZE);

        orbGeometry = new Geometry("orb_" + name, s);
        orbGeometry.setMaterial(orbMaterial);
        orbGeometry.setLocalTranslation(0, HEIGHT_OVER_GROUND, 0);
        orbGeometry.setQueueBucket(Bucket.Transparent);

        clickableEntityNode.setLocalTranslation(position);
        clickableEntityNode.attachChild(orbGeometry);

        return clickableEntityNode;
    }

    @Override
    protected void update(float tpf) {
        height += tpf;
        float y = 0.1f * (float) Math.sin(1.5f * height);
        orbGeometry.setLocalTranslation(0, HEIGHT_OVER_GROUND + y, 0);
    }

    @Override
    public void onClick() {
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

    private abstract class Element {

        public Element() {
        }
    }
}
