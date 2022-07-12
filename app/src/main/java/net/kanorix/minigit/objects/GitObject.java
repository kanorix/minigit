package net.kanorix.minigit.objects;

import java.nio.charset.StandardCharsets;

import net.kanorix.minigit.utils.ByteArrayUtil;

public abstract class GitObject {

    private byte[] body;

    public GitObject(byte[] body) {
        this.body = body;
    }

    public abstract String getType();

    public int getSize() {
        return body.length;
    }

    public byte[] getBody() {
        return body;
    }

    public byte[] getBytes() {
        return ByteArrayUtil.concat(
                "%s %d\0".formatted(getType(), getSize()).getBytes(StandardCharsets.UTF_8),
                body);
    }

    public String toString() {
        final var format = """
                ----------------------------------------------
                hash: %s
                type: %s
                size: %d
                ----------------------------------------------
                %s""";
        return String.format(
                format,
                ByteArrayUtil.getHash(getBytes()),
                getType(),
                getSize(),
                ByteArrayUtil.toString(body));
    }
}
