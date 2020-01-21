package g.tools.statistic.models;


import java.util.Scanner;


public class State {

    private String currentFilePath;
    private final Statistics statistics;

    public State() {
        this.statistics = new Statistics();
    }

    public String getCurrentFilePath() {
        return currentFilePath;
    }

    public void setCurrentFilePath(String currentFilePath) {
        this.currentFilePath = currentFilePath;
    }

    public Statistics getStatistics() {
        return statistics;
    }
}
