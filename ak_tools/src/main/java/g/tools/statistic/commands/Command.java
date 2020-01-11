package g.tools.statistic.commands;

public enum Command {

    ADD_RECORD("add"),
    SAVE_FILE("save"),
    SAVE_NEXT_FILE("savenext"),
    READ_FILE("read"),
    ADD_S("adds"),
    EXIT("exit");

    private final String name;

    Command(String name) {
        this.name = name;
    }

    public static Command parse(String command) {
        String normalizedCommand = command.trim().toLowerCase();
        for (Command value : Command.values()) {
            if (value.name.equals(normalizedCommand)) {
                return value;
            }
        }

        throw new RuntimeException("Unknown command: " + command);
    }
}
