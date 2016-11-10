
package bork;

/**
 * Handles command to turn a light source on or off
 * @author Woodruff
 */
class ToggleLightCommand extends Command {

      private String lightSourceName;
      private String toggleState;
      
      /**
       * Constructor for the ToggleLightCommand with the input of which light source and with state to turn it to
       * @param lightSourceName the name of the light source item being toggled
       * @param toggleState the state it is being toggled to
       */
      ToggleLightCommand(String lightSourceName, String toggleState) {}
      
      /** 
       * Returns the String indicating the change in state of the light source
       * @return String the message reflecting the change or lack of change in the light sources state
       */
       public String execute() {return "";}
}
