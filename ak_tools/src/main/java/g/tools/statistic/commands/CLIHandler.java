package g.tools.statistic.commands;


import g.tools.statistic.models.Statistics;

import java.util.*;


public class CLIHandler {

    enum StepCode {
        UNKNOWN_COMMAND(1),
        OK(0),
        EXIT(-1);

        private final int code;

        StepCode(int code) {
            this.code = code;
        }
    }

    private String currentFilePath;
    private final Statistics statistics;
    private final Scanner scanner;
    private final StandardCommands standardCommands;
    private final ICommandWrapper commandsChain;


    public CLIHandler() {
        this.statistics = new Statistics();
        this.standardCommands = new StandardCommands(this.statistics);
        this.scanner = new Scanner(System.in);

        this.commandsChain = new FilePathInput();
        this.commandsChain
                .setNextCommand(new SaveNextInput())
                .setNextCommand(new AddRecordInput())
                .setNextCommand(new AddsRecordInput());
    }

    public void run() {
        System.out.println("Start the program...");
        System.out.println("Enter a command:");

        while (nextStep(null, null) != StepCode.EXIT);
    }

    private StepCode nextStep(Command inputCommand, Object sInput) {
        Command command = inputCommand == null ? Command.parse(scanner.nextLine()) : inputCommand;
        if (command == Command.EXIT) {
            return StepCode.EXIT;
        } else if (command == null) {
            return StepCode.UNKNOWN_COMMAND;
        }

        Object input = sInput;

        // TODO use chain of responsibilities pattern.
        if (input == null) {
            input = this.commandsChain.handle(command);
            //throw new RuntimeException("Unknown command: " + command);
//            System.out.println("Unknown command: " + command);
//            return StepCode.UNKNOWN_COMMAND;
        }

//        System.out.println(command + " ----> " + input);
        CommandExecutor commandExecutor = standardCommands.getCommands().get(command);
        commandExecutor.execute(input);
        System.out.println("Command " + command + " executed");

        return StepCode.OK;
    }

    // TODO Do something with it.

    private interface ICommandWrapper {
        Object handle(Command command);
        boolean canHandle(Command command);
        ICommandWrapper getNextCommand();
        ICommandWrapper setNextCommand(ICommandWrapper command);
    }

    private abstract class CommandWrapper implements ICommandWrapper {

        private ICommandWrapper commandInputWrapper;

        @Override
        public ICommandWrapper getNextCommand() {
            return this.commandInputWrapper;
        }

        @Override
        public ICommandWrapper setNextCommand(ICommandWrapper command) {
            this.commandInputWrapper = command;
            return this.commandInputWrapper;
        }

        @Override
        public Object handle(Command command) {
            Object res;
            if (canHandle(command)) {
                res = internalHandle(command);
            } else {
                ICommandWrapper nextChain = getNextCommand();
                res = nextChain == null ? null : nextChain.handle(command);
            }
            return res;
        }

        protected abstract Object internalHandle(Command command);
    }


    private class FilePathInput extends CommandWrapper {

        @Override
        public boolean canHandle(Command command) {
            return command == Command.SAVE_FILE
                    || command == Command.LOAD_FILE
                    || command == Command.READ_FILE;
        }

        @Override
        public Object internalHandle(Command command) {
            System.out.println("Enter file path: ");
            String filePath = scanner.nextLine();
            CLIHandler.this.currentFilePath = filePath;
            return filePath;
        }

    }

    private class SaveNextInput extends CommandWrapper {

        @Override
        public boolean canHandle(Command command) {
            return command == Command.SAVE_NEXT_FILE;
        }

        @Override
        public Object internalHandle(Command command) {
            return CLIHandler.this.currentFilePath;
        }

    }

    private class AddRecordInput extends CommandWrapper {

        @Override
        public boolean canHandle(Command command) {
            return command == Command.ADD_RECORD;
        }

        @Override
        public Object internalHandle(Command command) {
            return CLIHandler.this.scanner;
        }

    }

    private class AddsRecordInput extends CommandWrapper {

        @Override
        public boolean canHandle(Command command) {
            return command == Command.ADD_S;
        }

        @Override
        public Object internalHandle(Command command) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("scanner", scanner);
            parameters.put("filePath", CLIHandler.this.currentFilePath);
            return parameters;
        }

    }

}
