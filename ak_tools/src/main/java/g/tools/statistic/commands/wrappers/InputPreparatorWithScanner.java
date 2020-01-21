package g.tools.statistic.commands.wrappers;


import g.tools.statistic.models.State;

import java.util.Scanner;


public abstract class InputPreparatorWithScanner extends InputPreparator {

    private final Scanner scanner;

    public InputPreparatorWithScanner(State state, Scanner scanner) {
        super(state);
        this.scanner = scanner;
    }

    public Scanner getScanner() {
        return scanner;
    }
}
