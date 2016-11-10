package bork;

/**
 * Handles the save command.
 * 
 * @author Dr. Zeitz
 */
class SaveCommand extends Command {

    private static String DEFAULT_SAVE_FILENAME = "bork";

    private String saveFilename;

    /**
     * Constructs a new SaveCommand object with a file name to be executed.
     * 
     * @param saveFilename the name of the file to save to
     */
    SaveCommand(String saveFilename) {
        if (saveFilename == null || saveFilename.length() == 0) {
            this.saveFilename = DEFAULT_SAVE_FILENAME;
        } else {
            this.saveFilename = saveFilename;
        }
    }

    /**
     * Saves the current GameState to a file with the given file name.
     * 
     * @return whether the save was successful or not
     */
    public String execute() {
        try {
            GameState.instance().store(saveFilename);
            return "Data saved to " + saveFilename +
                GameState.SAVE_FILE_EXTENSION + ".\n";
        } catch (Exception e) {
            System.err.println("Couldn't save!");
            e.printStackTrace();
            return "";
        }
    }
}
