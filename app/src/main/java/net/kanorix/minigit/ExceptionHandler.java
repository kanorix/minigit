package net.kanorix.minigit;

import picocli.CommandLine;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.ParseResult;

public class ExceptionHandler implements IExecutionExceptionHandler {

    @Override
    public int handleExecutionException(
            Exception ex,
            CommandLine commandLine,
            ParseResult parseResult)
            throws Exception {

        final String message = ex.getClass().getName() + ": " + ex.getMessage();
        commandLine.getErr().println(commandLine.getColorScheme().errorText(message));

        return commandLine.getCommandSpec().exitCodeOnExecutionException();
    }
}
