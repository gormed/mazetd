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

import com.jme3.collision.MotionAllowedListener;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import entities.Map.MapSquare;

/**
 *
 * @author Hans Ferchland
 * @version 0.1 #2
 */
public class IsoCameraControl implements ActionListener {
    //==========================================================================
    //===   Static Fields
    //==========================================================================

    /** The Constant CAMERA_HEIGHT. */
    public static float CAMERA_HEIGHT = 17;
    /** The Constant CAMERA_ANGLE. */
    private static final float CAMERA_ANGLE = (float) Math.PI / 2.5f;
    /** The Constant CAMERA_ACTIVE_ZONE_X for the activation of camera movement in x-dir. */
    private static final int CAMERA_ACTIVE_ZONE_X = 50;
    /** The Constant CAMERA_ACTIVE_ZONE_Y for the activation of camera movement in y-dir. */
    private static final int CAMERA_ACTIVE_ZONE_Y = 50;
    /** The Constant CAMERA_MOVE_SPEED for the speed the camera moves. */
    private static final float CAMERA_MOVE_SPEED = 8f;
    //==========================================================================
    //===   Private/Protected Fields
    //==========================================================================
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
    /** The game reference. */
    protected MazeTDGame game;
    /** The reference of the input manager. */
    protected InputManager inputManager;

    //==========================================================================
    //===   Constructor & Methods
    //==========================================================================
    /**
     * The contructor of the camera for isometric view.
     * @param game the reference to the game-application
     */
    public IsoCameraControl(MazeTDGame game) {
        this.cam = game.getCamera();
        this.game = game;
        this.inputManager = game.getInputManager();
        setupCamera();
    }

    /**
     * Method for camera setup.
     */
    private void setupCamera() {
        reset();
    }

    /**
     * Updates the camera each tick (100 times a sec in average).
     * @param tpf the timegap in seconds since the last frame
     */
    public void updateCamera(float tpf) {
        if (!enabled) {
            return;
        }
        Vector2f mouse = inputManager.getCursorPosition();

        Vector3f loc = cam.getLocation();
        float dist = 0;
        boolean yCondition = (mouse.y > CAMERA_ACTIVE_ZONE_Y && mouse.y < cam.getHeight() - CAMERA_ACTIVE_ZONE_Y);


        if (mouse.x < CAMERA_ACTIVE_ZONE_X && yCondition) {
            dist = (CAMERA_ACTIVE_ZONE_X - mouse.x) / CAMERA_ACTIVE_ZONE_X;
            loc.addLocal(new Vector3f(tpf * CAMERA_MOVE_SPEED * dist, 0, 0));
        } else if (mouse.x > cam.getWidth() - CAMERA_ACTIVE_ZONE_X && yCondition) {
            dist = -((cam.getWidth() - CAMERA_ACTIVE_ZONE_X) - mouse.x) / CAMERA_ACTIVE_ZONE_X;
            loc.addLocal(new Vector3f(-tpf * CAMERA_MOVE_SPEED * dist, 0, 0));
        } else {
            return;
        }
        cam.setLocation(loc);

    }

    /**
     * Resets the camera to the initial position.
     */
    public void reset() {
        setEnabled(true);
        float[] rot = {CAMERA_ANGLE, 0, 0};
        initialUpVec = cam.getUp().clone();
        cam.setLocation(new Vector3f(0, CAMERA_HEIGHT, -CAMERA_HEIGHT / 2.2f));
        cam.setRotation(new Quaternion(rot));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void lookAtMapSquare(MapSquare square) {
        if (square != null) {
            cam.setLocation(new Vector3f(square.getLocalTranslation().x, CAMERA_HEIGHT, -CAMERA_HEIGHT / 2.2f));
        }
    }

    /**
     * Till jet unused.
     * @param name
     * @param isPressed
     * @param tpf 
     */
    public void onAction(String name, boolean isPressed, float tpf) {
    }
}
