import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import objects.BlobObject;
import objects.GitObject;

public class MiniGit {

    public static final String CMD_INIT = "init";

    public static final String CMD_HASH_OBJECT_WRITE = "hash-object";

    public static final String CMD_CAT_FILE = "cat-file";

    public static final String CMD_CHECKOUT = "checkout";

    public static final Repository repository = new Repository();

    public static void main(String[] args) throws Exception {
        switch (args[0]) {
            case CMD_INIT:
            case CMD_HASH_OBJECT_WRITE:
                hashObjectWrite(args[1]);
                break;
            case CMD_CAT_FILE:
                catFilePretty(args[1]);
                break;
            case CMD_CHECKOUT:
            checkout(args[1]);
                break;
            default:
                break;
        }
    }

    public static void hashObjectWrite(String filename) throws IOException {
        final var input = Files.readAllBytes(Paths.get(filename));
        final var blob = new BlobObject(input);
        final var hash = blob.getHash();

        repository.save(hash, blob.getBytes());
    }

    public static void catFilePretty(String hash) throws IOException {
        final GitObject object = repository.find(hash);
        System.out.println(object.getContent());
    }

    public static void checkout(String hash) throws IOException {
        final GitObject object = repository.find(hash);
        System.out.println(object.getContent());
    }
}
