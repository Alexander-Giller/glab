package g.tools.statistic.commands.wrappers;

import g.tools.statistic.commands.Command;
import g.tools.statistic.models.State;

public abstract class InputProcessor implements IInputProcessor {

    private IInputProcessor commandInputWrapper;
    private final State state;

    public InputProcessor(State state) {
        this.state = state;
    }

    @Override
    public IInputProcessor getNextCommand() {
        return this.commandInputWrapper;
    }

    @Override
    public IInputProcessor setNextCommand(IInputProcessor command) {
        this.commandInputWrapper = command;
        return this.commandInputWrapper;
    }

    @Override
    public Object handle(Command command) {
        Object res;
        if (canHandle(command)) {
            res = internalHandle(command);
        } else {
            IInputProcessor nextChain = getNextCommand();
            res = nextChain == null ? null : nextChain.handle(command);
        }
        return res;
    }

    protected State getState() {
        return state;
    }

    protected abstract Object internalHandle(Command command);
}
