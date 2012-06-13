/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;
import entities.Orb;
import entities.Tower;
import logic.Player;

/**
 *
 * @author Ahmed
 */
public class Inventory {
    
    private Player player = Player.getInstance();
    private Tower tower;
    private Orb.ElementType type;

    
    public static Inventory instance;
       
    public enum Slot {
        SLOT1,
        SLOT2,
        SLOT3,
    }
            

    
    public Inventory(){

    }
    
    public static Inventory getInstance() {
      if (instance != null) {
            return instance;
        }
        return instance = new Inventory();
     }

    
   public void placeOrb(Slot slot, Orb.ElementType type) {
        
       tower=player.getSelectedTower();
        
        if(player.hasType(type)){
            
            switch (slot) {
            case SLOT1:
            tower.placeOrb(type, 0);
            case SLOT2:
            tower.placeOrb(type, 1); 
            case SLOT3:
            tower.placeOrb(type, 2);
            default:
            }
        }
        else{
        System.out.println("Player does not have this Orb");
        }
   }

    
    public void replaceOrb(Slot slot, Orb.ElementType type) {
        tower=player.getSelectedTower();
      
        if(player.hasType(type)){
            
            switch (slot) {
            case SLOT1:
            tower.replaceOrb(type, 0);
            case SLOT2:
            tower.replaceOrb(type, 1); 
            case SLOT3:
            tower.replaceOrb(type, 2);
            default:
            }
        }
        else{
        System.out.println("Player does not have this Orb");
        }
  }
    

    
}
    