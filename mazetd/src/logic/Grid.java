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

//Imports
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
    public static final int GRID_HEIGHT = 9;
    public static final int GRID_WIDTH = 27;
    private static FieldInfo[][] grid;
    private static FieldInfo startField, endField;
    private int totalHeight;
    private int totalWidth;

    /**
     * Constructor
     */
    private Grid() {
        totalHeight = GRID_HEIGHT;
        totalWidth = GRID_WIDTH;
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
     * initialize the Grid singelton
     */
    public void initialize() {
        totalHeight = GRID_HEIGHT;
        totalWidth = GRID_WIDTH;
        initGrid(totalWidth, totalHeight);
    }

    /**
     * 
     * init method of the grid
     * create an 2-dimensional Array of FieldInfo
     * initialize the weigth of every fieldInfo
     * 
     * 
     * @param width - the width of the field
     * @param height - the height of the field
     */
    private void initGrid(int width, int height) {
        grid = new FieldInfo[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new FieldInfo(x, y, (10 + (Math.abs(x - (width - 1)) * 10) + (Math.abs(y - (height / 2)) * 10)));
            }
        }
        //Startfield
        startField = grid[0][height / 2];
        startField.setWeight(0);
        //Endfield
        endField = grid[width - 1][height / 2];
    }

    /**
     * get the endField of the grid
     * @return endfield FieldInfo
     */
    public static FieldInfo getEndField() {
        return endField;
    }

    /**
     * get the startField of the grid
     * @return startfield FieldInfo
     */
    public static FieldInfo getStartField() {
        return startField;
    }

    /**
     * 
     * get the total height of the grid
     * @return totalWidth
     */
    public int getTotalWidth() {
        return totalWidth;
    }

    /**
     * get the total height of the grid
     * @return totalHeight
     */
    public int getTotalHeight() {
        return totalHeight;
    }

    /**
     * Get the fieldInfo by coords
     * used for create paths
     * @param xCoord of the fieldinfo
     * @param yCoord of the fieldinfo 
     * @return FieldInfo
     */
    public FieldInfo getFieldInfo(int xCoord, int yCoord) {
        return grid[xCoord][yCoord];
    }

    /**
     * set a new weigth to fieldinfo
     * 
     * @param xCoord of the fieldinfo
     * @param yCoord of the fieldinfo 
     */
    void setTower(int xCoord, int yCoord) {
        grid[xCoord][yCoord].setWeight(Pathfinder.TOWER_WEIGHT);
    }

    /**
     * innerClass FieldInfo
     * logical represantation of the mapsquare
     */
    public class FieldInfo {

        /**
         * private Data Fields
         */
        private int g;
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
         * @param weigth - used for create paths
         */
        private FieldInfo(int xCoord, int yCoord, int weight) {
            this.xCoord = xCoord;
            this.yCoord = yCoord;
            this.weight = weight;
            this.parent = null;
        }

        /**
         * set G (distance between startfield an this)
         * @param g 
         */
        public void setG(int g) {
            this.g = g;
        }

        /**
         * get G (distance between startfield an this)
         * @return int
         */
        public int getG() {
            return g;
        }

        /**
         * Set parent
         * @param field FieldInfo, used for create paths
         */
        public void setParent(FieldInfo field) {
            this.parent = field;
        }

        /**
         * Setter
         * change value of weigth
         * used by build towers
         * @param value 
         */
        public void incrementWeight(int weight) {
            this.weight += weight;
        }

        /**
         * setter
         * change value of weigth
         * used by removing towers
         * @param weight 
         */
        public void decrementWeight(int weight) {
            this.weight -= weight;
        }

        /**
         * Used for testing
         * @return String for System.out.print
         */
        @Override
        public String toString() {
            super.toString();
            String output = "Field(" + xCoord + ", " + yCoord + ") - weight: " + weight;
            return output;
        }

        /**
         * related MapSquare(visual, geographic) to the Fieldinfo(logical, numerical)
         * @param m MapSquare
         */
        public void setMapSquare(MapSquare m) {
            this.square = m;
        }

        /**
         * set the weigth
         * used for incremend / decremend weigth when building towers
         * @param weight 
         */
        public void setWeight(int weight) {
            this.weight = weight;
        }

        /**
         * get the Weight of the FieldInfo; distance to the endField;
         * calculated initial; used for create paths
         * @return weigth int 
         */
        public int getWeight() {
            return weight;
        }

        /**
         * get XCoord in logical grid
         * @return int XCoord
         */
        public int getXCoord() {
            return xCoord;
        }

        /**
         * get YCoord in logical grid
         * @return int XCoord
         */
        public int getYCoord() {
            return yCoord;
        }

        /**
         * get Parent
         * @return Parent FieldInfo, used for create paths
         */
        public FieldInfo getParent() {
            return parent;
        }

        /**
         * get Square
         * @return MapSquare related to the FIeldInfo
         */
        public MapSquare getSquare() {
            return square;
        }
    }
}
