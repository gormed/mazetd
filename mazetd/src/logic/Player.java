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
 * File: Player.java
 * Type: logic.Player
 * 
 * Documentation created: 13.05.2012 - 23:13:37 by Hans
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package logic;
import java.util.ArrayList;
import entities.Orb;

/**
 * The class Player
 * @author Ahmed
 * @version
 */
public class Player {
    
    public static final int PLAYRER_HEALTH = 500;
    
    public static Player instance;
   
    private int redCount=0;
    private int blueCount=0;
    private int greenCount=0;
    private int yellowCount=0;
    private int whiteCount=0;
    
    private int gold=500;
    
    
    private Orb.ElementType type;
    
    
    private ArrayList<Orb> inventory;
    
     
 

  
    
      
    public static Player getInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new Player();
    }
    
    
    public Player(){
    inventory = new ArrayList<Orb>();
    }
    
    public void removeOrb(Orb orb) {
    inventory.remove(orb);
    type=orb.getElementType();
    
    switch (type) {
            case RED : redCount= redCount-1;
                break;
            case BLUE : blueCount= blueCount-1;
                break;
            case GREEN : greenCount= greenCount-1;
                break;
            case YELLOW : yellowCount= yellowCount-1;
                break;
            case WHITE : whiteCount= whiteCount-1;
            break;
            default:
            break;
    }
  }
    
    public void addOrb(Orb orb) {
    inventory.add(orb);
    type=orb.getElementType();
    
    switch (type) {
            case RED : redCount= redCount+1;
                break;
            case BLUE : blueCount= blueCount+1;
                break;
            case GREEN : greenCount= greenCount+1;
                break;
            case YELLOW : yellowCount= yellowCount+1;
                break;
            case WHITE : whiteCount= whiteCount+1;
            break;
            default:
            break;     
  
  }
    }
    
    public void addGold(int golddrop){
    gold=gold+golddrop;
    }
    
    public void chargeGold(int costs){
    gold=gold-costs;
    }
    
     public void buyOrb(){
    //TODO
    }
     
    public int getGold(){
    return gold;
    }

    public int getRedCount(){
    return redCount;
    }
    
    public int getBlueCount(){
    return blueCount;
    }
      
    public int getGreenCount(){
    return greenCount;
    }
    
    public int getYellowCount(){
    return yellowCount;
    }
   
    public int getWhiteCount(){
    return whiteCount;
    }
}
