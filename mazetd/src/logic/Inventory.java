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
  
    public Inventory(){

    }
    
    public static Inventory getInstance() {
      if (instance != null) {
            return instance;
        }
        return instance = new Inventory();
     }
  
   public Orb.ElementType Slot1Context(){
   tower=player.getSelectedTower();
   Orb orb1=tower.getFirstOrb();  
   
   if(orb1==null){
   return null;
   }
   else{
   orb1.getElementType();
   return type;   
  }
   }
   
   public Orb.ElementType Slot2Context(){
   tower=player.getSelectedTower();
   Orb orb2=tower.getSecondOrb();

   if(orb2==null){
   return null;
   }
   else{
   orb2.getElementType();
   return type;   
  }
   }
   
   public Orb.ElementType Slot3Context(){
   tower=player.getSelectedTower();
   Orb orb3=tower.getThirdOrb();
 
   if(orb3==null){
   return null;
   }
   else{
   orb3.getElementType();
   return type;   
  }
   }
   
    
   public void placeOrb(int slot, Orb.ElementType type) {
        
       tower=player.getSelectedTower();
        
        if(player.hasType(type)){
            
            switch (slot) {
            case 0:
            tower.placeOrb(type, 0);
            case 1:
            tower.placeOrb(type, 1); 
            case 2:
            tower.placeOrb(type, 2);
            default:
            }
        }
        else{
        System.out.println("Player does not have this Orb");
        }
   }

    
    public void replaceOrb(int slot, Orb.ElementType type) {
        tower=player.getSelectedTower();
      
        if(player.hasType(type)){
            
            switch (slot) {
            case 0:
            tower.replaceOrb(type, 0);
            case 1:
            tower.replaceOrb(type, 1); 
            case 2:
            tower.replaceOrb(type, 2);
            default:
            }
        }
        else{
        System.out.println("Player does not have this Orb");
        }
  }
    

    
}
    