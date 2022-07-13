package net.kanorix.minigit.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import net.kanorix.minigit.objects.BlobObject;
import net.kanorix.minigit.objects.GitObject;
import net.kanorix.minigit.utils.ByteArrayUtil;
import net.kanorix.minigit.utils.GitRepositoryUtil;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "hash-object")
public class HashObject implements Callable<Integer> {

    @Option(names = "--help", usageHelp = true, description = "display this help and exit")
    private boolean help;

    @Option(names = "-w", description = "Write the object into the object database.")
    private boolean write;

    @ArgGroup(exclusive = true, multiplicity = "1")
    private Input input;

    static class Input {
        @Option(names = "--stdin", description = "Read the object from stdin")
        private boolean stdin;

        @Parameters()
        private Path file;
    }

    @Override
    public Integer call() throws IOException {
        final byte[] content = input.stdin
                ? ByteArrayUtil.toByteArray(System.in)
                : ByteArrayUtil.toByteArray(input.file);

        final GitObject object = new BlobObject(content);
        System.out.println(ByteArrayUtil.getHash(object.getBytes()));

        if (write) {
            GitRepositoryUtil.save(object);
        }
        return 0;
    }
}
