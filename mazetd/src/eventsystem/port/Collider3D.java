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
 * File: Collider3D.java
 * Type: eventsystem.port.Collider3D
 * 
 * Documentation created: 23.05.2012 - 21:47:57 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package eventsystem.port;

/**
 *
 * @author Hans Ferchland
 */
public class Collider3D {
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of Collider3D.
     */
    private Collider3D() {
    }

    /**
     * The static method to retrive the one and only instance of Collider3D.
     */
    public static Collider3D getInstance() {
        return Collider3DHolder.INSTANCE;
    }

    /**
     * The holder-class Collider3DHolder for the Collider3D.
     */
    private static class Collider3DHolder {

        private static final Collider3D INSTANCE = new Collider3D();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    //==========================================================================
    //===   Methods
    //==========================================================================
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
