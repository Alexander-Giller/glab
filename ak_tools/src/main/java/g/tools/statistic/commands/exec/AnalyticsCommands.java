package g.tools.statistic.commands.exec;


import g.tools.statistic.commands.Command;
import g.tools.statistic.commands.CommandExecutor;
import g.tools.statistic.commands.input.impl.HowMuch;
import g.tools.statistic.logic.LogicUtil;
import g.tools.statistic.models.Record;
import g.tools.statistic.models.Statistics;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class AnalyticsCommands {

    private final Map<Command, CommandExecutor> commands = new HashMap<>();
    private Statistics statistics;


    public AnalyticsCommands(Statistics statistics) {
        this.statistics = statistics;
        commands.put(Command.ADD_RECORD, ADD_RECORD);
        commands.put(Command.ADD_S, ADD_S);
        commands.put(Command.DAYS, DAYS);
        commands.put(Command.HOW_MUCH, HOW_MUCH);
        commands.put(Command.HOW_MUCH_ALL, HOW_MUCH_ALL);
        commands.put(Command.ALL, HOW_MUCH_ALL);
    }

    public Map<Command, CommandExecutor> getCommands() {
        return commands;
    }

    private final CommandExecutor DAYS = (input) -> {
        long diffDays = LogicUtil.getStartEndDays(this.statistics);
        System.out.println("Total days: " + diffDays);
        return diffDays;
    };

    private final CommandExecutor HOW_MUCH = (input) -> {
        HowMuch.Parameter parameter = (HowMuch.Parameter) input;
        Record aggregatedRecord = LogicUtil.calculateStatistic(this.statistics, parameter);
        System.out.println(aggregatedRecord);
        return null;
    };

    private final CommandExecutor HOW_MUCH_ALL = (input) -> {
        List<Record> aggregatedRecord = LogicUtil.calculateAllStatistic(this.statistics);
        aggregatedRecord.forEach(System.out::println);
        return null;
    };

    private final CommandExecutor ADD_RECORD = (input) -> {
        Scanner in = (Scanner)input;
        try {
            Record record = inputRecord(in);
            statistics.getRecords().add(record);
        } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    };

    private final CommandExecutor ADD_S = (input) -> {
        Map<String, Object> parameters = (Map)input;
        ADD_RECORD.execute(parameters.get("scanner"));
        commands.get(Command.SAVE_FILE).execute(parameters.get("filePath"));
        return null;
    };

    private static Record inputRecord(Scanner in) {
        // public static String[] HEADER = {DATE, TYPE, TIME_HOURS, DESCRIPTION, COMMENT};
        // Default value
        String instant = String.valueOf(Instant.now());
        System.out.println("type + time hours + description");
        String type = in.nextLine();
        String timeHours = in.nextLine();
        String description = in.nextLine();
        String comment = null;

        String values[] = {
                instant,
                type,
                timeHours,
                description,
                comment};

        return Record.RecordBuilder.builder(values).build();
    }

}

