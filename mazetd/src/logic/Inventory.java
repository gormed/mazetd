/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;
import entities.Orb;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;

/**
 *
 * @author Ahmed
 */
public class Inventory {
    
    private Orb orb;
    private Object o;
    private Orb.ElementType type;
    private HashMap <String, Orb> towerSlot;
    
    private boolean redElement;
    private boolean blueElement;
    private boolean greenElement;
    private boolean yellowElement;
    private boolean whiteElement;
    
    private boolean slot1hasOrb;
    private boolean slot2hasOrb;
    private boolean slot3hasOrb;
       
    public enum Slot {
        SLOT1,
        SLOT2,
        SLOT3,
    }
            

    
    public Inventory(){
    towerSlot = new HashMap<String, Orb>();
    }
    
    public void InventoryInit(){
    towerSlot.put("Slot 1", null);
    towerSlot.put("Slot 2", null);
    towerSlot.put("Slot 3", null);
    
    slot1hasOrb=false;
    slot2hasOrb=false;
    slot3hasOrb=false;

    
    }
    public void addOrbSlot(Slot slot,Orb orb) {
   
        switch (slot) {
            
            case SLOT1:
               if (slot1hasOrb == false){
               towerSlot.put("Slot 1", orb);
               slot1hasOrb=true;
               }
            case SLOT2:
               if (slot2hasOrb == false){
               towerSlot.put("Slot 2", orb);
               slot2hasOrb=true; 
               }  
            case SLOT3:
               if (slot3hasOrb ==false){
               towerSlot.put("Slot 3", orb);
               slot3hasOrb=true;
               }
            default:
    }
   }

    
    public void removeOrb(Slot slot, Orb orb) {
     switch (slot) {
            case SLOT1:
               towerSlot.remove("Slot 1");
               slot1hasOrb=false;
            case SLOT2:
               towerSlot.remove("Slot 2");
               slot2hasOrb=false;
            case SLOT3:
               towerSlot.remove("Slot 3");
               slot3hasOrb=false;
            default:
    }
  }
    
    public void checkElementTypes(){
    
    redElement = false;
    blueElement = false;
    greenElement = false;
    yellowElement = false;
    whiteElement = false;
    
    Collection c = towerSlot.values();
    Iterator itr = c.iterator();
   
    while(itr.hasNext())
    o = itr.next();
    orb = (Orb) o;
    type=orb.getElementType();
    
    switch (type) {
            case RED:
                redElement = true;
            case BLUE:
                blueElement = true;
            case GREEN:
                greenElement = true;
            case YELLOW:
                yellowElement = true;
            case WHITE:
               whiteElement = true;
            default:
    }
    }
    
    
    public boolean containsRedElementType(){
    return redElement;
    }
    
    public boolean containsBlueElementType(){
    return blueElement;
    }
     
    public boolean containsGreenElementType(){
    return greenElement;
    }
      
    public boolean containsYellowElementType(){
    return yellowElement;
    }
    
    public boolean containsWhiteElementType(){
    return whiteElement;
    }
    
}
    