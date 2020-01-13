package g.tools.statistic.commands;

import g.tools.statistic.models.Statistics;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CLIHandler {

    enum StepCode {
        OK(0),
        EXIT(-1);

        private final int code;

        StepCode(int code) {
            this.code = code;
        }
    }

    private String currentFilePath;
    Statistics statistics = new Statistics();
    private StandardCommands standardCommands = new StandardCommands(statistics);

    public CLIHandler() {
    }

    public void run() {
        System.out.println("Start the program...");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a command:");

        while (nextStep(scanner, null, null) != StepCode.EXIT);
    }

    private StepCode nextStep(Scanner scanner, Command inputCommand, Object sInput) {
        Command command = inputCommand == null ? Command.parse(scanner.nextLine()) : inputCommand;
        if (command == Command.EXIT) {
            return StepCode.EXIT;
        }
        Object input = sInput;

        // TODO use chain of responsibilities pattern.
        if (input == null) {
            switch (command) {
                case SAVE_FILE:
                case READ_FILE:
                    String filePath = getFilePathFromCLI(scanner);
                    this.currentFilePath = filePath;
                    input = filePath;
                    break;
                case SAVE_NEXT_FILE:
                    input = this.currentFilePath;
                    break;
                case ADD_RECORD:
                    input = scanner;
                    break;
                case ADD_S:
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("scanner", scanner);
                    parameters.put("filePath", this.currentFilePath);
                    input = parameters;
                    break;
                default:
                    throw new RuntimeException("Unknown command: " + command);
            }
        }

        runCommand(command, input);
        System.out.println("Command " + command + " executed");

        return StepCode.OK;
    }

    private String getFilePathFromCLI(Scanner scanner) {
        System.out.println("Enter file path: ");
        String filePath = scanner.nextLine();
        return filePath;
    }

    private Object runCommand(Command command, Object input) {
        CommandExecutor commandExecutor = standardCommands.getCommands().get(command);
        return commandExecutor.execute(input);
    }

}
