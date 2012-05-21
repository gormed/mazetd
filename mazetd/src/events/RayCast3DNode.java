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
 * File: RayCast3DNode.java
 * Type: collisions.raycasts.RayCast3DNode
 * 
 * Documentation created: 14.05.2012 - 18:59:39 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package events;

import com.jme3.collision.CollisionResult;
import com.jme3.math.Vector2f;

/**
 * This interface is for Spatials that can be added into scenegraph for 3d clicking.
 * @author Hans Ferchland
 */
public interface RayCast3DNode {
    /**
     * This method is executed if the Spatial implementing this method is clicked.
     * Add the given spatial to the ScreenRayCast3D as clickable!
     * 
     * @param result the clicking result, including 3d-point, normal and some more
     */
    public void onRayCastClick(Vector2f mouse, CollisionResult result);
    
    public void onRayCastMouseOver(Vector2f mouse, CollisionResult result);
    
    public void onRayCastMouseLeft(Vector2f mouse, CollisionResult result);
}
