package g.tools.statistic;

import g.tools.statistic.commands.CLIHandler;
import java.io.IOException;

public class GStatistics {

    public static void main(String[] args) throws IOException {
        System.out.println("Start...");
        CLIHandler cliHandler = new CLIHandler();
        cliHandler.run();
        System.out.println("Complete");
    }

}
