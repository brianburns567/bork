
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
    @Override
    public String execute() {
        
        Item itemReferredTo = null;
        try {
            itemReferredTo = GameState.instance().getItemInVicinityNamed(noun);
        } catch (Item.NoItemException e) {
            return "There's no " + noun + " here.";
        }
        
        String msg = itemReferredTo.getMessageForVerb(verb);
        if(msg == null)
        {
            return "Sorry, you can't " + verb + " the " + noun + ".\n";
        } else if (msg.contains("]"))
        {
            String[] eventMessage = msg.split(":");
            if(eventMessage[0].contains(","))
            {
                String[] events = eventMessage[0].split(",");
                for(String e : events)
                {
                    
                    if (e.contains("Transform"))
                    {
                        GameState.instance().transform(noun, e.substring(e.indexOf("(") + 1, e.indexOf(")")));
                        
                    } else if (e.contains("Wound"))
                    {
                        GameState.instance().wound(Integer.parseInt(e.substring(e.indexOf("(") + 1, e.indexOf(")"))));
                        
                    } else if (e.contains("Score"))
                    {
                        GameState.instance().addScore(Integer.parseInt(e.substring(e.indexOf("(") + 1, e.indexOf(")"))));
                        
                    } else if (e.contains("Die"))
                    {
                        GameState.instance().die();
                        
                    } else if (e.contains("Win"))
                    {
                        GameState.instance().win();
                        
                    } else if (e.contains("Disappear"))
                    {
                        GameState.instance().disappear(noun);
                        
                    } else if (e.contains("Teleport"))
                    {
                        GameState.instance().teleport();
                        
                    }
                }
                
            } else {
                if (eventMessage[0].contains("Transform"))
                {
                    GameState.instance().transform(noun, eventMessage[0].substring(eventMessage[0].indexOf("(") + 1, eventMessage[0].indexOf(")")));

                } else if (eventMessage[0].contains("Wound"))
                {
                    GameState.instance().wound(Integer.parseInt(eventMessage[0].substring(eventMessage[0].indexOf("(") + 1, eventMessage[0].indexOf(")"))));

                } else if (eventMessage[0].contains("Score"))
                {
                    GameState.instance().addScore(Integer.parseInt(eventMessage[0].substring(eventMessage[0].indexOf("(") + 1, eventMessage[0].indexOf(")"))));

                } else if (eventMessage[0].contains("Die"))
                {
                    GameState.instance().die();

                } else if (eventMessage[0].contains("Win"))
                {
                    GameState.instance().win();

                } else if (eventMessage[0].contains("Disappear"))
                {
                    GameState.instance().disappear(noun);

                } else if (eventMessage[0].contains("Teleport"))
                {
                    GameState.instance().teleport();

                }
                
            }
            
            return eventMessage[1];
        } else 
        {
            return msg;
        }
        
    }
}
