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
 * File: Pathfinder.java
 * Type: logic.pathfinding.Pathfinder
 * 
 * Documentation created: 16.05.2012 - 17:43:55 by Hady Khalifa
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic.pathfinding;

import logic.Grid;

/**
 *
 * @author Hady Khalifa
 */
public class Pathfinder {
    
    private Grid grid = Grid.getInstance();
    
    private Pathfinder() {
        init();
    }
    
    public static Pathfinder getInstance() {
        return PathfinderHolder.INSTANCE;
    }

    private static class PathfinderHolder {

        private static final Pathfinder INSTANCE = new Pathfinder();
    }
    
        private void init() {
            
            
            /*
             * todo
             * 
             */
    }
        
        public void generateMainPath(){
            //Pathfindingalgorithmus viel Spass HAdy
        }
    
}
