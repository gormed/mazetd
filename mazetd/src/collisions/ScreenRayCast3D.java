/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package collisions;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
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
    private Collision3DNode lastHit = null;
    private MazeTDGame game = MazeTDGame.getInstance();
    //==========================================================================
    //===   Methods
    //==========================================================================

    public Collision3DNode getLastHit() {
        return lastHit;
    }
    
    /**
     * Adds a node to the clickable 3d objects.
     * @param object that will be clickable
     */
    public void addCollisonObject(Collision3DNode object) {
        clickable3D.attachChild(object);
    }

    /**
     * Removes a specific node from the clickable 3d object.
     * @param object that wont be clickable anymore
     */
    public void removeCollisonObject(Collision3DNode object) {
        clickable3D.detachChild(object);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed && name.equals(Mappings.RAYCAST_3D)) {
            // 1. Reset results list.
            CollisionResults results = new CollisionResults();
            // 2. Aim the ray from cam loc to cam direction.
            Ray ray = new Ray(game.getCamera().getLocation(), game.getCamera().getDirection());
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
                Node n = closest.getGeometry().getParent();
                if (n != null && n instanceof Collision3DNode) {
                    Collision3DNode hit = (Collision3DNode) n;
                    hit.onCollision3D(closest);
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
