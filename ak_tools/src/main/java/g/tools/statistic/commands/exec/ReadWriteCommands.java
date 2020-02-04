package g.tools.statistic.commands.exec;


import g.tools.statistic.commands.Command;
import g.tools.statistic.commands.CommandExecutor;
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


public class ReadWriteCommands {

    private final Map<Command, CommandExecutor> commands = new HashMap<>();
    private Statistics statistics;


    public ReadWriteCommands(Statistics statistics) {
        this.statistics = statistics;
        commands.put(Command.READ_FILE, READ_FILE);
        commands.put(Command.LOAD_FILE, READ_FILE);
        commands.put(Command.SAVE_FILE, SAVE_FILE);
        commands.put(Command.SAVE_NEXT_FILE, SAVE_NEXT_FILE);
    }

    public Map<Command, CommandExecutor> getCommands() {
        return commands;
    }


    private final CommandExecutor READ_FILE = (input) -> {
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

    private final CommandExecutor SAVE_FILE = (input) -> {
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

    private final CommandExecutor SAVE_NEXT_FILE = (input) -> {
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

}
