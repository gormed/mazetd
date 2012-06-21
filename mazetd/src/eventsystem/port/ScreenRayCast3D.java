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
package eventsystem.port;

import eventsystem.interfaces.Clickable3D;
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
import eventsystem.EventManager;
import eventsystem.listener.MouseInputListener;
import mazetd.MazeTDGame;

/**
 * The class ScreenRayCast3D is responsible for firing events when a 
 * 3D-object is clicked.
 * @author Hans Ferchland
 */
public class ScreenRayCast3D implements MouseInputListener {
    public static String RAYCAST_3D = "Raycast_3D";
    
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of the singleton.
     */
    private ScreenRayCast3D() {
        // add listener for left click
        EventManager.getInstance().addMouseButtonEvent(
                RAYCAST_3D,
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
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
    public static final float MOUSE_MOVEMENT_TOLERANCE = 0.25f;
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Node clickable3D;
    private Clickable3D lastClicked = null;
    private Clickable3D lastHovered = null;
    private MazeTDGame game = MazeTDGame.getInstance();
    private InputManager inputManager = MazeTDGame.getInstance().getInputManager();
    private Vector3f lastWorldHit;
    private Camera cam = MazeTDGame.getInstance().getCamera();
    private Vector2f lastMousePosition = Vector2f.ZERO.clone();
    private boolean initialized;
    //==========================================================================
    //===   Methods
    //==========================================================================

    /**
     * Initializes the class if not already done or it was destroyed.
     */
    public void initialize() {
        if (initialized) {
            return;
        }
        clickable3D = new Node("Clickable3DNodes");
        game.getRootNode().attachChild(clickable3D);
        inputManager.setCursorVisible(true);

        EventManager.getInstance().addMouseInputListener(
                this,
                RAYCAST_3D);
        initialized = true;
    }

    /**
     * Destroys the class, removes all resources from jme3.
     */
    public void destroy() {
        if (!initialized) {
            return;
        }

        EventManager.getInstance().
                removeMouseInputListener(this);
        lastClicked = null;
        lastHovered = null;
        clickable3D.detachAllChildren();
        game.getRootNode().detachChild(clickable3D);
        clickable3D = null;
        lastMousePosition = Vector2f.ZERO.clone();
        initialized = false;
    }

    /**
     * Checks if the class was already initialized.
     * @return true if initialize false otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Retrieve the last clicked RayCast3DNode.
     * @return the last clicked RayCast3DNode
     */
    public Clickable3D getLastClicked() {
        return lastClicked;
    }

    /**
     * Retrieve the last hovered RayCast3DNode.
     * @return the last hovered RayCast3DNode
     */
    public Clickable3D getLastHovered() {
        return lastHovered;
    }

    /**
     * Gets the last point hit by a ray.
     * @return the 3D position of the hit
     */
    public Vector3f getLastWorldHit() {
        return lastWorldHit;
    }

    /**
     * Adds a node to the clickable 3d objects.
     * @param object that will be clickable
     */
    public void addClickableObject(Spatial object) {
        clickable3D.attachChild(object);
    }

    /**
     * Removes a specific node from the clickable 3d objects.
     * @param object that wont be clickable anymore
     */
    public void removeClickableObject(Spatial object) {

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
            lastWorldHit = click3d.clone();
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
                // this is where the magic happens
                // if there was a geometry check
                if (n != null) {
                    // okay, we have geometry, is it inheriting Clickable3D
                    if (n instanceof Clickable3D) {
                        // okay than decide if we entered the geometry or
                        // left it
                        decideLeftOrOver(n, click2d, closest);
                    }
                    // does the node have a parent
                    parent = n.getParent();
                    // really?
                    while (parent != null) {
                        // okay, than check if Clickable3D
                        if (parent instanceof Clickable3D) {
                            try {
                                // okay than decide if we entered the geometry 
                                // or left it
                                decideLeftOrOver(parent, click2d, closest);
                            } catch (ClassCastException castException) {
                                System.err.println(castException.getStackTrace());
                            }
                        }
                        // okay, that find me the next parent and do this again
                        parent = parent.getParent();
                    }
                }
            } else {
                // well otherwise we have no selection at all
            }

        }

    }

    /**
     * Invokes a RayCast3DNodes onMouseOver method.
     * @param r the hit RayCast3DNode
     * @param click2d the screen pos
     * @param closest the 3d hit params
     */
    private void invokeOnMouseOver(Clickable3D r, Vector2f click2d, CollisionResult closest) {
        r.onRayCastMouseOver(click2d, closest);
        lastHovered = r;
    }

    /**
     * Invokes a RayCast3DNodes onMouseLeft method.
     * @param r the hit RayCast3DNode
     * @param click2d the screen pos
     * @param closest the 3d hit params
     */
    private void invokeOnMouseLeft(Clickable3D r, Vector2f click2d, CollisionResult closest) {
        r.onRayCastMouseLeft(click2d, closest);
    }

    /**
     * Desides if a node was left with mouse-pointer or entered.
     * @param node
     * @param click2d
     * @param closest 
     */
    private void decideLeftOrOver(Spatial spatial, Vector2f click2d, CollisionResult closest) {
        if (spatial instanceof Clickable3D) {
            try {
                Clickable3D node = (Clickable3D) spatial;
                if (lastHovered != null && !node.equals(lastHovered)) {
                    invokeOnMouseLeft(lastHovered, click2d, closest);
                }
                invokeOnMouseOver(node, click2d, closest);
            } catch (ClassCastException castException) {
                System.err.println(castException.getStackTrace());
            }
        }
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

        if (!isPressed && name.equals(RAYCAST_3D)) {
            // 1. Reset results list.
            CollisionResults results = new CollisionResults();
            // 2. Aim the ray from cam loc to cam direction.
            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 0f).clone();
            lastWorldHit = click3d.clone();
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
                // recursivly check if object is clickable or not
                if (n != null) {
                    invokeOnClick(n, click2d, closest);
                    parent = n.getParent();
                    while (parent != null) {
                        invokeOnClick(parent, click2d, closest);
                        parent = parent.getParent();
                    }
                }
            } else {
                // reset the last clicked object if none
                lastClicked = null;
            }
        }
    }

    /**
     * Invokes a RayCast3DNodes onClick method.
     * @param r the hit RayCast3DNode
     * @param click2d the screen pos
     * @param closest the 3d hit params
     */
    private void invokeOnClick(Spatial spatial, Vector2f click2d, CollisionResult closest) {
        if (spatial instanceof Clickable3D) {
            try {
                Clickable3D node = (Clickable3D) spatial;
                node.onRayCastClick(click2d, closest);
                lastClicked = node;

            } catch (ClassCastException castException) {
                System.err.println(castException.getStackTrace());
            }
        }
    }
}
