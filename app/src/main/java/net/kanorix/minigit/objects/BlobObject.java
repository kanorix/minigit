package net.kanorix.minigit.objects;

public class BlobObject extends GitObject {

    public BlobObject(final byte[] body) {
        super(body);
    }

    @Override
    public String getType() {
        return "blob";
    }
}
