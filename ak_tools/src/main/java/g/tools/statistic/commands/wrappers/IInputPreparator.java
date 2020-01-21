package g.tools.statistic.commands.wrappers;

import g.tools.statistic.commands.Command;

public interface IInputPreparator {

    Object handle(Command command);

    boolean canHandle(Command command);

    IInputPreparator getNextCommand();

    IInputPreparator setNextCommand(IInputPreparator command);


}
