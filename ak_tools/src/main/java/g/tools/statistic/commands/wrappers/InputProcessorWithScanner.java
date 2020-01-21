package g.tools.statistic.commands.wrappers;


import g.tools.statistic.models.State;

import java.util.Scanner;


public abstract class InputProcessorWithScanner extends InputProcessor {

    private final Scanner scanner;

    public InputProcessorWithScanner(State state, Scanner scanner) {
        super(state);
        this.scanner = scanner;
    }

    public Scanner getScanner() {
        return scanner;
    }
}
