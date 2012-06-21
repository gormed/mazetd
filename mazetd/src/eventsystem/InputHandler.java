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
 * File: InputHandler.java
 * Type: eventsystem.InputHandler
 * 
 * Documentation created: 23.05.2012 - 16:20:09 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package eventsystem;

import com.jme3.input.InputManager;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import eventsystem.listener.KeyInputListener;
import eventsystem.listener.MouseInputListener;
import mazetd.MazeTDGame;

/**
 * The class InputHandler.
 * @author Hans Ferchland
 */
public class InputHandler {
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of InputHandler.
     */
    private InputHandler() {
        this.inputManager = MazeTDGame.getInstance().getInputManager();
    }

    /**
     * The static method to retrive the one and only instance of InputHandler.
     *
     * @return single instance of InputHandler
     */
    public static InputHandler getInstance() {
        return InputHandlerHolder.INSTANCE;
    }

    /**
     * The holder-class InputHandlerHolder for the InputHandler.
     */
    private static class InputHandlerHolder {

        /** The Constant INSTANCE. */
        private static final InputHandler INSTANCE = new InputHandler();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /** The input manager. */
    private InputManager inputManager;
    //==========================================================================
    //===   Methods
    //==========================================================================

    /**
     * Adds a key-input-mapping to the input-manager.
     * @param mapping the key-name of the mappig of the triggers
     * @param keyTriggers the triggers that will trigger 
     * the mapping for the listeners
     */
    void addKeyInputEvent(String mapping, KeyTrigger... keyTriggers) {
        inputManager.addMapping(mapping, keyTriggers);
    }

    /**
     * Adds a mouse-button-mapping to the input-manager.
     * @param mapping the key-name of the mappig of the triggers
     * @param mouseButtonTriggers the triggers that will trigger 
     * the mapping for the listeners
     */
    void addMouseButtonEvent(String mapping, MouseButtonTrigger... mouseButtonTriggers) {
        inputManager.addMapping(mapping, mouseButtonTriggers);
    }

    /**
     * Adds a mouse-movement-mapping to the input-manager.
     * @param mapping the key-name of the mappig of the triggers
     * @param mouseAxisTriggers the triggers that will trigger 
     * the mapping for the listeners
     */
    void addMouseMovementEvent(String mapping, MouseAxisTrigger... mouseAxisTriggers) {
        inputManager.addMapping(mapping, mouseAxisTriggers);
    }

    /**
     * Adds a KeyInputListener for one or more mappings.
     * @param inputListener the listener to add
     * @param mappings the mappings to hear for the listener
     */
    void addKeyInputListener(KeyInputListener inputListener, String... mappings) {
        inputManager.addListener(inputListener, mappings);
    }

    /**
     * Removes a KeyInputListener for all its mappings.
     * @param inputListener the listener to remove
     */
    void removeKeyInputListener(KeyInputListener inputListener) {
        inputManager.removeListener(inputListener);
    }

    /**
     * Adds a MouseInputListener for one or more mappings.
     * @param inputListener the listener to add
     * @param mappings the mappings to hear for the listener
     */
    void addMouseInputListener(MouseInputListener inputListener, String... mappings) {
        inputManager.addListener(inputListener, mappings);
    }

    /**
     * Removes a MouseInputListener for all its mappings.
     * @param inputListener the listener to remove
     */
    void removeMouseInputListener(MouseInputListener inputListener) {
        inputManager.removeListener(inputListener);
    }
}
