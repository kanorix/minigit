package net.kanorix.minigit.commands;

import net.kanorix.minigit.MiniGitException;
import net.kanorix.minigit.objects.GitObject;
import net.kanorix.minigit.utils.ByteArrayUtil;
import net.kanorix.minigit.utils.GitRepositoryUtil;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

@Command(name = "cat-file")
public class CatFile implements Runnable {

    @Spec
    private CommandSpec spec;

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
    public void run() {
        try {
            final GitObject gitObject = GitRepositoryUtil.find(object);

            if (type) {
                System.out.println(gitObject.getType());
                return;
            }
            if (size) {
                System.out.println(gitObject.getSize());
                return;
            }
            if (pretty) {
                System.out.println(ByteArrayUtil.toString(gitObject.getBody()));
                return;
            }
            System.out.println(gitObject.toString());
            return;

        } catch (MiniGitException e) {
            e.print();
            return;
        }
    }
}
