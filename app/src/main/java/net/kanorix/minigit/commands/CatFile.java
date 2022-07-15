package net.kanorix.minigit.commands;

import java.util.concurrent.Callable;

import net.kanorix.minigit.objects.GitObject;
import net.kanorix.minigit.utils.GitRepositoryUtil;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "cat-file")
public class CatFile implements Callable<Integer> {

    @Option(names = "--help", usageHelp = true, description = "display this help and exit")
    private boolean help;

    @Option(names = "-t", description = "Show the object type.")
    private boolean type;

    @Option(names = "-s", description = "Show the object size.")
    private boolean size;

    @Option(names = "-p", description = "Pretty-print objects content.")
    private boolean pretty;

    @Parameters()
    private String object;

    @Override
    public Integer call() throws Exception {
        final GitObject gitObject = GitRepositoryUtil.find(object);

        if (type) {
            System.out.println(gitObject.getType());
            return 0;
        }
        if (size) {
            System.out.println(gitObject.toBytes().length);
            return 0;
        }
        if (pretty) {
            System.out.println(gitObject);
            return 0;
        }
        System.out.println(gitObject.inspect());
        return 0;
    }
}
