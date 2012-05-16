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
 * File: Grid.java
 * Type: logic.Grid
 * 
 * Documentation created: 16.05.2012 - 17:00:21 by Hady Khalifa
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

/**
 *
 * Class Grid
 * The logical respresentation of the Field
 * 
 * @author Hady Khalifa
 */
public class Grid {

    /**
     * private data fields
     */
    private FieldInfo[][] grid;

    /**
     * Constructor
     */
    private Grid() {
        init(21, 21);
    }

    /**
     * 
     * @return GridHolder.INSTANCE
     */
    public static Grid getInstance() {
        return GridHolder.INSTANCE;
    }

    /**
     * innerClass gridHolder
     * use for Singelton-Pattern
     */
    private static class GridHolder {

        private static final Grid INSTANCE = new Grid();
    }

    /**
     * 
     * init method
     * 
     * @param width - the width of the field
     * @param height - the height of the field
     */
    private void init(int width, int height) {
        grid = new FieldInfo[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[x][y] = new FieldInfo(x, y, 0);
            }
        }
    }
    /**
     * 
     * @param xCoord
     * @param yCoord
     * @return 
     */
    FieldInfo getFieldInfo(int xCoord, int yCoord){
        return grid[xCoord][yCoord];
    } 
    
    /**
     * 
     * @param xCoord
     * @param yCoord
     * @param value 
     */
    void setTowerValue(int xCoord, int yCoord, int value){
        grid[xCoord][yCoord].setTowerValue(value);
    }

    /**
     * innerClass FieldInfo
     */
    private class FieldInfo {

        /**
         * private Data Fields
         */
        int xCoord;
        int yCoord;
        int towerValue;

        /**
         * FieldInfo Constructor
         * 
         * @param xCoord - used for ID
         * @param yCoord - used for ID
         * @param towerValue 0: no tower on the field; 1: tower on the field
         */
        private FieldInfo(int xCoord, int yCoord, int towerValue) {
            this.xCoord = xCoord;
            this.yCoord = yCoord;
            this.towerValue = towerValue;

        }

        /**
         * setter - TowerValue
         * 
         * @param value 
         */
        private void setTowerValue(int value) {
         this.towerValue = value;
        }
        
        
    }
}
