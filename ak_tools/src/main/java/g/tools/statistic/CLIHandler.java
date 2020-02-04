package g.tools.statistic;


import g.tools.statistic.commands.Command;
import g.tools.statistic.commands.CommandExecutor;
import g.tools.statistic.commands.exec.StandardCommands;
import g.tools.statistic.commands.input.*;
import g.tools.statistic.commands.input.impl.*;
import g.tools.statistic.models.State;
import org.apache.commons.cli.*;

import java.util.*;


public class CLIHandler {

    public enum StepCode {

        UNKNOWN_COMMAND(1),
        OK(0),
        EXIT(-1);

        private final int code;

        StepCode(int code) {
            this.code = code;
        }

    }


    private final State state;
    private final Scanner scanner;
    private final StandardCommands standardCommands;
    private final IInputProcessor inputProcessorChain;
    private final GCLIParser cliParser;


    public CLIHandler() {
        this.scanner = new Scanner(System.in);
        this.state = new State();
        this.standardCommands = new StandardCommands();
        this.cliParser = new GCLIParser();

        this.inputProcessorChain = new FilePathInput(this.state, this.scanner);
        // It's important to use at first new and after attach another chains.
        this.inputProcessorChain.setNextCommand(new SaveNextInput(this.state))
                .setNextCommand(new AddRecordInput(this.state, this.scanner))
                .setNextCommand(new DaysInput(this.state))
                .setNextCommand(new HowMuch(this.state, this.scanner))
                .setNextCommand(new AddRecordInput(this.state, this.scanner))
                .setNextCommand(new AddsRecordInput(this.state, this.scanner));
    }

    public void run() {
        System.out.println("Start the program...");
        System.out.println("Enter a command:");

        while (nextStep(null, null) != StepCode.EXIT);
    }

    private StepCode nextStep(Command inputCommand, Object sInput) {

        Command command;
        if (inputCommand == null) {
            String line = this.scanner.nextLine();
            String args[] = line.split(" ");
            command = this.cliParser.parse(args);
        } else {
            command = inputCommand;
        }

        if (command == Command.EXIT) {
            return StepCode.EXIT;
        } else if (command == null) {
            return StepCode.UNKNOWN_COMMAND;
        }

        Object input = sInput;
        if (input == null) {
            input = this.inputProcessorChain.handle(command);
        }

        CommandExecutor commandExecutor = standardCommands.getCommands().get(command);
        commandExecutor.execute(input);
        System.out.println("Command " + command + " executed");

        return StepCode.OK;
    }


    private static class GCLIParser {

        private final CommandLineParser parser = new BasicParser();
        private final Options options = new Options();


        public GCLIParser() {
            Arrays.asList(Command.values()).forEach(command -> {
                options.addOption(command.getName(), command.hasArg(), command.getName());
            });
        }

        public Command parse(String[] args) {
            Command result;

            try {
                CommandLine commandLine = parser.parse(this.options, args);
                List<String> sCommands = Arrays.asList(commandLine.getArgs());
                Optional<String> firstCommand = sCommands.stream().findFirst();

                if (firstCommand.isPresent()) {
                    String sCommand = firstCommand.get();
                    result = Command.parse(sCommand);
                } else {
                    System.out.println("There is no any command in line");
                    result = null;
                }

            } catch (ParseException e) {
                System.out.println(e.getMessage());
                result = null;
            }

            return result;
        }
    }

}
