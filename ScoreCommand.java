
package bork;

/**
 * Handles commands to view the players current score
 * @author Woodruff and Brian E. Burns
 */
class ScoreCommand extends Command {

      ScoreCommand() {}
      
      /**
       * Returns the String of the players current score with a message about their ranking/level
       * @return String message with the score and ranking
       */
      public String execute() {
          int score = GameState.instance().getScore();
          String desc = "You have accumulated " + score + " points. Your current"
                  + " rank is ";
          
          if (score < 25) {
              desc += "Amateur Adventurer.\n";
          }
          else if (score < 50) {
              desc += "Common Adventurer.\n";
          }
          else if (score < 75) {
              desc += "Advanced Adventurer.\n";
          }
          else {
              desc += "Expert Adventurer.\n";
          }
          
          return desc;
      }
}
