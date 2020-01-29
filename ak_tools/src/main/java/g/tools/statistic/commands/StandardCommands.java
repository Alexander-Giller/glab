package g.tools.statistic.commands;


import g.tools.statistic.commands.wrappers.impl.HowMuch;
import g.tools.statistic.logic.LogicUtil;
import g.tools.statistic.models.Record;
import g.tools.statistic.models.Statistics;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class StandardCommands {

    private final Map<Command, CommandExecutor> commands = new HashMap<>();
    private final Statistics statistics = new Statistics();


    public StandardCommands() {
        commands.put(Command.ADD_RECORD, ADD_RECORD);
        commands.put(Command.SAVE_FILE, SAVE_FILE);
        commands.put(Command.READ_FILE, READ_FILE);
        commands.put(Command.LOAD_FILE, READ_FILE);
        commands.put(Command.ADD_S, ADD_S);
        commands.put(Command.DAYS, DAYS);
        commands.put(Command.SAVE_NEXT_FILE, SAVE_NEXT_FILE);
        commands.put(Command.HOW_MUCH, HOW_MUCH);
    }

    public final CommandExecutor DAYS = (input) -> {
        long diffDays = LogicUtil.getStartEndDays(this.statistics);
        System.out.println("Total days: " + diffDays);
        return diffDays;
    };

    public final CommandExecutor HOW_MUCH = (input) -> {
        HowMuch.Parameter parameter = (HowMuch.Parameter) input;
        Record aggregatedRecord = LogicUtil.calculateStatistic(this.statistics, parameter);
        System.out.println(aggregatedRecord);
        return null;
    };

    public final CommandExecutor ADD_RECORD = (input) -> {
        Scanner in = (Scanner)input;
        try {
            Record record = inputRecord(in);
            statistics.getRecords().add(record);
        } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    };

    public final CommandExecutor SAVE_FILE = (input) -> {
        String fileName = (String)input;

        try (FileWriter out = new FileWriter(fileName);
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(Record.HEADER))) {

                for (Record record : statistics.getRecords()) {
                    printer.printRecord(record.getAsStringArray());
                }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    };

    public final CommandExecutor READ_FILE = (input) -> {
        String fileName = (String)input;

        try (Reader in = new FileReader(fileName)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader(Record.HEADER)
                    .withFirstRecordAsHeader()
                    .parse(in);
            Record.RecordBuilder builder = Record.RecordBuilder.builder();

            for (CSVRecord record : records) {
                Record modelRecord = builder
                        .setComment(record.get(Record.COMMENT))
                        .setDate(Instant.parse(record.get(Record.DATE)))
                        .setType(record.get(Record.TYPE))
                        .setDescription(record.get(Record.DESCRIPTION))
                        .setTimeHours(Double.valueOf(record.get(Record.TIME_HOURS)))
                        .build();
                statistics.getRecords().add(modelRecord);
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    };

    public final CommandExecutor ADD_S = (input) -> {
        Map<String, Object> parameters = (Map)input;
        ADD_RECORD.execute(parameters.get("scanner"));
        SAVE_FILE.execute(parameters.get("filePath"));
        return null;
    };

    public final CommandExecutor SAVE_NEXT_FILE = (input) -> {
        String fileName = (String)input;

        int i = fileName.length();
        while (true) {
            if (i == 0) {
                break;
            }
            if (Character.isDigit(fileName.charAt(i - 1))) {
                --i;
            } else {
                break;
            }
        }
        String name = fileName.substring(0, i);
        int digit = Integer.valueOf(fileName.substring(i));
        digit++;
        SAVE_FILE.execute(name + digit);
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

    public Map<Command, CommandExecutor> getCommands() {
        return this.commands;
    }

}
