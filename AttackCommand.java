
package bork;

/**
 * Handles the command to attack something/one else
 * @author Woodruff
 */
class AttackCommand extends Command {

      private Weapon weapon;
      private NPC target;
      
      /**
       * Constructor for the weapon the player uses and the target of the attack
       * @param weaponName the name of the weapon the player is attacking with
       * @param targetName the name of the target the player is attacking
       */
      AttackCommand(String weaponName, String targetName) {}
      
      /**
       * Returns the String based on the calculated combat exchange 
       * @return String with lines of combat and messages about the round
       */
      public String execute() {return "";}
}
