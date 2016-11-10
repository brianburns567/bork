
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
      
      /**
       * Constructor for LightSources reads in from input scanner for values
       * @param s scanner object for the initialization of the light source
       * @throws NoItemException Signals that there is no Item left to create
       * @throws Dungeon.IllegalDungeonFormatException Signals that the format of the input file is incorrect
       */
      LightSource(Scanner s) throws NoItemException, Dungeon.IllegalDungeonFormatException
      {
          super(s);
      }
      
      /**
       * Changes the state of the light source
       */
      public void toggleLight() {}
      
      /**
       * Returns the amount of fuel left in the light source
       * @return int and int value of the number of turns/commands before the source is out of fuel
       */
      public int getFuel() {return 0;}
      
      /**
       * Returns whether or not the source is currently lit
       * @return boolean the value of the state of the source
       */
      public boolean isLit() {return false;}
}
