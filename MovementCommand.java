package bork;

/**
 * Handles the movement command.
 * 
 * @author Dr. Zeitz
 */
class MovementCommand extends Command {

    private String dir;
                       
    /**
     * Constructs a new directional Command object.
     * 
     * @param dir the direction the player desires to travel in
     */
    MovementCommand(String dir) {
        this.dir = dir;
    }
    
    /**
     * Checks to see if the player can move in the requested direction, updates
     * the GameState accordingly, and returns a message.
     * 
     * @return a description of the next room or a
     * notification that the user cannot go there
     */
    public String execute() {
        Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
        Room nextRoom = currentRoom.leaveBy(dir);
        if (nextRoom != null) {  // could try/catch here.
            GameState.instance().setAdventurersCurrentRoom(nextRoom);
            return "\n" + nextRoom.describe() + "\n";
        } else {
            return "You can't go " + dir + ".\n";
        }
    }
}
