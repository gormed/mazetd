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

import entities.Map.MapSquare;

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
    private static FieldInfo[][] grid;
    private static FieldInfo startField, endField;
    private int totalHeight;
    private int totalWidth;

    /**
     * Constructor
     */
    private Grid() {
        totalHeight = 21;
        totalWidth = 21;
        initGrid(totalWidth, totalHeight);
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
    private void initGrid(int width, int height) {
        grid = new FieldInfo[width][height];

        //GoalField
        //grid[width - 1][height / 2] = new FieldInfo(width - 1, height / 2, 10);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new FieldInfo(x, y, (10 + (Math.abs(x - (width - 1)) * 10) + (Math.abs(y - (height / 2)) * 10)));           
            }
        }
        //Startfield
        grid[0][height / 2].setWeight(0);
        
        //Test Tower
        setTower(2, 10);
        setTower(12, 11);
    }

    public int getTotalWidth() {
        return totalWidth;
    }

    public int getTotalHeight() {
        return totalHeight;
    }

    /**
     * 
     * @param xCoord
     * @param yCoord
     * @return 
     */
    public FieldInfo getFieldInfo(int xCoord, int yCoord) {
        return grid[xCoord][yCoord];
    }

    /**
     * 
     * @param xCoord
     * @param yCoord 
     */
    void setTower(int xCoord, int yCoord) {
        grid[xCoord][yCoord].incrementWeight(10000);
        grid[xCoord + 1][yCoord].incrementWeight(10000);
        grid[xCoord][yCoord + 1].incrementWeight(10000);
        grid[xCoord + 1][yCoord + 1].incrementWeight(10000);
    }

    /**
     * 
     * @param xCoord
     * @param yCoord 
     */
    void removeTower(int xCoord, int yCoord) {
        grid[xCoord][yCoord].decrementWeight(10000);
        grid[xCoord + 1][yCoord].decrementWeight(10000);
        grid[xCoord][yCoord + 1].decrementWeight(10000);
        grid[xCoord + 1][yCoord + 1].decrementWeight(10000);
    }

    /**
     * innerClass FieldInfo
     */
    public class FieldInfo {

        /**
         * private Data Fields
         */
        private int xCoord;
        private int yCoord;
        private int weight;
        private MapSquare square;
        private FieldInfo parent;

        /**
         * FieldInfo Constructor
         * 
         * @param xCoord - used for ID
         * @param yCoord - used for ID
         * @param towerID - null: no tower on the field
         */
        private FieldInfo(int xCoord, int yCoord, int weight) {
            this.xCoord = xCoord;
            this.yCoord = yCoord;
            this.weight = weight;
            this.parent = null;


        }

        public void setParent(FieldInfo field) {
            this.parent = field;
        }

        /**
         * setter - TowerValue
         * 
         * @param value 
         */
        private void incrementWeight(int weight) {
            this.weight += weight;
        }

        private void decrementWeight(int weight) {
            this.weight -= weight;
        }

        @Override
        public String toString() {
            super.toString();
            String output = "Field(" + xCoord + ", " + yCoord + ") - weight: " + weight;
            return output;

        }

        public void setMapSquare(MapSquare m) {
            this.square = m;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }

        public int getXCoord() {
            return xCoord;
        }

        public int getYCoord() {
            return yCoord;
        }

        public FieldInfo getParent() {
            return parent;
        }

        public MapSquare getSquare() {
            return square;
        }
        
    }
}
