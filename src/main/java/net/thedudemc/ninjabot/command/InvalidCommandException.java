package net.thedudemc.ninjabot.command;

public class InvalidCommandException extends RuntimeException {

    private final String message;

    public InvalidCommandException(String name) {
        this.message = "The command you typed was not a valid command: " + name;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
