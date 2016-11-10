
package bork;

import java.util.List;
import java.util.Arrays;

/**
 * A singleton class whose main function is to parse commands and create a new 
 * command object of the correct type.
 * 
 * @author Dr. Zeitz
 */
public class CommandFactory {

    private static CommandFactory theInstance;
    public static List<String> MOVEMENT_COMMANDS = 
        Arrays.asList("n","w","e","s","u","d" );
    
    /**
     * A method that creates a CommandFactory instance or gets the instance if 
     * it already exists.
     * @return the CommandFactory instance
     */
    public static synchronized CommandFactory instance() {
        if (theInstance == null) {
            theInstance = new CommandFactory();
        }
        return theInstance;
    }
    
    /**
     * Default CommandFactory constructor.
     */
    private CommandFactory() {
    }
    
    /**
     * Parses the string from the user and creates the correct Command object.
     * @param command the command string entered by the user
     * @return a new Command object of the correct type
     */
    public Command parse(String command) {
        String parts[] = command.split(" ");
        String verb = parts[0];
        String noun = parts.length >= 2 ? parts[1] : "";
        if (verb.equals("save")) {
            return new SaveCommand(noun);
        }
        if (verb.equals("take")) {
            return new TakeCommand(noun);
        }
        if (verb.equals("drop")) {
            return new DropCommand(noun);
        }
        if (verb.equals("i") || verb.equals("inventory")) {
            return new InventoryCommand();
        }
        if (MOVEMENT_COMMANDS.contains(verb)) {
            return new MovementCommand(verb);
        }
        if (parts.length == 2) {
            return new ItemSpecificCommand(verb, noun);
        }
        return new UnknownCommand(command);
    }
}
