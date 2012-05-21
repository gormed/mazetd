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
 * File: ScreenRayCast3D.java
 * Type: collisions.raycasts.ScreenRayCast3D
 * 
 * Documentation created: 14.05.2012 - 18:59:39 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package collisions.raycasts;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import events.EventManager;
import events.Mappings;
import events.MouseInputListener;
import mazetd.MazeTDGame;

/**
 * The class ScreenRayCast3D is responsible for firing events when a 
 * 3D-object is clicked.
 * @author Hans Ferchland
 */
public class ScreenRayCast3D implements MouseInputListener {
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of the singleton.
     */
    private ScreenRayCast3D() {
        clickable3D = new Node("Clickable3DNodes");
        game.getRootNode().attachChild(clickable3D);
        inputManager = game.getInputManager();
        inputManager.setCursorVisible(true);
        cam = game.getCamera();

        EventManager.getInstance().addMouseButtonEvent(
                Mappings.RAYCAST_3D,
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        EventManager.getInstance().addMouseButtonEvent(
                Mappings.RAYCAST_3D_MOVE,
                new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE),
                new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));

        EventManager.getInstance().addMouseInputListener(
                this,
                Mappings.RAYCAST_3D,
                Mappings.RAYCAST_3D_MOVE);
    }

    /**
     * Static method to retrieve the one and olny reference to the manager.
     * @return the reference of the ScreenRayCast3D
     */
    public static ScreenRayCast3D getInstance() {
        return ScreenRayCast3DHolder.INSTANCE;
    }

    /**
     * Holder class for the ScreenRayCast3D
     */
    private static class ScreenRayCast3DHolder {

        private static final ScreenRayCast3D INSTANCE = new ScreenRayCast3D();
    }
    //==========================================================================
    //===   Static Fields
    //==========================================================================
    public static final float MOUSE_MOVEMENT_TOLERANCE = 0.001f;
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Node clickable3D;
    private RayCast3DNode lastClicked = null;
    private RayCast3DNode lastHovered = null;
    private MazeTDGame game = MazeTDGame.getInstance();
    private InputManager inputManager;
    private Camera cam;
    private Vector2f lastMousePosition = Vector2f.ZERO.clone();

    //==========================================================================
    //===   Methods
    //==========================================================================
    /**
     * Retrieve the last clicked RayCast3DNode.
     * @return the last clicked RayCast3DNode
     */
    public RayCast3DNode getLastClicked() {
        return lastClicked;
    }

    /**
     * Retrieve the last hovered RayCast3DNode.
     * @return the last hovered RayCast3DNode
     */
    public RayCast3DNode getLastHovered() {
        return lastHovered;
    }

    /**
     * Adds a node to the clickable 3d objects.
     * @param object that will be clickable
     */
    public void addCollisonObject(Spatial object) {
        clickable3D.attachChild(object);
    }

    /**
     * Removes a specific node from the clickable 3d objects.
     * @param object that wont be clickable anymore
     */
    public void removeCollisonObject(Spatial object) {

        clickable3D.detachChild(object);
    }

    /**
     * Updates the mouse movement on screen for movement checks.
     * @param tpf the gamp between two calls
     */
    public void update(float tpf) {
        Vector2f mouse = inputManager.getCursorPosition();
        boolean isInWindow =
                mouse.x >= 0 && mouse.y >= 0
                && mouse.x <= cam.getWidth() && mouse.y <= cam.getHeight();
        if (isInWindow) {
            checkMouseMovement(mouse, tpf);
        }
        lastMousePosition = mouse.clone();
    }

    /**
     * Checks if the mouse moved over an object to call 
     * its onRayCastMouseOver() method.
     * @param mouse the mouse position
     * @param tpf the gap between 2 two calls
     */
    public void checkMouseMovement(Vector2f mouse, float tpf) {
        float diff = Math.abs(mouse.subtract(lastMousePosition).length());
        //==========================================================================
        //===   Mouse Moved
        //==========================================================================

        if (diff > MOUSE_MOVEMENT_TOLERANCE) {

            // 1. Reset results list.
            CollisionResults results = new CollisionResults();
            // 2. Aim the ray from cam loc to cam direction.
            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 0f).clone();
            Vector3f dir = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
            Ray ray = new Ray(click3d, dir);
            // 3. Collect intersections between Ray and Shootables in results list.
            clickable3D.collideWith(ray, results);
            // 4. Print the results
            System.out.println("----- 3D Collisions? " + results.size() + "-----");
            for (int i = 0; i < results.size(); i++) {
                // For each hit, we know distance, impact point, name of geometry.
                float dist = results.getCollision(i).getDistance();
                Vector3f pt = results.getCollision(i).getContactPoint();
                String hit = results.getCollision(i).getGeometry().getName();
                System.out.println("* 3D Collision #" + i);
                System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
            }
            // 5. Use the results (we mark the hit object)
            if (results.size() > 0) {
                // The closest collision point is what was truly hit:
                CollisionResult closest = results.getClosestCollision();
                Spatial n = closest.getGeometry();
                Spatial parent;

                if (n != null) {
                    if (n instanceof RayCast3DNode) {
                        decideLeftOrOver((RayCast3DNode) n, click2d, closest);
                    }
                    parent = n.getParent();
                    while (parent != null) {
                        if (parent instanceof RayCast3DNode) {
                            decideLeftOrOver((RayCast3DNode) n, click2d, closest);
                        }
                        parent = parent.getParent();
                    }
                }
//                // Let's interact - we mark the hit with a red dot.
//                mark.setLocalTranslation(closest.getContactPoint());
//                rootNode.attachChild(mark);
            } else {
                //lastHovered = null;
//                // No hits? Then remove the red mark.
//                rootNode.detachChild(mark);
            }

        }

    }

    /**
     * Invokes a RayCast3DNodes onMouseOver method.
     * @param r the hit RayCast3DNode
     * @param click2d the screen pos
     * @param closest the 3d hit params
     */
    private void invokeOnMouseOver(RayCast3DNode r, Vector2f click2d, CollisionResult closest) {
        r.onRayCastMouseOver(click2d, closest);
        lastHovered = r;
    }

    private void invokeOnMouseLeft(RayCast3DNode r, Vector2f click2d, CollisionResult closest) {
        r.onRayCastMouseLeft(click2d, closest);
    }

    private void decideLeftOrOver(RayCast3DNode node, Vector2f click2d, CollisionResult closest) {

        if (lastHovered != null && !node.equals(lastHovered)) {
            invokeOnMouseLeft(lastHovered, click2d, closest);
        }
        invokeOnMouseOver(node, click2d, closest);
    }

    /**
     * This method is raised on a click on the left mouse button to 
     * check 3d click events (ray casts).
     * All Spatials that implement RayCast3DNode will be clickable and will
     * execute the onRayCast3D() method on click.
     * 
     * @param name name of the mapping for the action
     * @param isPressed if the button (or key) is pressed or released
     * @param tpf the time-gap since the last update
     */
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {


        //==========================================================================
        //===   Mouse Clicks
        //==========================================================================

        if (!isPressed && name.equals(Mappings.RAYCAST_3D)) {
            // 1. Reset results list.
            CollisionResults results = new CollisionResults();
            // 2. Aim the ray from cam loc to cam direction.
            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 0f).clone();
            Vector3f dir = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
            Ray ray = new Ray(click3d, dir);
            // 3. Collect intersections between Ray and Shootables in results list.
            clickable3D.collideWith(ray, results);
            // 4. Print the results
            System.out.println("----- 3D Collisions? " + results.size() + "-----");
            for (int i = 0; i < results.size(); i++) {
                // For each hit, we know distance, impact point, name of geometry.
                float dist = results.getCollision(i).getDistance();
                Vector3f pt = results.getCollision(i).getContactPoint();
                String hit = results.getCollision(i).getGeometry().getName();
                System.out.println("* 3D Collision #" + i);
                System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
            }
            // 5. Use the results (we mark the hit object)
            if (results.size() > 0) {
                // The closest collision point is what was truly hit:
                CollisionResult closest = results.getClosestCollision();
                Spatial n = closest.getGeometry();
                Spatial parent;

                if (n != null) {
                    if (n instanceof RayCast3DNode) {
                        invokeOnClick((RayCast3DNode) n, click2d, closest);
                    }
                    parent = n.getParent();
                    while (parent != null) {
                        if (parent instanceof RayCast3DNode) {
                            invokeOnClick((RayCast3DNode) n, click2d, closest);
                        }
                        parent = parent.getParent();
                    }
                }
//                // Let's interact - we mark the hit with a red dot.
//                mark.setLocalTranslation(closest.getContactPoint());
//                rootNode.attachChild(mark);
            } else {
                lastClicked = null;
//                // No hits? Then remove the red mark.
//                rootNode.detachChild(mark);
            }
        }
    }

    /**
     * Invokes a RayCast3DNodes onClick method.
     * @param r the hit RayCast3DNode
     * @param click2d the screen pos
     * @param closest the 3d hit params
     */
    private void invokeOnClick(RayCast3DNode r, Vector2f click2d, CollisionResult closest) {
        r.onRayCastClick(click2d, closest);
        lastClicked = r;
    }
}
