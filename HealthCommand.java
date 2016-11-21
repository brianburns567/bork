
package bork;

/**
 * Handles the command to view the user's current health.
 * @author Woodruff
 */
class HealthCommand extends Command {

      HealthCommand() {}
      
      /**
       * Returns the String based on the players current health
       * @return String the line expressing how healthy the player is
       */
      public String execute() {
          int health = GameState.instance().getHealth();
          
          if (health > 74) {
              return "You are at your peak performance.\n";
          }
          else if (health > 49) {
              return "You're a bit fatigued.\n";
          }
          else if (health > 24) {
              return "You're pretty banged up.\n";
          }
          else {
              return "Think Bruce Willis near the end of \"Die Hard\". Each "
                      + "move from here on out could be your last.\n";
          }
      }
}
