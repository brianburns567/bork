
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
      ToggleLightCommand(String lightSourceName, String toggleState) {
            this.lightSourceName = lightSourceName;
            this.toggleState = toggleState;
      }
      
      /** 
       * Returns the String indicating the change in state of the light source
       * @return String the message reflecting the change or lack of change in the light sources state
       */
      @Override
       public String execute() {
            Item itemReferredTo = null;
            try {
                  itemReferredTo = GameState.instance().getItemInVicinityNamed(lightSourceName);
            } catch (Item.NoItemException e) {
                  return "There's no " + lightSourceName + " here.";
            }
            if (!(itemReferredTo instanceof LightSource)){
                  return "The " + lightSourceName + " is not a light source.";
            }
            LightSource lsReferredTo = (LightSource)itemReferredTo;
            if (toggleState.equals("on")) {
                  if (lsReferredTo.isLit()) {
                        return "The " + lightSourceName + " is already on.";
                  } else if (lsReferredTo.getFuel() > 0) {
                        lsReferredTo.toggleLight();
                        String msg = "The " + lightSourceName + " casts light around you.";
                        if (!GameState.instance().getAdventurersCurrentRoom().isLit()) {
                              msg += "\n" + GameState.instance().getAdventurersCurrentRoom().describe();
                        }
                        return msg;
                  } else { 
                        return "The " + lightSourceName + " has no fuel.";
                  }
            } else if (toggleState.equals("off")) {
                  if (!lsReferredTo.isLit()) {
                        return "The " + lightSourceName + " is already off.";
                  } else {
                        lsReferredTo.toggleLight();
                        return "The " + lightSourceName + " stops lighting up the room.";
                  }
            } else {
                  return "You can not " + toggleState + " the " + lightSourceName + ".";
            }
                              
       }
}
