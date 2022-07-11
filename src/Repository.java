import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import objects.GitObject;

public class Repository {

    public static final String BASE_DIR = "minigit";

    public static final Path REPOSITORY_DIR = Paths.get(BASE_DIR, "objects");

    public void save(String hash, byte[] object) throws IOException {

        final var dir = REPOSITORY_DIR.resolve(hash.substring(0, 2));
        final var name = dir.resolve(hash.substring(2, hash.length()));

        if (!Files.exists(name)) {
            Files.createDirectories(dir);
            Files.createFile(name);
            Files.write(name, object);
        }
    }

    public GitObject find(String hash) throws IOException {
        final var dir = hash.substring(0, 2);
        final var name = hash.substring(2, hash.length());
        final var path = REPOSITORY_DIR.resolve(Paths.get(dir, name));

        return GitObject.from(Files.readAllBytes(path));
    }
}
