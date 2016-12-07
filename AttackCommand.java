
package bork;

/**
 * Handles the command to attack something/one else
 * @author Woodruff & Christopher Ringham
 */
class AttackCommand extends Command {

      private String weaponName;
      private String targetName;
      
      /**
       * Constructor for the weapon the player uses and the target of the attack
       * @param weaponName the name of the weapon the player is attacking with
       * @param targetName the name of the target the player is attacking
       */
      AttackCommand(String weaponName, String targetName)
      {
          this.weaponName = weaponName;
          this.targetName = targetName;
      }
      
      /**
       * Returns the String based on the calculated combat exchange 
       * @return String with lines of combat and messages about the round
       */
      @Override
      public String execute()
      {
          try
          {
            //Player attack phase
            int playerScore = GameState.instance().getScore();
            int weaponDamage = 0;
            Item itemReferredTo = GameState.instance().getItemFromInventoryNamed(weaponName);
            if(itemReferredTo instanceof Weapon)
            {
                Weapon wepReferredTo = (Weapon)itemReferredTo;
                weaponDamage = wepReferredTo.getDamage(); //if it is a weapon
            } else {
                weaponDamage = 1; //if it is not a weapon
            }
            NPC targetNPC = GameState.instance().getNpcInVicinityNamed(targetName);
            targetNPC.wound(weaponDamage+(playerScore/2));

            if(weaponDamage+(playerScore/2) >= targetNPC.getHealth()) //Make sure the NPC isn't dead
            {
                //NPC attack phase
                int NPCScore = targetNPC.getScore();
                weaponDamage = targetNPC.getWeapon().getDamage();
                GameState.instance().wound(weaponDamage+(NPCScore/2));
            }
          } catch (Item.NoItemException ex) {
              return "You do not have the " + this.weaponName + " to attack with.";
          } catch (NPC.NoNpcException e) {
              return this.targetName + " is not in the room.\n";
          } catch (Weapon.NoWeaponException x)
          {
              try
              {
                NPC targetNPC = GameState.instance().getNpcInVicinityNamed(targetName);
                GameState.instance().wound(1+(targetNPC.getScore()/2));
              
              } catch (NPC.NoNpcException e) {
              return this.targetName + " is not in the room.\n";
              
              }
              
          }
          
          return "You've exchanged blows!\n";
         
      }
}
