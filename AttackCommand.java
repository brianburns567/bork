
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
            Item wepReferredTo = GameState.instance().getItemFromInventoryNamed(weaponName);
            if(wepReferredTo instanceof Weapon)
            {
                int weaponDamage = GameState.instance().getItemFromInventoryNamed(weaponName).getDamage();
            } else {
                int weaponDamage = 1;
            }
            NPC targetNPC = GameState.instance().getNPCInVicinity();
            targetNPC.wound(weaponDamage+(playerScore/2));

            //NPC attack phase
            int NPCScore = targetNPC.getScore();
            int weaponDamage = targetNPC.getWeaponFromInventory();
            GameState.instance().wound(weaponDamage+(NPCScore/2));
            
          } catch (Weapon.NoWeaponException ex) {
              return "You do not have the " + this.weaponName + " to attack with.";
          } catch (NPC.NoNPCException e) {
              return this.targetName + "is not in the room.";
          }
          
          return "You exchange blows!";
         
      }
}
