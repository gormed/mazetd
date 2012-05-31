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
 * File: ClickableGeometry.java
 * Type: collisions.raycasts.ClickableGeometry
 * 
 * Documentation created: 16.05.2012 - 17:34:22 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.geometry;

import eventsystem.interfaces.Clickable3D;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;

/**
 * The class ClickableGeometry for geometry that listens to mouse-events.
 * @author Hans Ferchland
 * @version 0.2
 */
public abstract class ClickableGeometry extends Geometry implements Clickable3D {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    public ClickableGeometry(String name) {
        super(name);
    }

    public ClickableGeometry(String name, Mesh mesh) {
        super(name, mesh);
    }
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
