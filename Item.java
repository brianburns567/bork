
package bork;

import java.util.Scanner;
import java.util.Hashtable;

/**
 * Class that contains all the information about an Item object
 * @author Dr. Zeitz
 */
public class Item {

    static class NoItemException extends Exception {}

    private String primaryName;
    private int weight;
    private Hashtable<String,String> messages;
    
    /**
     * Constructor for Item objects reads in from the input scanner for the values of each Item
     * @param s scanner object for initializing the items in the dungeon
     * @throws NoItemException Signals that there is no Item left to create
     * @throws Dungeon.IllegalDungeonFormatException Signals that the format of the input file being read is incorrect
     */
    Item(Scanner s) throws NoItemException,
        Dungeon.IllegalDungeonFormatException {

        messages = new Hashtable<String,String>();

        // Read item name.
        primaryName = s.nextLine();
        if (primaryName.equals(Dungeon.TOP_LEVEL_DELIM)) {
            throw new NoItemException();
        }

        // Read item weight.
        weight = Integer.valueOf(s.nextLine());

        // Read and parse verbs lines, as long as there are more.
        String verbLine = s.nextLine();
        while (!verbLine.equals(Dungeon.SECOND_LEVEL_DELIM)) {
            if (verbLine.equals(Dungeon.TOP_LEVEL_DELIM)) {
                throw new Dungeon.IllegalDungeonFormatException("No '" +
                    Dungeon.SECOND_LEVEL_DELIM + "' after item.");
            }
            String[] verbParts;
            if(verbLine.contains("[")){
                verbParts = verbLine.split("[");
            }else{
                verbParts = verbLine.split(":");
            }
            messages.put(verbParts[0],verbParts[1]);
            
            verbLine = s.nextLine();
        }
    }

    /**
     * Return true if the item's primary name equals the input string
     * @param name string name of an item
     * @return boolean returns whether the item's primary name is equal to the input or not
     */
    boolean goesBy(String name) {
        // could have other aliases
        return this.primaryName.equals(name);
    }

    /**
     * Returns the primary name of the item
     * @return String the primary name of the item
     */
    String getPrimaryName() { return primaryName; }

    /**
     * Returns the String message associated with the input parameter's verb
     * @param verb String for a verb referencing the hashtable of the item's messages
     * @return String the message associated with the verb if there is one
     */
    public String getMessageForVerb(String verb) {
        return messages.get(verb);
    }

    /**
     * Returns the string of the primary name
     * @return String the primary name of the item
     */
    @Override
    public String toString() {
        return primaryName;
    }
}
