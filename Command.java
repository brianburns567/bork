
package bork;

/**
 * An abstract class that lays the foundation for many Command subclasses.
 * @author Dr. Zeitz
 */
abstract class Command {
    
    /**
     * An abstract method implemented in Command subclasses whose purpose is to 
     * execute the command string from the user and update the GameState.
     * @return a String describing the state of the game following execution of 
     * the command
     */
    abstract String execute();

}
