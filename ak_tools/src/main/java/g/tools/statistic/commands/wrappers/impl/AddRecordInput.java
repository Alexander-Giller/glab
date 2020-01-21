package g.tools.statistic.commands.wrappers.impl;

import g.tools.statistic.commands.Command;
import g.tools.statistic.commands.wrappers.InputProcessorWithScanner;
import g.tools.statistic.models.State;

import java.util.Scanner;

public class AddRecordInput extends InputProcessorWithScanner {

    public AddRecordInput(State state, Scanner scanner) {
        super(state, scanner);
    }

    @Override
    public boolean canHandle(Command command) {
        return command == Command.ADD_RECORD;
    }

    @Override
    public Object internalHandle(Command command) {
        return getScanner();
    }

}
