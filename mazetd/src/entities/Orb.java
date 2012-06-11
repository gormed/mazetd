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
 * File: Orb.java
 * Type: entities.Orb
 * 
 * Documentation created: 21.05.2012 - 22:02:25 by Hans Ferchland
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package entities;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import entities.base.ClickableEntity;
import entities.base.EntityManager;
import entities.effects.AbstractOrbEffect;
import entities.effects.OrbEffectType;
import entities.effects.PoisonOrbEffect;
import eventsystem.port.ScreenRayCast3D;
import mazetd.MazeTDGame;
import logic.Player;

/**
 * The class Orb for upgrade-orbs in MazeTD - for orbs that drop from 
 * killed creeps.
 * @author Hans Ferchland
 * @version 0.2
 */
public class Orb extends ClickableEntity {
    //==========================================================================
    //===   Enums
    //==========================================================================

    /**
     * The enum ElementType discribes which Elements for orbs exist.
     */
    public enum ElementType {

        RED,
        BLUE,
        GREEN,
        YELLOW,
        WHITE
    }
    //==========================================================================
    //===   Constants
    //==========================================================================
    private static final float HEIGHT_OVER_GROUND = 0.4f;
    public static final int ORB_DECAY = 2;
    private static final int ORB_SAMPLES = 10;
    private static final float ORB_SIZE = 0.125f;
    private static MazeTDGame game = MazeTDGame.getInstance();
    //==========================================================================
    //===   Private Fields
    //==========================================================================
    private Player player;
    private Vector3f position;
    private ElementType type;
    private Element element;
    private float height = 0.4f;
    private boolean deacying = false;
    private float decayTime = 0;
    private Tower attachedTower;
    private AbstractOrbEffect orbEffect;
    // Particle
    private ParticleEmitter explodesEmitter;

    //==========================================================================
    //===   Methods & Constructor
    //==========================================================================
    /**
     * Creates an orb from a position and any given ElementType.
     * @param name the desired name
     * @param position the desired position
     * @param type the desired ElementType
     */
    public Orb(String name, Vector3f position, ElementType type) {
        super(name);
        this.position = position;
        this.type = type;
    }

    @Override
    public Node createNode(MazeTDGame game) {
        super.createNode(game);

        this.element = createElement();

        clickableEntityNode.setLocalTranslation(position);
        clickableEntityNode.attachChild(element.geometry);

        createExpodesEmitter(game);

        return clickableEntityNode;
    }

    @Override
    protected void update(float tpf) {

        if (deacying) {

            decayTime += tpf;
            if (decayTime > ORB_DECAY) {
                ScreenRayCast3D.getInstance().
                        removeClickableObject(clickableEntityNode);
                EntityManager.getInstance().removeEntity(id);
            }
        }
        height += tpf;
        float y = 0.1f * (float) Math.sin(1.5f * height);
        element.geometry.setLocalTranslation(0, HEIGHT_OVER_GROUND + y, 0);

    }

    @Override
    public void onClick() {
        explodes();
        player=player.getInstance();
        player.addOrb(this);
    }

    @Override
    public void onMouseOver() {
    }

    @Override
    public void onMouseLeft() {
    }

    private void explodes() {
        emittExplosion();
        // set decaying
        deacying = true;
    }

    private void emittExplosion() {
        explodesEmitter.setLocalTranslation(element.geometry.getLocalTranslation());
        clickableEntityNode.attachChild(explodesEmitter);
        clickableEntityNode.detachChild(element.geometry);
        explodesEmitter.emitAllParticles();
        explodesEmitter.setParticlesPerSec(0);
    }

    private void createExpodesEmitter(MazeTDGame game) {
        /** Explosion effect. Uses Texture from jme3-test-data library! */
        explodesEmitter = new ParticleEmitter(
                "Debris", ParticleMesh.Type.Triangle, 20);
        Material debris_mat = new Material(
                game.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        debris_mat.setTexture("Texture",
                game.getAssetManager().
                loadTexture("Textures/Effects/shockwave.png"));
        explodesEmitter.setMaterial(debris_mat);
        explodesEmitter.setImagesX(1);
        explodesEmitter.setImagesY(1); // 3x3 texture animation
        explodesEmitter.setRotateSpeed(0);
        //explodesEmitter.setSelectRandomImage(true);
        explodesEmitter.getParticleInfluencer().
                setInitialVelocity(new Vector3f(0, -0.3f, 0));
        explodesEmitter.setStartColor(element.color);
        ColorRGBA end = element.color.clone();
        end.a = 0;
        explodesEmitter.setEndColor(end);
        explodesEmitter.setStartSize(ORB_SIZE);
        explodesEmitter.setEndSize(ORB_SIZE * 3);
        explodesEmitter.setLowLife(1.0f);
        explodesEmitter.setHighLife(1.5f);
        explodesEmitter.setGravity(0f, -1.5f, 0f);
        explodesEmitter.getParticleInfluencer().setVelocityVariation(.5f);
        //clickableEntityNode.attachChild(debris);
        //debris.emitAllParticles();

    }

    /**
     * Creates the Element from the given ElementType from the Orb.
     * @return the Element of the orb
     */
    private Element createElement() {
        switch (type) {
            case BLUE:
                return Element.getBlueElement();
            case RED:
                return Element.getRedElement();
            case GREEN:
                return Element.getGreenElement();
            case YELLOW:
                return Element.getYellowElement();
            case WHITE:
                return Element.getWhiteElement();
            default:
                return Element.getBlueElement();
        }
    }

    public ElementType getElementType() {
        return type;
    }

    public AbstractOrbEffect createOrbEffect() {
        switch (type) {
            case BLUE:

                return null;
            case GREEN:
                return new PoisonOrbEffect(OrbEffectType.POISON, 0);
            case RED:
                
                return null;
            case WHITE:
                
                return null;
            case YELLOW:
            default:    
                return null;
        }
    }
    
    AbstractOrbEffect getOrbEffect() {
        return createOrbEffect();
    }
    
    ColorRGBA getOrbColor() {
        return element.color.clone();
    }

    //==========================================================================
    //===   Inner Classes
    //==========================================================================
    /**
     * The class Element discribes the orb-element from the Orb.
     * @author Hans Ferchland
     */
    private static class Element {

        /**
         * Creates a red orb-element.
         * @return the red orb-element
         */
        public static Element getRedElement() {
            Element e = new Element();
            e.color = ColorRGBA.Red.clone();
            e.createBaseElement();

            return e;
        }

        /**
         * Creates a blue orb-element.
         * @return the blue orb-element
         */
        public static Element getBlueElement() {
            Element e = new Element();
            e.color = ColorRGBA.Blue.clone();
            e.createBaseElement();

            return e;
        }

        /**
         * Creates a green orb-element.
         * @return the green orb-element
         */
        public static Element getGreenElement() {
            Element e = new Element();
            e.color = ColorRGBA.Green.clone();
            e.createBaseElement();

            return e;
        }

        /**
         * Creates a yellow orb-element.
         * @return the yellow orb-element
         */
        public static Element getYellowElement() {
            Element e = new Element();
            e.color = ColorRGBA.Yellow.clone();
            e.createBaseElement();

            return e;
        }

        /**
         * Creates a white orb-element.
         * @return the white orb-element
         */
        public static Element getWhiteElement() {
            Element e = new Element();
            e.color = ColorRGBA.White.clone();
            e.createBaseElement();

            return e;
        }
        private ColorRGBA color;
        private Geometry geometry;
        private Material material;

        /**
         * Hidden constructor, only static creation should happen!
         */
        private Element() {
        }

        /**
         * Creates the base of any element.
         */
        private void createBaseElement() {
            // orbs Material        
            material = new Material(
                    game.getAssetManager(),
                    "Common/MatDefs/Misc/Unshaded.j3md");
            material.setColor("Color", color);
            material.setColor("GlowColor", color);
            material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

            Sphere s = new Sphere(ORB_SAMPLES, ORB_SAMPLES, ORB_SIZE);

            geometry = new Geometry("element_orb_geometry", s);
            geometry.setMaterial(material);
            geometry.setLocalTranslation(0, HEIGHT_OVER_GROUND, 0);
            geometry.setQueueBucket(Bucket.Transparent);
            geometry.setShadowMode(ShadowMode.CastAndReceive);
        }
    }
}
