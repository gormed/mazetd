package gamestates;

import mazetd.MazeTDGame;

/**
 * The Class Gamestate. 
 * @author Hans Ferchland
 * @version 0.1
 */
public abstract class Gamestate {

    /** The name. */
    private String name;

    /**
     * Instantiates a new gamestate.
     *
     * @param name the name
     */
    public Gamestate(String name) {
        this.name = name;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Enter.
     */
    void enter() {
        loadContent(MazeTDGame.getInstance());
    }

    /**
     * Leave.
     */
    void leave() {
        unloadContent();
    }

    /**
     * Pause.
     */
    public void pause() {
    }

    /**
     * Resume.
     */
    public void resume() {
    }

    /**
     * Reset.
     */
    public void reset() {
    }

    /**
     * Terminate.
     */
    public void terminate() {
    }

    /**
     * Updates the.
     *
     * @param tpf the tpf
     */
    public abstract void update(float tpf);

    /**
     * Load content.
     *
     * @param game the game
     */
    protected abstract void loadContent(MazeTDGame game);

    /**
     * Unload content.
     */
    protected abstract void unloadContent();
}
