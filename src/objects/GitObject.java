package objects;

import java.nio.charset.StandardCharsets;
import java.rmi.UnexpectedException;

import utils.ArrayUtils;
import utils.Sha1;

public abstract class GitObject {

    private byte[] body;

    public GitObject(byte[] body) {
        this.body = body;
    }


    public static GitObject from(byte[] input) throws UnexpectedException {
        final int index = ArrayUtils.findIndex(input, (byte) 0).orElseThrow();
        final String header = new String(ArrayUtils.subsequence(input, 0, index), StandardCharsets.UTF_8);
        final byte[] content = ArrayUtils.subsequence(input, index, input.length);
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

    abstract String getType();

    public byte[] getHeader() {
        return "%s %d\0".formatted(getType(), body.length).getBytes(StandardCharsets.UTF_8);
    }

    public byte[] getBytes() {
        return ArrayUtils.concat(getHeader(), body);
    }

    public String getHash() {
        return Sha1.digest(getBytes());
    }

    public String getContent() {
        return new String(body, StandardCharsets.UTF_8);
    }

    public String toString() {
        final var format = """
            ---
            hash: %s
            type: %s
            size: %d
            ---
            %s""";
        return String.format(
                format,
                getHash(),
                getType(),
                body.length,
                getContent());
    }
}
