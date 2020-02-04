package g.tools.statistic.commands.exec;


import g.tools.statistic.commands.Command;
import g.tools.statistic.commands.CommandExecutor;
import g.tools.statistic.models.Statistics;
import java.util.HashMap;
import java.util.Map;


public class StandardCommands {

    private final Map<Command, CommandExecutor> commands = new HashMap<>();
    private final Statistics statistics = new Statistics();


    public StandardCommands() {
        AnalyticsCommands analyticsCommands = new AnalyticsCommands(statistics);
        ReadWriteCommands readWriteCommands = new ReadWriteCommands(statistics);

        this.commands.putAll(analyticsCommands.getCommands());
        this.commands.putAll(readWriteCommands.getCommands());
    }


    public Map<Command, CommandExecutor> getCommands() {
        return this.commands;
    }

}
