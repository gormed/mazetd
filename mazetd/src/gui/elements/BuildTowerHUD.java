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
 * File: BuildTowerHUD.java
 * Type: gui.elements.BuildTowerHUD
 * 
 * Documentation created: 03.06.2012 - 12:58:17 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package gui.elements;

import com.jme3.collision.CollisionResult;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Quad;
import entities.Map;
import entities.Map.MapSquare;
import entities.geometry.ClickableGeometry;
import eventsystem.EventManager;
import eventsystem.events.TimerEvent;
import eventsystem.listener.TimerEventListener;
import eventsystem.port.ScreenRayCast3D;
import logic.Level;
import logic.Player;
import logic.pathfinding.Pathfinder;
import mazetd.MazeTDGame;

/**
 *
 * @author Hans Ferchland
 */
public class BuildTowerHUD implements TimerEventListener {

    public static final float ICON_FADE_DIST = 0.25f;
    public static final float SIGN_SIZE = 0.7f;
    //==========================================================================
    //===   Singleton
    //==========================================================================

    /**
     * The hidden constructor of BuildTowerHUD.
     */
    private BuildTowerHUD() {
    }

    /**
     * The static method to retrive the one and only instance of BuildTowerHUD.
     */
    public static BuildTowerHUD getInstance() {
        return BuildTowerHUDHolder.INSTANCE;
    }

    /**
     * The holder-class BuildTowerHUDHolder for the BuildTowerHUD.
     */
    private static class BuildTowerHUDHolder {

        private static final BuildTowerHUD INSTANCE = new BuildTowerHUD();
    }
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Geometry geometry;
    private Material material;
    private Node translationNode;
    private boolean initialized = false;
    private float period = 0f;
    private float scale = 0.0f;
    private MazeTDGame game = MazeTDGame.getInstance();
    private MapSquare currentSquare;
    private Vector3f clickPosition;
    private Camera cam = MazeTDGame.getInstance().getCamera();
    //==========================================================================
    //===   Methods
    //==========================================================================

    public void initialize() {
        if (initialized) {
            return;
        }
        createHUD(game);
        initialized = true;
    }

    private void createHUD(MazeTDGame game) {

        material = new Material(game.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", game.getAssetManager().
                loadTexture("Textures/HUD/TowerIcon.png"));
        material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        Quad q = new Quad(SIGN_SIZE, SIGN_SIZE);

        geometry = new ClickableGeometry("HUD_Geometry", q) {

            @Override
            public void onRayCastClick(Vector2f mouse, CollisionResult result) {
                if (currentSquare != null) {
                    currentSquare.buildTowerOnField();
                }
            }

            @Override
            public void onRayCastMouseOver(Vector2f mouse, CollisionResult result) {
                if (currentSquare != null) {
                    currentSquare.setHovered(true);
                }
            }

            @Override
            public void onRayCastMouseLeft(Vector2f mouse, CollisionResult result) {
                if (currentSquare != null) {
                    currentSquare.setHovered(false);
                }
            }
        };

        geometry.setMaterial(material);
        geometry.setCullHint(CullHint.Never);
        geometry.setQueueBucket(Bucket.Translucent);

        translationNode = new Node("HUD_Translation");
        translationNode.attachChild(geometry);
    }

    private void orientate() {
        Vector3f position = new Vector3f(SIGN_SIZE / 2, .15f, -SIGN_SIZE / 2);

        Vector3f up = cam.getUp().clone();
        Vector3f dir = cam.getDirection().
                clone().negateLocal().normalizeLocal();
        Vector3f left = cam.getLeft().
                clone().normalizeLocal().negateLocal();

        Quaternion look = new Quaternion();
        look.fromAxes(left, up, dir);

        geometry.setLocalTransform(new Transform(position, look));

    }

    @Override
    public void onTimedEvent(TimerEvent t) {
        if (clickPosition != null) {
            Vector3f mouse = ScreenRayCast3D.getInstance().getLastWorldHit().clone();
            mouse.y = 0;

            float dist = mouse.subtract(clickPosition).length();

            material.setColor("Color", new ColorRGBA(1, 1, 1, 1 - dist / ICON_FADE_DIST));

            if (Math.abs(dist) > ICON_FADE_DIST) {
                geometry.setCullHint(CullHint.Always);
                hide();
            } else {
                Vector3f square = currentSquare.getLocalTranslation();
                scale += t.getTimeGap();
                float s = 1 + (0.05f * (float) Math.sin(Math.PI * 2 * scale * 0.5f));
                translationNode.setLocalScale(s, s, s);
                Vector3f pos = new Vector3f(square.x, 0, square.z - 0.025f);
                translationNode.setLocalTranslation(pos);
                geometry.setCullHint(CullHint.Never);
                orientate();
            }
        }
    }

    @Override
    public float getPeriod() {
        return period;
    }

    public void destroy() {
        if (!initialized) {
            return;
        }

        EventManager.getInstance().removeTimerEventListener(this);
        ScreenRayCast3D.getInstance().removeClickableObject(translationNode);
        initialized = false;
    }

    public void show(MapSquare square) {
        if (initialized && square != null &&
                square.getTower() == null) {
            EventManager.getInstance().addTimerEventListener(this);
            clickPosition = ScreenRayCast3D.getInstance().getLastWorldHit().clone();
            ScreenRayCast3D.getInstance().addClickableObject(translationNode);
            clickPosition.y = 0;
            currentSquare = square;
        }
    }

    public void hide() {
        if (initialized) {
            currentSquare.setHovered(false);
            EventManager.getInstance().removeTimerEventListener(this);
            ScreenRayCast3D.getInstance().removeClickableObject(translationNode);
            currentSquare = null;
            clickPosition = null;
        }
    }
    //==========================================================================
    //===   Inner Classes
    //==========================================================================
}
