package g.tools.statistic.commands.input;

import g.tools.statistic.commands.Command;

public interface IInputProcessor {

    Object handle(Command command);

    boolean canHandle(Command command);

    IInputProcessor getNextCommand();

    IInputProcessor setNextCommand(IInputProcessor command);

}
