package net.kanorix.minigit.commands;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import net.kanorix.minigit.objects.BlobObject;
import net.kanorix.minigit.objects.GitObject;
import net.kanorix.minigit.utils.ByteArrayUtil;
import net.kanorix.minigit.utils.GitRepositoryUtil;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

@Command(name = "hash-object", helpCommand = true)
public class HashObject implements Callable<Integer> {

    @Spec
    private CommandSpec spec;

    @Option(names = "-w", description = "Write the object into the object database.")
    private boolean write;

    @Option(names = "--stdin", description = "Read the object from stdin")
    private boolean stdin;

    @Parameters(paramLabel = "file")
    private Path path;

    @Override
    public Integer call() throws Exception {
        if (stdin || path != null) {
            final byte[] content = stdin
                    ? ByteArrayUtil.toByteArray(System.in)
                    : ByteArrayUtil.toByteArray(path);

            final GitObject object = new BlobObject(content);
            System.out.println(ByteArrayUtil.getHash(object.getBytes()));

            if (write) {
                GitRepositoryUtil.save(object);
            }
        }
        return 0;
    }
}
