package net.kanorix.minigit.objects;

import java.nio.charset.StandardCharsets;
import java.rmi.UnexpectedException;
import java.util.List;

import net.kanorix.minigit.utils.ByteArrayUtil;

public abstract class GitObject {

    /**
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

    public abstract String getType();

    public abstract byte[] getContent();

    public abstract String toString();

    public byte[] toBytes() {
        final byte[] content = getContent();
        return ByteArrayUtil.concat(
                "%s %d\0".formatted(getType(), content.length).getBytes(StandardCharsets.UTF_8),
                content);
    }

    public String inspect() {
        final var format = """
                ----------------------------------------------
                hash: %s
                type: %s
                size: %d
                ----------------------------------------------
                %s""";
        return String.format(
                format,
                ByteArrayUtil.hashOf(toBytes()),
                getType(),
                getContent().length,
                toString());
    }
}
