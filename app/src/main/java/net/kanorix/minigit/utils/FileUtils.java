package net.kanorix.minigit.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.kanorix.minigit.objects.GitObject;

public class FileUtils {

    public static final String BASE_DIR = "minigit";

    public static final Path REPOSITORY_DIR = Paths.get(BASE_DIR, "objects");

    public static void save(String hash, byte[] object) throws IOException {

        final var dir = REPOSITORY_DIR.resolve(hash.substring(0, 2));
        final var name = dir.resolve(hash.substring(2, hash.length()));

        if (!Files.exists(name)) {
            Files.createDirectories(dir);
            Files.createFile(name);
            Files.write(name, object);
        }
    }

    public static GitObject find(String hash) throws IOException {
        final var dir = hash.substring(0, 2);
        final var name = hash.substring(2, hash.length());
        final var path = REPOSITORY_DIR.resolve(Paths.get(dir, name));

        return GitObject.from(Files.readAllBytes(path));
    }

    public static byte[] read(InputStream is) throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        return buffer.toByteArray();
    }
}
