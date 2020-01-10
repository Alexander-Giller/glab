package g.tools.statistic.models;

import java.util.ArrayList;
import java.util.List;

public class Statistics {

    private List<Record> records = new ArrayList<>();

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
