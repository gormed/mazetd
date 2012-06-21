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

//Imports
import entities.Map.MapSquare;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import javax.vecmath.Point2i;
import logic.Grid;
import logic.Grid.FieldInfo;

/**
 *
 * The pathfinder is used by the creepAI and build/remove Tower events to generate 
 * the shortest path from a start point to an end point.
 * 
 * - Everytime a tower has been build on the current mainpath or a tower has been removed
 * the methode createMainPath() is called -> new MainPath will be created Queue<MapSquare>
 * 
 *  * - Everytime a new mainPath has been created, every creep checks if he is on the mainpath
 *          - If he is on the mainpath(mainpath contains creeps current position) the creep  
 *              use generated mainpath
 *          - else the creep uses an new creted path from the creeps position to the goal
 *              @see{ createCreepPath(FieldInfo creepPos, FieldInfo goal)}
 * 
 * @author Hady Khalifa
 */
public class Pathfinder {

    /** The DEBU g_ path. */
    public static boolean DEBUG_PATH = true;
    
    /** The TOWE r_ weight. */
    public static int TOWER_WEIGHT = 10000;
    
    /** The STON e_ weight. */
    public static int STONE_WEIGHT = 50000;
    
    /** The NORMA l_ weight. */
    public static int NORMAL_WEIGHT = 1000;
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * Constructor Pathfinder.
     */
    private Pathfinder() {
    }

    /**
     * Singelton.
     *
     * @return an Instance of tehe class
     */
    public static Pathfinder getInstance() {
        return PathfinderHolder.INSTANCE;
    }

    /**
     * Singelton.
     */
    private static class PathfinderHolder {
        
        /** The Constant INSTANCE. */
        private static final Pathfinder INSTANCE = new Pathfinder();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /** The path. */
    private Queue<MapSquare> path;
    
    /** The last path. */
    private Queue<MapSquare> lastPath;
    
    /** The grid. */
    private Grid grid = Grid.getInstance();
    
    /** The grid changed. */
    private boolean gridChanged = false;
    
    /** The changed square. */
    private MapSquare changedSquare;
    
    /** The changed weight. */
    private int changedWeight;
    
    /** The start. */
    private Point2i start = new Point2i(0, Grid.getInstance().getTotalHeight() / 2);
    
    /** The end. */
    private Point2i end = new Point2i(Grid.getInstance().getTotalWidth() - 1, Grid.getInstance().getTotalHeight() / 2);

    //==========================================================================
    //===   Methods
    //==========================================================================
    
    /**
     * initialize Singelton.
     */
    public void initialize() {
        setMainPath(createMainPath());
        lastPath = path;
    }

    /**
     * update.
     *
     * @param tpf the tpf
     */
    public void update(float tpf) {
        if (gridChanged) {
            changedSquare.getFieldInfo().incrementWeight(changedWeight);
            if (getMainPath().contains(changedSquare)) {
                lastPath = path;
                path = createMainPath();
                gridChanged = false;
            }
            setMainPath(createMainPath());
            gridChanged = false;
        }
    }

    /**
     * set the MapSquare which changed by buildingTower or removingTower.
     *
     * @param square MapSquare changed Square
     * @param newWeight int , increase by 10000 for build a tower
     */
    void setChangedMapSquare(MapSquare square, int newWeight) {
        changedSquare = square;
        gridChanged = true;
        changedWeight = newWeight;
    }

    /**
     * set new mainpath.
     *
     * @param path the new main path
     */
    public void setMainPath(Queue<MapSquare> path) {
        this.path = path;
    }

     /**
      * gets the current mainPath.
      *
      * @return Queue<MapSquare> mainpath
      */
    public Queue<MapSquare> getMainPath() {
        return new LinkedList<MapSquare>(path);
    }

    /**
     * gets the previous mainPath for checking creeps are on the old path.
     *
     * @return Queue<MapSquare> lastPath
     */
    public Queue<MapSquare> getLastPath() {
        return lastPath;
    }

    /**
     * get the StartField.
     *
     * @return FieldInfo StartField
     */
    public FieldInfo getStartField() {
        return grid.getFieldInfo(start.x, start.y);
    }

    /**
     * get the EndField.
     *
     * @return FieldInfo EndField
     */
    public FieldInfo getEndField() {
        return grid.getFieldInfo(end.x, end.y);
    }

    /**
     * 
     * Uses the mthode findPath(grid,start,goal) to get the FieldInfo from where
     * this methode create the concrete Path by adding the mapSquare of every parent 
     * to a Queue.
     * This Queue will be reversed to use ist in the right direction
     * 
     * @return the mainPath, a Queue of MapSquares  from the start to the goal
     */
    public Queue<MapSquare> createMainPath() {
        Queue<MapSquare> tempPath = new LinkedList<MapSquare>();
        FieldInfo field = null;
        try {
            field = findPath(grid, getStartField(), getEndField());
        } catch (RuntimeException e) {
            System.out.println("fehler Pathfinder-> findPath()");
        }
        MapSquare s;
        while (!field.getParent().equals(getStartField())) {
            s = field.getSquare();

            tempPath.add(s);
            field = field.getParent();
        }
        //Reverse the List
        Collections.reverse((LinkedList) tempPath);

        return tempPath;
    }

    /**
     * Uses the mthode findPath(grid,position of the creep ,goal) to get the FieldInfo from where
     * this methode create the concrete Path by adding the mapSquare of every parent
     * to a Queue.
     * This Queue will be reversed to use ist in the right direction
     *
     * @param creepPos FieldInfo of the Mapsquare with this Creep on it
     * @param goal the goal
     * @return a Queue of MapSquares as a Path from creeps position to the goal
     */
    public Queue<MapSquare> createCreepPath(FieldInfo creepPos, FieldInfo goal) {
        Queue<MapSquare> tempPath = new LinkedList<MapSquare>();
        FieldInfo field = null;
        try {
            field = findPath(grid, creepPos, goal);
        } catch (RuntimeException e) {
            System.out.println("fehler Pathfinder-> findPath()");
        }


        while (field != null) {
            tempPath.add(field.getSquare());
            field = field.getParent();
            if (field.equals(creepPos)) {
                break;
            }
        }
        //Reverse the List
        Collections.reverse((LinkedList) tempPath);

        return tempPath;
    }

    /**
     * Modified A*-Algorithm
     * 
     * 1. Add the starting square (or node) to the open list.
     * 2. Repeat the following:
     * 2.1 Look for the lowest F cost square on the open list. We refer to this as the current square.
     * 2.2 Switch it to the closed list.
     * 2.3 For each of the 4 field adjacent to this current square …
     * - If it is not walkable or if it is on the closed list, ignore it. Otherwise do the following.           
     * - If it isn’t on the open list, add it to the open list. Make the current square the parent of this square. Record the G costs of the square. 
     * - If it is on the open list already, check to see if this path to that square is better(low G-Cost) @see{betterIn(FieldInfo f, ArrayList<FieldInfo> l)}
     * - If so, change the parent of the square to the current square.
     * 3. Stop when you:  
     * - Add the target square to the closed list, in which case the path has been found, or
     * - Fail to find the target square, and the open list is empty. In this case, there is no path.   
     * 
     * @param grid logical respresentation of the map
     * @param start FieldInfo is starting point 
     * @param goal FieldInfo is the end point
     * @return FieldInfo == goal - the path can be created from this field and there parents @see{createMainPath()}
     */
    private FieldInfo findPath(Grid grid, FieldInfo start, FieldInfo goal) {
        ArrayList<FieldInfo> openList = new ArrayList<FieldInfo>();
        ArrayList<FieldInfo> closedList = new ArrayList<FieldInfo>();

        FieldInfo u = grid.getFieldInfo(start.getXCoord(), start.getYCoord());
        openList.add(u);
        while (!openList.isEmpty()) {
            FieldInfo q = getLeastWeight(openList);  //Field aus openList mit besten Aussichten ist q
            openList.remove(q);  //Dieser wird aus der openList entfernt, um ihn nicht noch einmal zu benutzen
            ArrayList<FieldInfo> successors = new ArrayList<FieldInfo>();
            int qx = q.getXCoord();
            int qy = q.getYCoord();
            try {
                //Richtung 1
                FieldInfo fi = grid.getFieldInfo(qx - 1, qy);
                if (!closedList.contains(fi)) {
                    fi.setParent(q);
                    fi.setG(calcG(fi));
                    successors.add(fi);
                }
            } catch (ArrayIndexOutOfBoundsException aie) {
            }
            try {
                //Richtung 3
                FieldInfo fi = grid.getFieldInfo(qx + 1, qy);
                if (!closedList.contains(fi)) {
                    fi.setParent(q);
                    fi.setG(calcG(fi));
                    successors.add(fi);
                }

            } catch (ArrayIndexOutOfBoundsException aie) {
            }
            try {
                //Richtung 2
                FieldInfo fi = grid.getFieldInfo(qx, qy - 1);
                if (!closedList.contains(fi)) {
                    fi.setParent(q);
                    fi.setG(calcG(fi));
                    successors.add(fi);
                }
            } catch (ArrayIndexOutOfBoundsException aie) {
            }
            try {
                //Richtung 4
                FieldInfo fi = grid.getFieldInfo(qx, qy + 1);
                if (!closedList.contains(fi)) {
                    fi.setParent(q);
                    fi.setG(calcG(fi));
                    successors.add(fi);
                }
            } catch (ArrayIndexOutOfBoundsException aie) {
            }
            for (FieldInfo f : successors) {  //für jede Gehmöglichkeit
                if (f.getXCoord() == goal.getXCoord() && f.getYCoord() == goal.getYCoord()) //wenn Ziel
                {
                    return f;  //FOUND SHORTEST PATH !!!
                }
                boolean add = true;  //wenn es kein besserer Feld in der
                if (betterIn(f, openList) || betterIn(f, closedList)) //openList und der closedList
                {
                    add = false;
                }
                if (add) {
                    openList.add(f);  //gibt, successor zur openList hinzufügen
                }
            }
            closedList.add(q);  //Schleife beendet, q zur closedList tun
        }
        //Schleife beendet->kein Weg gefunden
        throw new RuntimeException("keinen Weg gefunden");
    }
    
    /**
     * Calculate the distance between the given field and the start field
     * Used to optimize findPath() -> find the shortest Path.
     *
     * @param field current FieldInfo in openList
     * @return distance between field and startField
     */
    private static int calcG(FieldInfo field) {
        FieldInfo f = field.getParent();
        return f.getG() + 1;
    }

    /**
     * Returns the FieldInfo with the lowest Weigth in a ArrayList.
     *
     * @param l ArrayList<FieldInfo> , in find path it's the openList
     * @return FieldInfo with the lowest weight in an ArrayList<FieldInfo>
     */
    private static FieldInfo getLeastWeight(ArrayList<FieldInfo> l) //Field aus open-/closedList mit niedrigster Weight suchen
    {
        FieldInfo least = null;
        for (FieldInfo f : l) {
            if ((least == null) || (f.getWeight() + f.getG() < least.getWeight() + least.getG())) {
                least = f;
            }
        }
        return least;
    }

    /**
     * check to see if this path to that square is better (lower Costs) than anoth in the openList.
     *
     * @param f compared FieldInfo
     * @param l List to Compare
     * @return an boolean value if there is an shorter PAth to the FieldInfo f in the openList or not
     */
    private static boolean betterIn(FieldInfo f, ArrayList<FieldInfo> l) //Umweg gegangen?
    {
        for (FieldInfo field : l) {
            if (field.getXCoord() == f.getXCoord() && field.getYCoord() == f.getYCoord() && field.getWeight() + field.getG() <= f.getWeight() + f.getG()) {
                return true;
            }
        }
        return false;
    }
}
