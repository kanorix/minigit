package net.kanorix.minigit.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.DataFormatException;

import net.kanorix.minigit.objects.GitObject;

public class GitRepositoryUtil {

    public static final Path BASE_DIR = Paths.get("mgit");

    public static final Path OBJECTS_DIR = BASE_DIR.resolve("objects");
    public static final Path REFS_DIR = BASE_DIR.resolve("refs");
    public static final Path HEADS_DIR = REFS_DIR.resolve("heads");

    public static void create() throws IOException {
        Files.createDirectories(OBJECTS_DIR);
        Files.createDirectories(HEADS_DIR);
    }

    /**
     * Gitオブジェクトを保存します。
     *
     * @param gitObject Gitオブジェクト
     * @throws IOException IO例外
     */
    public static void save(GitObject gitObject) throws IOException {
        final byte[] bytes = gitObject.toBytes();
        final String hash = ByteArrayUtil.hashOf(bytes);

        final var dir = OBJECTS_DIR.resolve(hash.substring(0, 2));
        final var name = dir.resolve(hash.substring(2, hash.length()));

        if (!Files.exists(name)) {
            Files.createDirectories(dir);
            Files.createFile(name);
            Files.write(name, ByteArrayUtil.compress(bytes));
        }
    }

    /**
     * Gitオブジェクトを探します。
     *
     * @param hash ハッシュ値
     * @return Gitオブジェクト
     * @throws IOException IO例外
     * @throws DataFormatException データ形式例外
     */
    public static GitObject find(String hash) throws IOException, DataFormatException {
        final var dir = hash.substring(0, 2);
        final var name = hash.substring(2, hash.length());

        final var targetDir = OBJECTS_DIR.resolve(dir);
        final var files = Files.list(targetDir)
                .filter(fp -> name.isEmpty() || fp.toFile().getName().startsWith(name))
                .toList();

        final var path = files.size() == 1
                ? files.get(0)
                : targetDir.resolve(name);
        final var bytes = ByteArrayUtil.decompress(ByteArrayUtil.readAllBytes(path));
        return GitObject.from(bytes);
    }
}
