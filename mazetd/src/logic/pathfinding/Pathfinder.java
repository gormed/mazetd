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
 * @author Hady Khalifa
 */
public class Pathfinder {

    public static boolean DEBUG_PATH = true;
    public static int TOWER_WEIGHT = 10000;
    public static int NORMAL_WEIGHT = 1;
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * Constructor Pathfinder
     */
    private Pathfinder() {
    }

    /**
     * 
     * @return 
     */
    public static Pathfinder getInstance() {
        return PathfinderHolder.INSTANCE;
    }

    /**
     * 
     */
    private static class PathfinderHolder {

        private static final Pathfinder INSTANCE = new Pathfinder();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Queue<MapSquare> path;
    private Queue<MapSquare> lastPath;
    private Grid grid = Grid.getInstance();
    private boolean gridChanged = false;
    private MapSquare changedSquare;
    private int changedWeight;
    private Point2i start = new Point2i(0, Grid.getInstance().getTotalHeight() / 2);
    private Point2i end = new Point2i(Grid.getInstance().getTotalWidth() - 1, Grid.getInstance().getTotalHeight() / 2);

    //==========================================================================
    //===   Methods
    //==========================================================================
    public void initialize() {
        setMainPath(createMainPath());
        lastPath = path;
    }

    public void update(float tpf) {
        if (gridChanged) {
            changedSquare.getFieldInfo().incrementWeight(changedWeight);
            if (getMainPath().contains(changedSquare)) {
                lastPath = path;
                path = createMainPath();
                gridChanged = false;

//                Future fut = MazeTDGame.getInstance().enqueue(new Callable() {
//
//                    @Override
//                    public Object call()
//                            throws Exception {
//
//                        return createMainPath();
//                    }
//                });
//                try {
//                    setMainPath((Queue<MapSquare>) fut.get());
//                    gridChanged = false;
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Pathfinder.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (ExecutionException ex) {
//                    Logger.getLogger(Pathfinder.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }

            setMainPath(createMainPath());
            gridChanged = false;
//            if (DEBUG_PATH) {
//                for (MapSquare ms : path) {
//                    ms.setMainPathDebug(true);
//                }
//            }
//            Future fut = MazeTDGame.getInstance().enqueue(new Callable() {
//
//                @Override
//                public Object call()
//                        throws Exception {
//
//                    return createMainPath();
//                }
//            });
//            try {
//                setMainPath((Queue<MapSquare>) fut.get());
//                gridChanged = false;
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Pathfinder.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (ExecutionException ex) {
//                Logger.getLogger(Pathfinder.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }

    void setChangedMapSquare(MapSquare square, int newWight) {
        changedSquare = square;
        gridChanged = true;
        changedWeight = newWight;
    }

    public void setMainPath(Queue<MapSquare> path) {
        this.path = path;
    }

    public Queue<MapSquare> getMainPath() {
        return new LinkedList<MapSquare>(path);
    }

    public Queue<MapSquare> getLastPath() {
        return lastPath;
    }

    public FieldInfo getStartField() {
        return grid.getFieldInfo(start.x, start.y);
    }

    public FieldInfo getEndField() {
        return grid.getFieldInfo(end.x, end.y);
    }

    /**
     * 
     *      
     * Ermittelt den Kürzesten Pfad zwischen dem Start- und dem Ziel-Feld;
     * 
     * @return  Queue aus MapSquares in der Richtigen Reihenfolge zum begehen
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


        Collections.reverse((LinkedList) tempPath);

        return tempPath;
    }

    /**
     * 
     * Ermittelt den Kürzesten Pfad zwischen 2 Feldern;
     * 
     * @param creepPos Field
     * @return 
     */
    public Queue<MapSquare> createCreepPath(FieldInfo creepPos, FieldInfo goal) {
        Queue<MapSquare> tempPath = new LinkedList<MapSquare>();
        FieldInfo field = null;
        try {
            field = findPath(grid, creepPos, goal);
        } catch (RuntimeException e) {
            System.out.println("fehler Pathfinder-> findPath()");
        }

//        while (field != null && !field.getParent().equals(start)) {
        while (field != null) {
            tempPath.add(field.getSquare());
            field = field.getParent();
            if (field.equals(creepPos)) {
                break;
            }
        }
//        if (DEBUG_PATH) {
//            for (MapSquare ms : tempPath) {
//                ms.setCreepPathDebug(true);
//            }
//        }
        Collections.reverse((LinkedList) tempPath);

        return tempPath;
    }

    /**
     * Modified A*-Algorithm
     * 
     * Ermittelt  den kuerzesten Pfad zwischen zwei übergebenen Feldern
     * Wird intern genutzt
     * 
     * @param grid
     * @param start Field
     * @param goal Field
     * @return Feld - der Pfad kann von diesem Feld aus seinen Vorgaengern erstellt werden
     */
    private FieldInfo findPath(Grid grid, FieldInfo start, FieldInfo goal) {
        ArrayList<FieldInfo> openList = new ArrayList<FieldInfo>();
        ArrayList<FieldInfo> closedList = new ArrayList<FieldInfo>();

        FieldInfo u = grid.getFieldInfo(start.getXCoord(), start.getYCoord());
        openList.add(u);
        while (!openList.isEmpty()) {  //...und los
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
     * Used to optimize findPath() -> find the shortest Path
     * 
     * @param field current FieldInfo in openList
     * @return distance between field and startField
     */
    private static int calcG(FieldInfo field) 
    {
        FieldInfo f = field.getParent();  
        return f.getG() + 1;
    }

    /**
     * Gibt das Field mit dem geringsten Gewicht aus einer ArrayList zurueck
     * 
     * @param l Liste aus FieldInfo 
     * @return 
     */
    private static FieldInfo getLeastWeight(ArrayList<FieldInfo> l) //Field aus open-/closedList mit niedrigster Weight suchen
    {
        FieldInfo least = null;
        for (FieldInfo f : l) {
            if ((least == null) || (f.getWeight()+f.getG() < least.getWeight()+least.getG())) {
                least = f;
            }
        }
        return least;
    }

    /**
     * 
     * 
     * @param f
     * @param l
     * @return 
     */
    private static boolean betterIn(FieldInfo f, ArrayList<FieldInfo> l) //Umweg gegangen?
    {
        for (FieldInfo field : l) {
            if (field.getXCoord() == f.getXCoord() && field.getYCoord() == f.getYCoord() && field.getWeight()+field.getG() <= f.getWeight()+f.getG()) {
                return true;
            }
        }
        return false;
    }
}
