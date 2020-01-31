package g.tools.statistic;


import g.tools.statistic.commands.Command;
import g.tools.statistic.commands.CommandExecutor;
import g.tools.statistic.commands.StandardCommands;
import g.tools.statistic.commands.wrappers.*;
import g.tools.statistic.commands.wrappers.impl.*;
import g.tools.statistic.models.State;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

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


    public CLIHandler() {
        this.scanner = new Scanner(System.in);
        this.state = new State();
        this.standardCommands = new StandardCommands();

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
            // TODO
//            command = Command.parse(args);
        } else {
            command = inputCommand;
        }

        if (command == Command.EXIT || command == Command.EXIT_SHORT) {
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
            options.addOption("", false, "");
        }
    }

}
