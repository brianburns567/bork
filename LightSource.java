
package bork;

import java.util.*;

/**
 * Class that extends the item class for the items that can be used as light sources
 * and maintains the value specific for light sources
 * @author Woodruff
 */
public class LightSource extends Item {

      private boolean isLit;
      private int fuel;
      
      static class NoActiveLightSourceException extends Exception {}
      
      /**
       * Constructor for LightSources reads in from input scanner for values
       * @param s scanner object for the initialization of the light source
       * @throws NoItemException Signals that there is no Item left to create
       * @throws Dungeon.IllegalDungeonFormatException Signals that the format of the input file is incorrect
       */
      LightSource(Scanner s) throws NoItemException, Dungeon.IllegalDungeonFormatException
      {
          super(s);
          fuel = Integer.valueOf(s.nextLine());
          isLit = false;
          
          if (!s.nextLine().equals(Dungeon.SECOND_LEVEL_DELIM)) 
            throw new Dungeon.IllegalDungeonFormatException("No '" +
                    Dungeon.SECOND_LEVEL_DELIM + "' after light source.");
      }
      
      /**
       * Changes the state of the light source
       */
      public void toggleLight() {
          if (isLit)
              isLit = false;
          else
              isLit = true;
      }
      
      /**
       * Returns the amount of fuel left in the light source
       * @return int and int value of the number of turns/commands before the source is out of fuel
       */
      public int getFuel() {
          return fuel;
      }
      
      /**
       * Returns whether or not the source is currently lit
       * @return boolean the value of the state of the source
       */
      public boolean isLit() {
          return isLit;
      }
      
      /**
       * Decrements the remaining fuel in the light source.
       */
      public void reduceFuel() {
          fuel--;
      }
}
