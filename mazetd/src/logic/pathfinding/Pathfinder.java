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
import logic.Grid;
import logic.Grid.FieldInfo;

/**
 *
 * @author Hady Khalifa
 */
public class Pathfinder {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Queue<MapSquare> path;
    private Grid grid = Grid.getInstance();

    /**
     * 
     */
    private Pathfinder() {
        init();
    }

    /**
     * 
     * @return 
     */
    public static Pathfinder getInstance() {
        return PathfinderHolder.INSTANCE;
    }

    private void init() {
        path = createMainPath();
    }

    /**
     * 
     */
    private static class PathfinderHolder {

        private static final Pathfinder INSTANCE = new Pathfinder();
    }

    public Queue<MapSquare> getMainPath(){
        return path;
    }
    
    /**
     * TODO -> Parameter start! , ziel?
     * 
     * @return 
     */
    public Queue<MapSquare> createMainPath() {
        Queue<MapSquare> tempPath = new LinkedList();
        FieldInfo field = null;
        try {
            field = findPath(grid,grid.getFieldInfo(0, 10),grid.getFieldInfo(20, 10));
        } catch (RuntimeException e) {
            System.out.println("fehler Pathfinder-> findPath()");
        }

        while (field.getParent() != null) {
            tempPath.add(field.getSquare());
            field = field.getParent();
        }


        Collections.reverse((LinkedList)tempPath);

        return tempPath;
    }

    /**
     * Modified A*-Algorithmus
     * 
     * @param grid
     * @return 
     */
    public FieldInfo findPath(Grid grid, FieldInfo start, FieldInfo goal) {
        ArrayList<FieldInfo> openList = new ArrayList();
        ArrayList<FieldInfo> closedList = new ArrayList();

        FieldInfo u = grid.getFieldInfo(start.getXCoord(), start.getYCoord());
        openList.add(u);
        while (!openList.isEmpty()) {  //...und los
            FieldInfo q = getLeastWeight(openList);  //Field aus openList mit besten Aussichten ist q
            openList.remove(q);  //Dieser wird aus der openList entfernt, um ihn nicht noch einmal zu benutzen
            ArrayList<FieldInfo> successors = new ArrayList();
            int qx = q.getXCoord();
            int qy = q.getYCoord();
            try {
                //Richtung 1
                FieldInfo fi = grid.getFieldInfo(qx - 1, qy);
                if (!closedList.contains(fi)) {
                    fi.setParent(q);
                    successors.add(fi);
                }

            } catch (ArrayIndexOutOfBoundsException aie) {
            }

            try {
                //Richtung 2
                FieldInfo fi = grid.getFieldInfo(qx, qy - 1);
                if (!closedList.contains(fi)) {
                    fi.setParent(q);
                    successors.add(fi);
                }
            } catch (ArrayIndexOutOfBoundsException aie) {
            }

            try {
                //Richtung 3
                FieldInfo fi = grid.getFieldInfo(qx + 1, qy);
                if (!closedList.contains(fi)) {
                    fi.setParent(q);
                    successors.add(fi);
                }

            } catch (ArrayIndexOutOfBoundsException aie) {
            }
            try {
                //Richtung 4
                FieldInfo fi = grid.getFieldInfo(qx, qy + 1);
                if (!closedList.contains(fi)) {
                    fi.setParent(q);
                    successors.add(fi);
                }
            } catch (ArrayIndexOutOfBoundsException aie) {
            }

            for (FieldInfo f : successors) {  //für jede Gehmöglichkeit
                if (f.getXCoord() == goal.getXCoord() && f.getYCoord() == goal.getYCoord()) //wenn Ziel
                {
                    return f;  //FERTIG!!!:)
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
     * 
     * @param l
     * @return 
     */
    private static FieldInfo getLeastWeight(ArrayList<FieldInfo> l) //Field aus open-/closedList mit niedrigster Weight suchen
    {
        FieldInfo least = null;
        for (FieldInfo f : l) {
            if ((least == null) || (f.getWeight() < least.getWeight())) {
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
            if (field.getXCoord() == f.getXCoord() && field.getYCoord() == f.getYCoord() && field.getWeight() <= f.getWeight()) {
                return true;
            }
        }
        return false;
    }
}
