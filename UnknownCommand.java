package bork;

/**
 * Handles commands that cannot be parsed to any other command type.
 * 
 * @author Dr. Zeitz
 */
class UnknownCommand extends Command {

    private String bogusCommand;

    /**
     * Constructs a new UnknownCommand object that stores the user's
     * illegitimate input commandString.
     * 
     * @param bogusCommand the user's input
     */
    UnknownCommand(String bogusCommand) {
        this.bogusCommand = bogusCommand;
    }

    /**
     * Informs the user that their command is unrecognized.
     * 
     * @return a string that informs the user that their command is unrecognized
     */
    String execute() {
        return "I'm not sure what you mean by \"" + bogusCommand + "\".\n";
    }
}
