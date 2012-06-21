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
 * The class WaveManager handles all creepwave and is resposible for the
 * timing of waves, spwaning next wave after old is dead and determining when
 * the last wave was killed and the player has won.
 * @author Hans Ferchland
 */
public class WaveManager {

    /** The Constant WAVE_CREEP_INTERVAL. */
    public static final float WAVE_CREEP_INTERVAL = 2f;
    
    /** The Constant WAVE_PAUSE_TIME. */
    public static final float WAVE_PAUSE_TIME = 5f;
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of WaveManager.
     */
    private WaveManager() {
    }

    /**
     * The static method to retrive the one and only instance of WaveManager.
     *
     * @return single instance of WaveManager
     */
    public static WaveManager getInstance() {
        return WaveManagerHolder.INSTANCE;
    }

    /**
     * The holder-class WaveManagerHolder for the WaveManager.
     */
    private static class WaveManagerHolder {

        /** The Constant INSTANCE. */
        private static final WaveManager INSTANCE = new WaveManager();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /** The wave descriptions. */
    private Queue<WaveDescription> waveDescriptions =
            new LinkedList<WaveDescription>();
    
    /** The initialized. */
    private boolean initialized;
    
    /** The max waves. */
    private int maxWaves;
    
    /** The completed. */
    private boolean completed = false;
    
    /** The current wave count. */
    private int currentWaveCount = 0;
    
    /** The creep generation count. */
    private int creepGenerationCount = 0;
    
    /** The current wave. */
    private WaveDescription currentWave;
    
    /** is true if all creeps contained by the wave are spawned. */
    private boolean waveCompletlySpawned = false;
    
    /** The start timer. */
    private WaveTimer startTimer;
    
    /** The wave timer. */
    private WaveTimer waveTimer;
    
    /** The entity manager. */
    private EntityManager entityManager = EntityManager.getInstance();
    
    /** The creeps. */
    private HashMap<Integer, Creep> creeps =
            EntityManager.getInstance().getCreepHashMap();

    //==========================================================================
    //===   Methods
    //==========================================================================
    /**
     * Initialize.
     */
    public void initialize() {
        if (initialized) {
            return;
        }
        // setup the timers
        setupStartTimer();
        setupWaveTimer();
        // add the timer that starts game after 2 secs
        EventManager.getInstance().addTimerEventListener(startTimer);
        completed = false;
        currentWaveCount = 0;
        creepGenerationCount = 0;
        waveCompletlySpawned = false;

        initialized = true;
    }

    /**
     * Load waves.
     *
     * @param descriptions the descriptions
     */
    public void loadWaves(Queue<WaveDescription> descriptions) {
        this.waveDescriptions = descriptions;
        this.maxWaves = waveDescriptions.size();
    }

    /**
     * Destroy.
     */
    public void destroy() {
        if (!initialized) {
            return;
        }
        // remove both timers
        EventManager.getInstance().removeTimerEventListener(startTimer);
        startTimer = null;
        EventManager.getInstance().removeTimerEventListener(waveTimer);
        waveTimer = null;
        waveDescriptions.clear();
        creeps.clear();

        initialized = false;
    }

    /**
     * Checks if is initialized.
     *
     * @return true, if is initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Setup start timer.
     */
    private void setupStartTimer() {
        startTimer = new WaveTimer(WAVE_PAUSE_TIME) {

            @Override
            public void onTimedEvent(TimerEvent t) {
                // if start event occures start the waves
                startWaves();
            }
        };

    }

    /**
     * Start waves.
     */
    private void startWaves() {
        // start the wave timer and kill the start timer
        EventManager.getInstance().removeTimerEventListener(startTimer);
        EventManager.getInstance().addTimerEventListener(waveTimer);
        currentWave = waveDescriptions.poll();
        currentWaveCount = 0;
    }

    /**
     * Setup wave timer.
     */
    private void setupWaveTimer() {
        waveTimer = new WaveTimer(WAVE_CREEP_INTERVAL) {

            @Override
            public void onTimedEvent(TimerEvent t) {
                updateWaves(t);
            }
        };
    }

    /**
     * Update waves.
     *
     * @param t the t
     */
    private void updateWaves(TimerEvent t) {

        boolean stillCreeps = (creeps.size() > 0);

        if (waveCompletlySpawned) {
            if (stillCreeps) {
                if (currentWave.numberOfOrbDrobs > 0) {
                    for (Creep c : creeps.values()) {
                        if (!c.isDropping() && currentWave.numberOfOrbDrobs > 0) {
                            c.setDropping(true);
                            currentWave.numberOfOrbDrobs--;
                        }
                    }
                }
            } else {
                Player.getInstance().addGold(currentWave.goldAtEnd);
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

    /**
     * Wave killed.
     */
    private void waveKilled() {
        if (currentWaveCount < maxWaves - 1) {
            currentWaveCount++;
            currentWave = waveDescriptions.poll();
            creepGenerationCount = 0;
            waveCompletlySpawned = false;

        } else {
            completed = true;
        }
    }

    /**
     * Generate creep.
     *
     * @return the creep
     */
    private Creep generateCreep() {

        float maxCreepHealthPoints = currentWave.maxCreepHealthPoints;
        float creepSpeed = currentWave.creepSpeed;
        float creepDamage = currentWave.creepDamage;
        float creepOrbDropRate = currentWave.creepOrbDropRate;
        int creepGoldDrop = currentWave.creepGoldDrop;
        boolean dropping = currentWave.dropOrb();
        Creep c =
                entityManager.createCreep(
                "Wave#" + currentWaveCount + "-Creep#" + creepGenerationCount,
                Pathfinder.getInstance().getStartField().
                getSquare().getLocalTranslation(),
                maxCreepHealthPoints,
                maxCreepHealthPoints);
        c.setSpeed(creepSpeed);
        c.setDropping(dropping);
        c.setGoldDrop(creepGoldDrop);
        c.setOrbDropRate(creepOrbDropRate);
        c.setDamage(creepDamage);
        return c;

    }

    /**
     * Gets the current wave.
     *
     * @return the current wave
     */
    public WaveDescription getCurrentWave() {
        return currentWave;
    }

    /**
     * Gets the current wave count.
     *
     * @return the current wave count
     */
    public int getCurrentWaveCount() {
        return currentWaveCount;
    }

    /**
     * Gets the max waves.
     *
     * @return the max waves
     */
    public int getmaxWaves() {
        return maxWaves;
    }

    /**
     * Sets the start wave.
     *
     * @param startWave the new start wave
     */
    public void setStartWave(int startWave) {
        if (startWave < maxWaves) {
            this.currentWaveCount = startWave;
        }
    }

    /**
     * Checks if is completed.
     *
     * @return true, if is completed
     */
    public boolean isCompleted() {
        return completed;
    }

    //==========================================================================
    //===   Inner Classes
    //==========================================================================
    /**
     * The Class WaveTimer.
     */
    private abstract class WaveTimer implements TimerEventListener {

        /** The period. */
        private float period;

        /**
         * Instantiates a new wave timer.
         *
         * @param period the period
         */
        public WaveTimer(float period) {
            this.period = period;
        }

        /* (non-Javadoc)
         * @see eventsystem.listener.TimerEventListener#onTimedEvent(eventsystem.events.TimerEvent)
         */
        @Override
        public abstract void onTimedEvent(TimerEvent t);

        /* (non-Javadoc)
         * @see eventsystem.listener.TimerEventListener#getPeriod()
         */
        @Override
        public float getPeriod() {
            return period;
        }
    }

    /**
     * The Class WaveDescription.
     */
    public class WaveDescription {
        // Gold

        /** The gold at end. */
        public int goldAtEnd;
        //Creeps
        /** The creep count. */
        public int creepCount;
        
        /** The max creep health points. */
        public float maxCreepHealthPoints;
        
        /** The creep speed. */
        public float creepSpeed;
        
        /** The creep damage. */
        public float creepDamage;
        
        /** The creep orb drop rate. */
        public float creepOrbDropRate;
        
        /** The creep gold drop. */
        public int creepGoldDrop;
        //Boss
        /** The has boss. */
        public boolean hasBoss;
        
        /** The boss at first. */
        public boolean bossAtFirst;
        
        /** The boss count. */
        public int bossCount;
        
        /** The max boss health points. */
        public float maxBossHealthPoints;
        
        /** The boss speed. */
        public float bossSpeed;
        
        /** The boss damage. */
        public float bossDamage;
        
        /** The boss orb drop rate. */
        public float bossOrbDropRate;
        
        /** The boss orb drop count. */
        public float bossOrbDropCount;
        
        /** The boss gold drop. */
        public float bossGoldDrop;
        
        /** The number of orb drobs. */
        public int numberOfOrbDrobs;

        /**
         * Instantiates a new wave description.
         */
        public WaveDescription() {
        }

        /**
         * Instantiates a new wave description.
         *
         * @param goldAtStart the gold at start
         * @param creepCount the creep count
         * @param maxCreepHealthPoints the max creep health points
         * @param creepSpeed the creep speed
         * @param creepDamage the creep damage
         * @param creepOrbDropRate the creep orb drop rate
         * @param creepGoldDrop the creep gold drop
         * @param hasBoss the has boss
         * @param bossAtFirst the boss at first
         * @param bossCount the boss count
         * @param maxBossHealthPoints the max boss health points
         * @param bossSpeed the boss speed
         * @param bossDamage the boss damage
         * @param bossOrbDropRate the boss orb drop rate
         * @param bossOrbDropCount the boss orb drop count
         * @param bossGoldDrop the boss gold drop
         * @param numberOfOrbDrobs the number of orb drobs
         */
        public WaveDescription(int goldAtStart,
                int creepCount, float maxCreepHealthPoints, float creepSpeed,
                float creepDamage, float creepOrbDropRate, int creepGoldDrop,
                boolean hasBoss, boolean bossAtFirst, int bossCount,
                float maxBossHealthPoints, float bossSpeed, float bossDamage,
                float bossOrbDropRate, float bossOrbDropCount, float bossGoldDrop, int numberOfOrbDrobs) {
            this.goldAtEnd = goldAtStart;
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
            this.numberOfOrbDrobs = numberOfOrbDrobs;
        }

        /**
         * Drop orb.
         *
         * @return true, if successful
         */
        private boolean dropOrb() {
            if (Math.random() <= 0.25f && numberOfOrbDrobs > 0) {
                numberOfOrbDrobs--;
                return true;
            }
            return false;

        }
    }
}
