package net.kanorix.minigit;

import net.kanorix.minigit.commands.HashObject;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "minigit", version = "1.0", subcommands = { HashObject.class })
public class App {

    public static void main(String[] args) {
        final int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }
}