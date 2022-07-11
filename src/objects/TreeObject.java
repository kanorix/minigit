package objects;

import java.util.List;

public class TreeObject extends GitObject {

    private List<Entry> entries;

    public record Entry(int mode, String hash, String name) {}

    public TreeObject(byte[] body) {
        super(body);
        // for(byte[] bytes: ArrayUtils.split(body, (byte) 0)) {
        // }
    }

    public void addEntry(int mode, String hash, String name) {
        entries.add(new Entry(mode, hash, name));
    }

    @Override
    String getType() {
        return "tree";
    }
}
