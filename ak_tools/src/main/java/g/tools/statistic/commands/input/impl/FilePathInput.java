package g.tools.statistic.commands.input.impl;

import g.tools.statistic.commands.Command;
import g.tools.statistic.commands.input.InputProcessorWithScanner;
import g.tools.statistic.models.State;

import java.util.Scanner;

public class FilePathInput extends InputProcessorWithScanner {

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
