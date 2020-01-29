package g.tools.statistic.models;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Record {

    public static final String DATE = "date";
    public static final String TYPE = "type";
    public static final String TIME_HOURS = "timeHours";
    public static final String DESCRIPTION = "description";
    public static final String COMMENT = "comment";

    public static String[] HEADER = {DATE, TYPE, TIME_HOURS, DESCRIPTION, COMMENT};


    private Instant date;
    private String type;
    private Double timeHours;
    private String description;
    private String comment;

    private Record() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getTimeHours() {
        return timeHours;
    }

    public void setTimeHours(Double timeHours) {
        this.timeHours = timeHours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String[] getAsStringArray() {
        String[] values = {
                String.valueOf(date),
                String.valueOf(type),
                String.valueOf(timeHours),
                String.valueOf(description),
                String.valueOf(comment),
        };
        return values;
    }

    public Map<String, String> getAsMap() {
        String[] values = getAsStringArray();
        return getAsMap(values);
    }

    public static Map<String, String> getAsMap(String[] values) {
        if (HEADER.length != values.length) {
            throw new RuntimeException("Different length between header and values (" + HEADER.length + " -> " + values.length + ")");
        }
        Map<String, String> mapValues = new HashMap<>();
        for (int i = 0; i < HEADER.length; ++i) {
            mapValues.put(HEADER[i], values[i]);
        }

        return mapValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(type, record.type) &&
                Objects.equals(timeHours, record.timeHours) &&
                Objects.equals(description, record.description) &&
                Objects.equals(comment, record.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, timeHours, description, comment);
    }

    @Override
    public String toString() {
        return "Record{" +
                "date=" + date +
                ", type='" + type + '\'' +
                ", timeHours=" + timeHours +
                ", description='" + description + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }


    public static final class RecordBuilder {
        private Instant date;
        private String type;
        private Double timeHours;
        private String description;
        private String comment;

        private RecordBuilder() {
            this.timeHours = new Double(0);
        }

        private RecordBuilder(String[] values) {
            Map<String, String> mapValues = getAsMap(values);
            date = Instant.parse(mapValues.get(DATE));
            type = mapValues.get(TYPE);
            timeHours = Double.valueOf(mapValues.get(TIME_HOURS));
            description = mapValues.get(DESCRIPTION);
            comment = mapValues.get(COMMENT);
        }

        public static RecordBuilder builder() {
            return new RecordBuilder();
        }

        public static RecordBuilder builder(String[] values) {
            return new RecordBuilder(values);
        }

        public RecordBuilder setDate(Instant date) {
            this.date = date;
            return this;
        }

        public RecordBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public RecordBuilder setTimeHours(Double timeHours) {
            this.timeHours = timeHours;
            return this;
        }

        public RecordBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public RecordBuilder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public void addHours(Double hours) {
            this.timeHours += hours;
        }

        public Record build() {
            Record record = new Record();
            record.setDate(date);
            record.setType(type);
            record.setTimeHours(timeHours);
            record.setDescription(description);
            record.setComment(comment);
            return record;
        }
    }
}
