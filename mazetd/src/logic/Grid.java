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
import logic.pathfinding.Pathfinder;

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
        totalHeight = 9;
        totalWidth = 27;
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
      /*  setTower(2,0);
        setTower(2, 1);
        setTower(2, 2);
        setTower(2, 3);
        setTower(2, 4);
        setTower(2, 5);
        setTower(2, 6);
        setTower(2, 7);
        setTower(2, 8);
        setTower(2, 9);
        setTower(2, 10);
        setTower(2, 11);
        setTower(2, 12);
        setTower(2, 13);
        setTower(2, 14);
        setTower(2, 15);
        setTower(2, 16);
        setTower(2, 17);
        setTower(2, 18);
        setTower(2, 19);
        setTower(2, 20);

        setTower(4, 0);
        setTower(4, 1);
        setTower(4, 2);
        setTower(4, 3);
        setTower(4, 4);
        setTower(4, 5);
        setTower(4, 6);
        setTower(4, 7);
        setTower(4, 8);
        setTower(4, 9);
        setTower(4, 10);
        setTower(4, 11);
        setTower(4, 12);
        setTower(4, 13);
        setTower(4, 14);
        setTower(4, 15);
        setTower(4, 16);
        setTower(4, 17);
        setTower(4, 18);
        setTower(4, 19);
*/


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
        grid[xCoord][yCoord].setWeight(Pathfinder.TOWER_WEIGHT);
        //grid[xCoord][yCoord].getSquare().setColor(ColorRGBA.Red.clone());

    }

    /**
     * 
     * @param xCoord
     * @param yCoord 
     */
    void removeTower(int xCoord, int yCoord) {
        grid[xCoord][yCoord].decrementWeight(Pathfinder.TOWER_WEIGHT);

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
        public  void incrementWeight(int weight) {
            this.weight += weight;
        }

        public void decrementWeight(int weight) {
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
