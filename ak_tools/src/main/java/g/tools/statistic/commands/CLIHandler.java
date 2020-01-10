package g.tools.statistic.commands;

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

public class CLIHandler {

    private static Statistics statistics = new Statistics();


    private static final CommandExecutor ADD_RECORD = (input) -> {
        Scanner in = (Scanner)input;
        try {
            Record record = inputRecord(in);
            statistics.getRecords().add(record);
        } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    };

    private static final CommandExecutor SAVE_FILE = (input) -> {
        String fileName = (String)input;
        try (FileWriter out = new FileWriter(fileName)) {
            try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(Record.HEADER))) {
                for (Record record : statistics.getRecords()) {
                    printer.printRecord(record.getAsStringArray());
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    };

    private static final CommandExecutor READ_FILE = (input) -> {
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


    private static final Map<Command, CommandExecutor> commands = new HashMap<>();
    static {
        commands.put(Command.ADD_RECORD, ADD_RECORD);
        commands.put(Command.SAVE_FILE, SAVE_FILE);
        commands.put(Command.READ_FILE, READ_FILE);
    }


    public CLIHandler() {
    }

    public void run() {
        System.out.println("Start the program...");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a command:");

        while (true) {
            String line = scanner.nextLine();
            Command command = Command.parse(line);
            if (command == Command.EXIT) {
                break;
            }

            CommandExecutor commandExecutor = commands.get(command);
            Object input;

            switch (command) {
                case SAVE_FILE:
                case READ_FILE:_FILE:
                    System.out.println("Enter file path: ");
                    input = scanner.nextLine();
                    break;
                case ADD_RECORD:
                    input = scanner;
                    break;
                default:
                    throw new RuntimeException("Unknown command: " + command);
            }
            commandExecutor.execute(input);
            System.out.println("Command " + command + " executed");
        }
    }

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
