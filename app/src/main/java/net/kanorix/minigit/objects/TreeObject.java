package net.kanorix.minigit.objects;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.kanorix.minigit.utils.ByteArrayUtil;

public class TreeObject extends GitObject {

    private List<Entry> entries = new ArrayList<>();

    public record Entry(int mode, String name, String hash) {
        public byte[] getContent() {
            return ByteArrayUtil.concat(
                    "%d %s\0".formatted(mode, name).getBytes(StandardCharsets.UTF_8),
                    ByteArrayUtil.toHexBytes(hash));
        }
    }

    /**
     * Treeオブジェクトを作成
     *
     * @param input 内容バイト列
     */
    public TreeObject(byte[] input) {
        int index = 0;
        while (index < input.length) {
            final int begin = index;

            // [ ]を探す（モードと名前の区切り）
            while (input[index++] != 0x20);
            final int nameIdx = index;

            // [\0]を探す（名前の終わり）
            while (input[index++] != 0x00);
            final int sha1Idx = index;

            // Hashは20文字
            final int end = (index += 20);

            final int mode = Integer.parseInt(ByteArrayUtil.subString(input, begin, nameIdx - 1));
            final String name = ByteArrayUtil.subString(input, nameIdx, sha1Idx - 1);
            final byte[] sha1bytes = ByteArrayUtil.subArray(input, sha1Idx, end);

            entries.add(new Entry(mode, name, ByteArrayUtil.toHexString(sha1bytes)));
        }
    }

    @Override
    public String getType() {
        return "tree";
    }

    @Override
    public byte[] getContent() {
        return entries.stream()
                .map(e -> e.getContent())
                .reduce(new byte[0], ByteArrayUtil::concat);
    }

    @Override
    public String toString() {
        return entries.stream()
                .map(e -> "%d %s %s".formatted(e.mode, e.name, e.hash))
                .collect(Collectors.joining("\n"));
    }
}
