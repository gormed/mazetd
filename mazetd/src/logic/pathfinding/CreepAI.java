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
 * File: CreepAI.java
 * Type: logic.pathfinding.CreepAI
 * 
 * Documentation created: 02.06.2012 - 14:05:06 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic.pathfinding;

import entities.Creep;
import entities.Map;
import entities.Map.MapSquare;
import entities.base.EntityManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Hans Ferchland
 */
public class CreepAI {
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of CreepAI.
     */
    private CreepAI() {
//        currentMainPath = new LinkedList<MapSquare>(pathfinder.getMainPath());
    }

    /**
     * The static method to retrive the one and only instance of CreepAI.
     */
    public static CreepAI getInstance() {
        return CreepAIHolder.INSTANCE;
    }

    /**
     * The holder-class CreepAIHolder for the CreepAI.
     */
    private static class CreepAIHolder {

        private static final CreepAI INSTANCE = new CreepAI();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private EntityManager entityManager = EntityManager.getInstance();
    private Pathfinder pathfinder = Pathfinder.getInstance();
    private MapSquare changedSquare = null;
    private boolean changed = false;
    private boolean someReverse = false;
    private boolean towerAdded = false;
    private boolean towerRemoved = false;
    private int changedWeight = 1;
//    private Queue<MapSquare> currentMainPath;
    //==========================================================================
    //===   Methods
    //==========================================================================

    public void update(float tpf) {
        if (changed) {
            updateCreepPathes(tpf, changedSquare, changedWeight);
            changed = false;
        }
        if (someReverse) {
            updateCreepPathesReverse(tpf);

        } 

    }

    private void updateCreepPathesReverse(float tpf) {

        checkCreepsReverse(entityManager.getCreepHashMap(),
                pathfinder.getMainPath());

        //currentMainPath = new LinkedList<MapSquare>(pathfinder.getMainPath());
    }

    private void updateCreepPathes(float tpf, MapSquare square, int newWeight) {

        //Liegt der Turm auf dem aktuellen MainPath?
        if (pathfinder.getLastPath().contains(square) && towerAdded) {
            //NeuerPfad wird generiert
            checkCreeps(entityManager.getCreepHashMap(),
                    pathfinder.getMainPath());
            towerAdded = false;
        } else if (towerRemoved) {
            checkCreeps(entityManager.getCreepHashMap(),
                    pathfinder.getMainPath());
            towerRemoved = false;
        }
        //currentMainPath = new LinkedList<MapSquare>(pathfinder.getMainPath());
    }

    private void checkCreepsReverse(
            HashMap<Integer, Creep> creeps,
            Queue<MapSquare> mainPath) {
        someReverse = false;
        for (Creep creep : creeps.values()) {
            if (creep.isReverse()) {
                Queue<MapSquare> path = new LinkedList<MapSquare>(mainPath);
                //Ist der Creep auf dem MainPath?
                if (mainPath.contains(creep.getCurrentSquare())) {
                    Collections.reverse((LinkedList) path);
                    //Ziehe alle Squares aus der Queue bis das richtige am anfang steht
                    while (!path.poll().equals(creep.getCurrentSquare())) {
                    }

                    creep.setPath(path);
                } else {
                    Queue<MapSquare> uniquePath =
                            pathfinder.createCreepPath(
                            creep.getCurrentSquare().getFieldInfo(), pathfinder.getStartField());
                    creep.setPath(uniquePath);
                }
                someReverse = true;
            }

        }
    }

    
    /**
     * 
     * @param creep 
     */
    public void createUniqueCreepPath(Creep creep){
        Queue<MapSquare> mainPath = pathfinder.getMainPath();
                    Queue<MapSquare> path = new LinkedList<MapSquare>(mainPath);
            //Ist der Creep auf dem MainPath?
            if (mainPath.contains(creep.getCurrentSquare())) {
                //Ziehe alle Squares aus der Queue bis das richtige am anfang steht
                while (!path.poll().equals(creep.getCurrentSquare())) {
                }

                creep.setPath(path);
            } else {
                Queue<MapSquare> uniquePath =
                        pathfinder.createCreepPath(
                        creep.getCurrentSquare().getFieldInfo(), pathfinder.getEndField());
                creep.setPath(uniquePath);
            }
    }
    
    /**
     * Ermittelt für alle Creeps den neuen Path
     * - wenn der Creep auf dem MainPath ist wird der MainPath ab der Creepposition übergeben
     * - sonst wird von seiner Position zum Ziel ein eigener Pfad erstellt
     * 
     * @param alle creeps
     * @param mainPath 
     */
    private void checkCreeps(
            HashMap<Integer, Creep> creeps,
            Queue<MapSquare> mainPath) {

        for (Creep creep : creeps.values()) {
            Queue<MapSquare> path = new LinkedList<MapSquare>(mainPath);
            //Ist der Creep auf dem MainPath?
            if (mainPath.contains(creep.getCurrentSquare())) {
                //Ziehe alle Squares aus der Queue bis das richtige am anfang steht
                while (!path.poll().equals(creep.getCurrentSquare())) {
                }

                creep.setPath(path);
            } else {
                Queue<MapSquare> uniquePath =
                        pathfinder.createCreepPath(
                        creep.getCurrentSquare().getFieldInfo(), pathfinder.getEndField());
                creep.setPath(uniquePath);
            }


        }
    }

    public void setChangeMapSquare(MapSquare square, int newWeight) {
        this.changedSquare = square;
        this.changedWeight = newWeight;
        pathfinder.setChangedMapSquare(square, newWeight);
        this.changed = true;
    }
    
    public void addTowerToSquare(MapSquare square, int newWeight) {
        setChangeMapSquare(square, newWeight);
        towerAdded = true;
    }
    
    public void removeTowerFromSquare(MapSquare square, int newWeight) {
        setChangeMapSquare(square, newWeight);
        towerRemoved = true;
    }

    public void setSomeReverse(boolean isRevert) {
        this.someReverse = true;
    }
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
