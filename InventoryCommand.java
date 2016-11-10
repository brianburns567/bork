
package bork;

import java.util.ArrayList;

/**
 * Handles the command to view the user's inventory
 * @author Dr. Zeitz
 */
class InventoryCommand extends Command {

    InventoryCommand() {
    }

    /**
     * Returns the String constructed based on the user's current inventory
     * @return String the names of the items in the user's inventory or the message that there are no items
     */
    public String execute() {
        ArrayList<String> names = GameState.instance().getInventoryNames();
        if (names.size() == 0) {
            return "You are empty-handed.\n";
        }
        String retval = "You are carrying:\n";
        for (String itemName : names) {
            retval += "   A " + itemName + "\n";
        }
        return retval;
    }
}
