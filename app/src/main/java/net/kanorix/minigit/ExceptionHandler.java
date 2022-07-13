package net.kanorix.minigit;

import picocli.CommandLine;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.ParseResult;

public class ExceptionHandler implements IExecutionExceptionHandler {

    @Override
    public int handleExecutionException(Exception ex, CommandLine commandLine, ParseResult parseResult)
            throws Exception {

        commandLine.getErr()
                .println(commandLine.getColorScheme().errorText(ex.getClass().getName() + ": " + ex.getMessage()));

        return commandLine.getCommandSpec().exitCodeOnExecutionException();
    }
}
