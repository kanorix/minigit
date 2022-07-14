package net.kanorix.minigit.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.UnexpectedException;
import java.util.List;
import java.util.zip.DataFormatException;

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

    public static GitObject find(String hash) throws IOException, DataFormatException {
        final var dir = hash.substring(0, 2);
        final var name = hash.substring(2, hash.length());

        final var targetDir = OBJECTS_DIR.resolve(dir);
        final var files = Files.list(targetDir)
                .filter(fp -> name.isEmpty() || fp.toFile().getName().startsWith(name))
                .toList();

        final var path = files.size() == 1 ? files.get(0) : targetDir.resolve(name);
        final var bytes = ByteArrayUtil.decompress(ByteArrayUtil.readAllBytes(path));
        return from(bytes);
    }

    /**
     *
     * バイト配列からGitオブジェクトを生成します。
     *
     * @param input バイト配列
     * @return Gitオブジェクト
     * @throws UnexpectedException 予期しないオブジェクトタイプ
     */
    public static GitObject from(byte[] input) throws UnexpectedException {
        // '\0'の場所で半分にする
        final List<byte[]> bytesList = ByteArrayUtil.splitn(input, 2, (b) -> b == 0);

        // '\0'の前半がヘッダー
        final String header = new String(bytesList.get(0), StandardCharsets.UTF_8);

        // '\0'の前半が内容
        final byte[] content = bytesList.get(1);

        // ヘッダーの空白よりにあるタイプを取得
        final String type = header.split(" ")[0];

        // タイプによってオブジェクトを生成する
        switch (type) {
            case "blob":
                return new BlobObject(content);
            case "tree":
                return new TreeObject(content);
            default:
                throw new UnexpectedException("Unknown git object type.");
        }
    }
}
