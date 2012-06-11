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
 * File: WaveManager.java
 * Type: logic.WaveManager
 * 
 * Documentation created: 01.06.2012 - 14:31:33 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;

import com.jme3.math.Vector2f;
import entities.Creep;
import entities.base.EntityManager;
import eventsystem.EventManager;
import eventsystem.events.TimerEvent;
import eventsystem.listener.TimerEventListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import logic.pathfinding.Pathfinder;

/**
 *
 * @author Hans Ferchland
 */
public class WaveManager {
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of WaveManager.
     */
    private WaveManager() {
        // setup the timers
        setupStartTimer();
        setupWaveTimer();
    }

    /**
     * The static method to retrive the one and only instance of WaveManager.
     */
    public static WaveManager getInstance() {
        return WaveManagerHolder.INSTANCE;
    }

    /**
     * The holder-class WaveManagerHolder for the WaveManager.
     */
    private static class WaveManagerHolder {

        private static final WaveManager INSTANCE = new WaveManager();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Queue<WaveDescription> waveDescriptions =
            new LinkedList<WaveDescription>();
    private boolean initialized;
    private int maxWaves;
    private int currentWaveCount = 0;
    private int creepGenerationCount = 0;
    private WaveDescription currentWave;
    /** is true if all creeps contained by the wave are spawned */
    private boolean waveCompletlySpawned;
    private WaveTimer startTimer;
    private WaveTimer waveTimer;
    private EntityManager entityManager = EntityManager.getInstance();
    private HashMap<Integer, Creep> creeps =
            EntityManager.getInstance().getCreepHashMap();
    //==========================================================================
    //===   Methods
    //==========================================================================

    public void initialize() {
        if (initialized) {
            return;
        }
        // add the timer that starts game after 2 secs
        EventManager.getInstance().addTimerEventListener(startTimer);

        initialized = true;
    }

    public void loadWaves(Queue<WaveDescription> descriptions) {
        this.waveDescriptions = descriptions;
        this.maxWaves = waveDescriptions.size();
    }

    public void destroy() {
        if (!initialized) {
            return;
        }
        // remove both timers
        EventManager.getInstance().removeTimerEventListener(startTimer);
        EventManager.getInstance().removeTimerEventListener(waveTimer);
        initialized = false;
    }

    public boolean isInitialized() {
        return initialized;
    }

    private void setupStartTimer() {
        startTimer = new WaveTimer(2) {

            @Override
            public void onTimedEvent(TimerEvent t) {
                // if start event occures start the waves
                startWaves();
            }
        };

    }

    private void startWaves() {
        // start the wave timer and kill the start timer
        EventManager.getInstance().removeTimerEventListener(startTimer);
        EventManager.getInstance().addTimerEventListener(waveTimer);
        currentWave = waveDescriptions.poll();
        currentWaveCount = 0;
    }

    private void setupWaveTimer() {
        waveTimer = new WaveTimer(2f) {

            @Override
            public void onTimedEvent(TimerEvent t) {
                updateWaves(t);
            }
        };
    }

    private void updateWaves(TimerEvent t) {

        boolean stillCreeps = (creeps.size() > 0);

        if (waveCompletlySpawned) {
            if (stillCreeps) {
                // no creeps have to be generated, because the old wave still lives
            } else {
                waveKilled();
            }
        } else {
            if (currentWave != null && creepGenerationCount < currentWave.creepCount) {
                Creep c = generateCreep();
                creepGenerationCount++;
            } else {
                waveCompletlySpawned = true;
            }
        }
    }

    private void waveKilled() {
        if (currentWaveCount < maxWaves) {
            currentWaveCount++;
            currentWave = waveDescriptions.poll();
            creepGenerationCount = 0;
            waveCompletlySpawned = false;
            
        } else {
        }
    }

    private Creep generateCreep() {

        float maxCreepHealthPoints = currentWave.maxCreepHealthPoints;
        float creepSpeed = currentWave.creepSpeed;
        float creepDamage = currentWave.creepDamage;
        float creepOrbDropRate = currentWave.creepOrbDropRate;
        int creepGoldDrop = currentWave.creepGoldDrop;
        Creep c =
                entityManager.createCreep(
                "Wave#" + currentWaveCount + "-Creep#" + creepGenerationCount,
                Pathfinder.getInstance().getStartField().
                getSquare().getLocalTranslation(),
                maxCreepHealthPoints,
                maxCreepHealthPoints);
        c.setSpeed(creepSpeed);
        c.setGoldDrop(creepGoldDrop);
        c.setOrbDropRate(creepOrbDropRate);
        c.setDamage(creepDamage);
        return c;

    }

    public WaveDescription getCurrentWave() {
        return currentWave;
    }

    public int getCurrentWaveCount() {
        return currentWaveCount;
    }
    
     public int getmaxWaves() {
        return maxWaves;
    }
    
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
    private abstract class WaveTimer implements TimerEventListener {

        private float period;

        public WaveTimer(float period) {
            this.period = period;
        }

        @Override
        public abstract void onTimedEvent(TimerEvent t);

        @Override
        public float getPeriod() {
            return period;
        }
    }

    public class WaveDescription {
        //Creeps

        public int creepCount;
        public float maxCreepHealthPoints;
        public float creepSpeed;
        public float creepDamage;
        public float creepOrbDropRate;
        public int creepGoldDrop;
        //Boss
        public boolean hasBoss;
        public boolean bossAtFirst;
        public int bossCount;
        public float maxBossHealthPoints;
        public float bossSpeed;
        public float bossDamage;
        public float bossOrbDropRate;
        public float bossOrbDropCount;
        public float bossGoldDrop;

        public WaveDescription() {
        }

        public WaveDescription(
                int creepCount, float maxCreepHealthPoints, float creepSpeed,
                float creepDamage, float creepOrbDropRate, int creepGoldDrop,
                boolean hasBoss, boolean bossAtFirst, int bossCount,
                float maxBossHealthPoints, float bossSpeed, float bossDamage,
                float bossOrbDropRate, float bossOrbDropCount, float bossGoldDrop) {
            this.creepCount = creepCount;
            this.maxCreepHealthPoints = maxCreepHealthPoints;
            this.creepSpeed = creepSpeed;
            this.creepDamage = creepDamage;
            this.creepOrbDropRate = creepOrbDropRate;
            this.creepGoldDrop = creepGoldDrop;
            this.hasBoss = hasBoss;
            this.bossAtFirst = bossAtFirst;
            this.bossCount = bossCount;
            this.maxBossHealthPoints = maxBossHealthPoints;
            this.bossSpeed = bossSpeed;
            this.bossDamage = bossDamage;
            this.bossOrbDropRate = bossOrbDropRate;
            this.bossOrbDropCount = bossOrbDropCount;
            this.bossGoldDrop = bossGoldDrop;
        }
    }
}
