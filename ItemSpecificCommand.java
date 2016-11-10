
package bork;

/**
 * Handles commands for specific items
 * @author Dr. Zeitz
 */
class ItemSpecificCommand extends Command {

    private String verb;
    private String noun;
    
    /**
     * Constructor for the ItemSpecificCommand with the input of String verb and noun
     * @param verb the verb refernced in the nouns messages
     * @param noun the string name of the item being refernced
     */
    ItemSpecificCommand(String verb, String noun) {
        this.verb = verb;
        this.noun = noun;
    }

    /**
     * Returns the String based on the item being referenced
     * @return String the message of the item, the message that the item isn't here, or the message that you can't use that verb on this item
     */
    public String execute() {
        
        Item itemReferredTo = null;
        try {
            itemReferredTo = GameState.instance().getItemInVicinityNamed(noun);
        } catch (Item.NoItemException e) {
            return "There's no " + noun + " here.";
        }
        
        String msg = itemReferredTo.getMessageForVerb(verb);
        return (msg == null ? 
            "Sorry, you can't " + verb + " the " + noun + "." : msg) + "\n";
    }
}
