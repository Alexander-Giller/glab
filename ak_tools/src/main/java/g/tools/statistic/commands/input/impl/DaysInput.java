package g.tools.statistic.commands.input.impl;

import g.tools.statistic.commands.Command;
import g.tools.statistic.commands.input.InputProcessor;
import g.tools.statistic.models.State;

public class DaysInput extends InputProcessor {

    public DaysInput(State state) {
        super(state);
    }

    @Override
    public boolean canHandle(Command command) {
        return command == Command.DAYS;
    }

    @Override
    public Object internalHandle(Command command) {
        return null;
    }

}
