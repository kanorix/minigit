package net.kanorix.minigit.commands;

import java.util.concurrent.Callable;

import net.kanorix.minigit.utils.GitRepositoryUtil;
import picocli.CommandLine.Command;

@Command(name = "init", helpCommand = true)
public class Init implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        GitRepositoryUtil.create();
        return 0;
    }
}
