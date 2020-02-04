package g.tools.statistic.logic;


import g.tools.statistic.commands.input.impl.HowMuch;
import g.tools.statistic.models.Record.*;
import g.tools.statistic.models.Record;
import g.tools.statistic.models.Statistics;

import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;


public class LogicUtil {

    public static long getStartEndDays(Statistics statistics) {
        if (statistics == null || statistics.getRecords().isEmpty()) {
            System.out.println("Records is empty");
            return -1;
        }

        List<Record> records = statistics.getRecords();
        Record min = records.stream().min(LogicUtil::compareRecordByTime).get();
        Record max = records.stream().max(LogicUtil::compareRecordByTime).get();
        return DAYS.between(min.getDate(), max.getDate());
    }

    private static int compareRecordByTime(Record rec1, Record rec2) {
        return rec1.getDate().compareTo(rec2.getDate());
    }

    public static Record calculateStatistic(final Statistics statistics, final HowMuch.Parameter parameter) {
        if (parameter == null) {
            System.out.println("Input parameter for calculating statistic is null");
            return null;
        }

        RecordBuilder aggregatedRecord = RecordBuilder.builder().setType(parameter.getName());

        for (Record currentRecord : statistics.getRecords()) {
            HowMuch.Parameter recordParameter = HowMuch.Parameter.parseParameter(currentRecord.getType());
            if (parameter == recordParameter) {
                aggregatedRecord.addHours(currentRecord.getTimeHours());
            }
        }

        return aggregatedRecord.build();
    }

    public static List<Record> calculateAllStatistic(final Statistics statistics) {
        List<Record> resultRecords = new ArrayList();
        for (HowMuch.Parameter parameter : Arrays.asList(HowMuch.Parameter.values())) {
            resultRecords.add(calculateStatistic(statistics, parameter));
        }
        calculatePercents(resultRecords);
        return resultRecords;
    }

    private static List<Record> calculatePercents(final List<Record> inputRecords) {
        List<Record> records = inputRecords;

        double sumHours = records.stream()
                .mapToDouble(record -> record.getTimeHours())
                .sum();

        records.stream().forEach(record -> {
            Double timeHours = record.getTimeHours();
            double percent = Math.round(100.0 * timeHours / sumHours);
            record.setComment(percent + "%");
        });

        // From highest to lowest.
        Collections.sort(records, Comparator.comparingDouble(Record::getTimeHours));
        Collections.reverse(records);

        return records;
    }

}
