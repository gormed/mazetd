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
 * File: IsoCameraControl.java
 * Type: mazetd.IsoCameraControl
 * 
 * Documentation created: 13.05.2012 - 23:13:37 by Hans
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package mazetd;

import com.bulletphysics.collision.broadphase.Dbvt.Node;
import com.jme3.collision.MotionAllowedListener;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 *
 * @author Hans Ferchland
 * @version 0.1 #2
 */
public class IsoCameraControl implements ActionListener {

    /** The Constant CAMERA_HEIGHT. */
    public static float CAMERA_HEIGHT = 15;
    /** The Constant CAMERA_ANGLE. */
    public static final float CAMERA_ANGLE = (float) Math.PI / 2.5f;
    /** The cam. */
    protected Camera cam;
    /** The root node. */
    protected Node rootNode;
    /** The initial up vec. */
    protected Vector3f initialUpVec;
    /** The drag speed. */
    protected float dragSpeed = 3f;
    /** The move speed. */
    protected float moveSpeed = 3f;
    /** The motion allowed. */
    protected MotionAllowedListener motionAllowed = null;
    /** The enabled. */
    protected boolean enabled = true;
    /** The last click state. */
    protected boolean lastClickState;
    /** The current click state. */
    protected boolean currentClickState;
    /** The is dragged. */
    protected boolean isDragged;
    /** The initial drag pos. */
    protected Vector2f initialDragPos;
    /** The input manager. */
    protected InputManager inputManager;

    public IsoCameraControl(Camera cam) {
        this.cam = cam;

        setupCamera();
    }

    private void setupCamera() {
        reset();
    }

    public void reset() {
        float[] rot = {CAMERA_ANGLE, 0, 0};
        initialUpVec = cam.getUp().clone();
        cam.setLocation(new Vector3f(0, CAMERA_HEIGHT, -CAMERA_HEIGHT/2.5f));
        cam.setRotation(new Quaternion(rot));
    }

    public void onAction(String name, boolean isPressed, float tpf) {
    }
}
