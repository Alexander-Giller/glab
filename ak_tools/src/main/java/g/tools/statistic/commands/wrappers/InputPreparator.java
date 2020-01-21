package g.tools.statistic.commands.wrappers;

import g.tools.statistic.commands.Command;
import g.tools.statistic.models.State;

public abstract class InputPreparator implements IInputPreparator {

    private IInputPreparator commandInputWrapper;
    private final State state;

    public InputPreparator(State state) {
        this.state = state;
    }

    @Override
    public IInputPreparator getNextCommand() {
        return this.commandInputWrapper;
    }

    @Override
    public IInputPreparator setNextCommand(IInputPreparator command) {
        this.commandInputWrapper = command;
        return this.commandInputWrapper;
    }

    @Override
    public Object handle(Command command) {
        Object res;
        if (canHandle(command)) {
            res = internalHandle(command);
        } else {
            IInputPreparator nextChain = getNextCommand();
            res = nextChain == null ? null : nextChain.handle(command);
        }
        return res;
    }

    protected State getState() {
        return state;
    }

    protected abstract Object internalHandle(Command command);
}
