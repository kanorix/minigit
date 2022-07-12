package net.kanorix.minigit.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.UnexpectedException;
import java.util.zip.DataFormatException;

import net.kanorix.minigit.MiniGitException;
import net.kanorix.minigit.objects.BlobObject;
import net.kanorix.minigit.objects.GitObject;
import net.kanorix.minigit.objects.TreeObject;

public class GitRepositoryUtil {

    public static final Path BASE_DIR = Paths.get("mgit");

    public static final Path OBJECTS_DIR = BASE_DIR.resolve("objects");
    public static final Path REFS_DIR = BASE_DIR.resolve("refs");
    public static final Path HEADS_DIR = REFS_DIR.resolve("heads");

    public static void create() throws IOException {
        Files.createDirectories(OBJECTS_DIR);
        Files.createDirectories(HEADS_DIR);
    }

    public static void save(GitObject gitObject) throws IOException {
        final byte[] bytes = gitObject.getBytes();
        final String hash = ByteArrayUtil.getHash(bytes);

        final var dir = OBJECTS_DIR.resolve(hash.substring(0, 2));
        final var name = dir.resolve(hash.substring(2, hash.length()));

        if (!Files.exists(name)) {
            Files.createDirectories(dir);
            Files.createFile(name);
            Files.write(name, ByteArrayUtil.compress(bytes));
        }
    }

    public static GitObject find(String hash) throws MiniGitException {
        if (hash.length() < 2) {
            throw new MiniGitException("fatal: Not valid object name " + hash);
        }
        final var dir = hash.substring(0, 2);
        final var name = hash.substring(2, hash.length());

        try {
            final var targetDir = OBJECTS_DIR.resolve(dir);
            final var target = OBJECTS_DIR.resolve(Paths.get(dir, name));
            final var files = Files.walk(targetDir)
                    .filter(fp -> !fp.equals(targetDir))
                    .toList();

            final var path = files.size() == 1
                    ? files.get(0)
                    : target;
            final var bytes = ByteArrayUtil.decompress(ByteArrayUtil.toByteArray(path));
            return from(bytes);
        } catch (IOException | DataFormatException e) {
            throw new MiniGitException("not found");
        }
    }

    public static GitObject from(byte[] input) throws UnexpectedException {
        final int index = ArrayUtils.findIndex(input, (byte) 0).orElseThrow();
        final String header = new String(ArrayUtils.subsequence(input, 0, index), StandardCharsets.UTF_8);
        final byte[] content = ArrayUtils.subsequence(input, index + 1, input.length);
        final String type = header.split(" ")[0];

        switch (type) {
            case "blob":
                return new BlobObject(content);
            case "commit":
                return new TreeObject(content);
            default:
                throw new UnexpectedException("Unknown git object type.");
        }
    }
}
