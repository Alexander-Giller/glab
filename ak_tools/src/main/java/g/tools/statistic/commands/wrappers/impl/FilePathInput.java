package g.tools.statistic.commands.wrappers.impl;

import g.tools.statistic.commands.Command;
import g.tools.statistic.commands.wrappers.InputPreparatorWithScanner;
import g.tools.statistic.models.State;

import java.util.Scanner;

public class FilePathInput extends InputPreparatorWithScanner {

    public FilePathInput(State state, Scanner scanner) {
        super(state, scanner);
    }

    @Override
    public boolean canHandle(Command command) {
        return command == Command.SAVE_FILE
                || command == Command.LOAD_FILE
                || command == Command.READ_FILE;
    }

    @Override
    public Object internalHandle(Command command) {
        System.out.println("Enter file path: ");
        String filePath = getScanner().nextLine();
        getState().setCurrentFilePath(filePath);
        return filePath;
    }

}
