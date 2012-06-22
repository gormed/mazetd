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
 * File: ClickableEntity.java
 * Type: entities.base.ClickableEntity
 * 
 * Documentation created: 22.05.2012 - 20:57:41 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities.base;

import entities.base.low.SimpleClickable;
import com.jme3.scene.Node;
import entities.nodes.ClickableEntityNode;
import mazetd.MazeTDGame;

/**
 * The class ClickableEntity.
 * @author Hans Ferchland
 * @version 1.0
 */
public abstract class ClickableEntity extends AbstractEntity implements SimpleClickable {

    //==========================================================================
    //===   Private Fields
    //==========================================================================
    /** The clickable entity node. */
    protected ClickableEntityNode clickableEntityNode;
    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================

    /**
     * Constructs an ClickableEntity by a given name.
     *
     * @param name the desired name
     */
    public ClickableEntity(String name) {
        super(name);
    }

    /* (non-Javadoc)
     * @see entities.base.AbstractEntity#createNode(mazetd.MazeTDGame)
     */
    @Override
    public Node createNode(MazeTDGame game) {
        
        clickableEntityNode =
                new ClickableEntityNode(name + "s_GeometryNode", this);
        super.createNode(game).attachChild(clickableEntityNode);
        
        return clickableEntityNode;
    }

    /* (non-Javadoc)
     * @see entities.base.low.SimpleClickable#getClickableEntityNode()
     */
    @Override
    public ClickableEntityNode getClickableEntityNode() {
        return clickableEntityNode;
    }

    /**
     * Is called if geometry is clicked.
     */
    @Override
    public abstract void onClick();

    /**
     * Is called if geometry is hovered.
     */
    @Override
    public abstract void onMouseOver();

    /**
     * Is called if geometry is not hovered anymore.
     */
    @Override
    public abstract void onMouseLeft();
}
