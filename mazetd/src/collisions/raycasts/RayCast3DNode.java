/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package collisions.raycasts;

import com.jme3.collision.CollisionResult;

/**
 * This interface is for Nodes that can be added into scenegraph for 3d clicking.
 * @author Hans Ferchland
 */
public interface RayCast3DNode {
    public abstract void onRayCast3D(CollisionResult result);
}
