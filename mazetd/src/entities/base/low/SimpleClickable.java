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
 * File: SimpleClickable.java
 * Type: entities.base.SimpleClickable
 * 
 * Documentation created: 23.05.2012 - 20:12:58 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.base.low;

import entities.nodes.ClickableEntityNode;

/**
 * The inteface clickable for clickable entities.
 * @author Hans Ferchland
 */
public interface SimpleClickable {

    /**
     * Gets the geometry that is clickable and will raise the given events if 
     * clicked.
     * @return the node with the geometry that will be clickable
     */
    public ClickableEntityNode getClickableEntityNode();

    /**
     * Is called if geometry is clicked.
     */
    public void onClick();

    /**
     * Is called if geometry is hovered.
     */
    public void onMouseOver();

    /**
     * Is called if geometry is not hovered anymore.
     */
    public void onMouseLeft();
}
