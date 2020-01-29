package g.tools.statistic.commands.wrappers.impl;

import g.tools.statistic.commands.Command;
import g.tools.statistic.commands.wrappers.InputProcessorWithScanner;
import g.tools.statistic.models.State;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class HowMuch extends InputProcessorWithScanner {

    public enum Parameter {
        ACTIVITY("активность"),
        STREAM("стрим"),
        LEARN("учеба");

        private final String name;

        private final static List<String> activities = Arrays.asList("активность", "activity", "activities");
        private final static List<String> streams = Arrays.asList("stream", "stream", "стрим");
        private final static List<String> learns = Arrays.asList("учеба", "learn", "учёба", "обучение");


        Parameter(String name) {
            this.name = name;
        }

        public static Parameter parseParameter(String string) {
            Parameter res;
            if (activities.contains(string)) {
                res = ACTIVITY;
            } else if(streams.contains(STREAM)) {
                res = STREAM;
            } else if (learns.contains(LEARN)) {
                res = LEARN;
            } else {
                res = null;
            }
            return res;
        }

        public String getName() {
            return name;
        }
    }


    public HowMuch(State state, Scanner scanner) {
        super(state, scanner);
    }

    @Override
    public boolean canHandle(Command command) {
        return command == Command.HOW_MUCH;
    }

    @Override
    public Object internalHandle(Command command) {
        System.out.println("Enter parameter (" + Arrays.asList(Parameter.values()) + "): ");
        String sParameter = getScanner().nextLine();
        Parameter parameter = Parameter.parseParameter(sParameter);
        return parameter;
    }

}
