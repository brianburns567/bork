
package bork;

/**
 *
 * @author Christopher Ringham
 */
public class LookCommand extends Command
{
    LookCommand()
    {
        
    }
    
    @Override
    public String execute()
    {
        return GameState.instance().getAdventurersCurrentRoom().lookDescribe();
    }
}
