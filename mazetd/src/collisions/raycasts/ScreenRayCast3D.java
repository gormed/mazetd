/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

        EventManager.getInstance().addMouseInputEvent(
                Mappings.RAYCAST_3D,
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        EventManager.getInstance().addMouseInputListener(
                this,
                Mappings.RAYCAST_3D);
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
    //===   Private Fields
    //==========================================================================
    private Node clickable3D;
    private RayCast3DNode lastHit = null;
    private MazeTDGame game = MazeTDGame.getInstance();
    private InputManager inputManager;
    private Camera cam;
    //==========================================================================
    //===   Methods
    //==========================================================================

    public RayCast3DNode getLastHit() {
        return lastHit;
    }

    /**
     * Adds a node to the clickable 3d objects.
     * @param object that will be clickable
     */
    public void addCollisonObject(RayCast3DNode object) throws IllegalArgumentException {
        if (object instanceof Spatial) {
            clickable3D.attachChild((Spatial) object);
        } else {
            throw new IllegalArgumentException("RayCast3DNode has to be a jME3 Spatial to be raycasted!");
        }
    }

    /**
     * Removes a specific node from the clickable 3d object.
     * @param object that wont be clickable anymore
     */
    public void removeCollisonObject(RayCast3DNode object) throws IllegalArgumentException {
        if (object instanceof Spatial) {
            clickable3D.detachChild((Spatial) object);
        } else {
            throw new IllegalArgumentException("RayCast3DNode has to be a jME3 Spatial to be raycasted!");
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
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
                if (n != null && n instanceof RayCast3DNode) {
                    RayCast3DNode hit = (RayCast3DNode) n;
                    hit.onRayCast3D(closest);
                    lastHit = hit;
                }
//                // Let's interact - we mark the hit with a red dot.
//                mark.setLocalTranslation(closest.getContactPoint());
//                rootNode.attachChild(mark);
            } else {
//                // No hits? Then remove the red mark.
//                rootNode.detachChild(mark);
            }
        }
    }
}
