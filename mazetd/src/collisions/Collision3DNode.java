/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package collisions;

import com.jme3.collision.CollisionResult;
import com.jme3.scene.Node;

/**
 * This class is for Nodes that can be added into scenegraph for 3d clicking.
 * @author Hans Ferchland
 */
public abstract class Collision3DNode extends Node {
    
    
    public abstract void onCollision3D(CollisionResult result);
}
