package g.tools.statistic.commands.wrappers.impl;

import g.tools.statistic.commands.Command;
import g.tools.statistic.commands.wrappers.InputPreparatorWithScanner;
import g.tools.statistic.models.State;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AddsRecordInput extends InputPreparatorWithScanner {

    public AddsRecordInput(State state, Scanner scanner) {
        super(state, scanner);
    }

    @Override
    public boolean canHandle(Command command) {
        return command == Command.ADD_S;
    }

    @Override
    public Object internalHandle(Command command) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("scanner", getScanner());
        parameters.put("filePath", getState().getCurrentFilePath());
        return parameters;
    }

}
