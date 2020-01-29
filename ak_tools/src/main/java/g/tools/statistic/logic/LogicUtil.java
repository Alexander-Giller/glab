package g.tools.statistic.logic;


import g.tools.statistic.commands.wrappers.impl.HowMuch;
import g.tools.statistic.models.Record.*;
import g.tools.statistic.models.Record;
import g.tools.statistic.models.Statistics;

import java.util.List;

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
        RecordBuilder aggregatedRecord = RecordBuilder.builder()
                .setType(parameter.getName());

        for (Record currentRecord : statistics.getRecords()) {
            HowMuch.Parameter recordParameter = HowMuch.Parameter.parseParameter(currentRecord.getType());
            if (parameter == recordParameter) {
                aggregatedRecord.addHours(currentRecord.getTimeHours());
            }
        }

        return aggregatedRecord.build();
    }
}
