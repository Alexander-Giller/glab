package g.tools.statistic.commands;


public enum Command {

    ADD_RECORD("add"),
    HOW_MUCH("howmuch"),
    HOW_MUCH_ALL("howmuchall"),
    ALL("all"),
    SAVE_FILE("save", true),
    SAVE_NEXT_FILE("savenext"),
    READ_FILE("read", true),
    LOAD_FILE("load", true),
    ADD_S("adds"),
    DAYS("days"),
    EXIT("exit");

    private final String name;
    private final boolean hasArg;

    Command(String name) {
        this.name = name;
        this.hasArg = false;
    }

    Command(String name, boolean hasArg) {
        this.name = name;
        this.hasArg = hasArg;
    }

    public static Command parse(String command) {
        String normalizedCommand = command.trim().toLowerCase();
        for (Command value : Command.values()) {
            if (value.name.equals(normalizedCommand)) {
                return value;
            }
        }
        System.out.println("Unknown command: " + command);
        return null;
    }

    public String getName() {
        return this.name;
    }

    public boolean hasArg() {
        return this.hasArg;
    }

}
