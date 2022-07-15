package net.kanorix.minigit.objects;

import net.kanorix.minigit.utils.ByteArrayUtil;

public class BlobObject extends GitObject {

    private byte[] content;

    public BlobObject(final byte[] content) {
        this.content = content;
    }

    @Override
    public String getType() {
        return "blob";
    }

    @Override
    public byte[] getContent() {
        return content;
    }

    @Override
    public String toString() {
        return ByteArrayUtil.toString(content);
    }
}
