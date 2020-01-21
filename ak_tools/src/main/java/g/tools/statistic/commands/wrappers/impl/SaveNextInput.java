package g.tools.statistic.commands.wrappers.impl;

import g.tools.statistic.commands.Command;
import g.tools.statistic.commands.wrappers.InputProcessor;
import g.tools.statistic.models.State;

public class SaveNextInput extends InputProcessor {

    public SaveNextInput(State state) {
        super(state);
    }

    @Override
    public boolean canHandle(Command command) {
        return command == Command.SAVE_NEXT_FILE;
    }

    @Override
    public Object internalHandle(Command command) {
        return this.getState().getCurrentFilePath();
    }

}
