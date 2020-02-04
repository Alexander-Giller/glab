package g.tools.statistic.commands.input.impl;

import g.tools.statistic.commands.Command;
import g.tools.statistic.commands.input.InputProcessor;
import g.tools.statistic.models.State;

public class HowMuchAll extends InputProcessor {

    public HowMuchAll(State state) {
        super(state);
    }

    @Override
    public boolean canHandle(Command command) {
        return command == Command.HOW_MUCH || command == Command.ALL;
    }

    @Override
    public Object internalHandle(Command command) {
        return null;
    }

}
