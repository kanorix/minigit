package net.kanorix.minigit.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArrayUtils {

    public static byte[] concat(byte[] b1, byte[] b2) {
        final byte[] joined = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, joined, 0, b1.length);
        System.arraycopy(b2, 0, joined, b1.length, b2.length);
        return joined;
    }

    public static Optional<Integer> findIndex(byte[] bytes, byte query) {
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == query) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public static List<byte[]> split(byte[] bytes, byte query) {
        final List<byte[]> splitted = new ArrayList<>();
        int start = 0;
        int end = bytes.length;

        for (;;) {
            final Optional<Integer> index = findIndex(bytes, query);
            if (index.isEmpty()) {
                splitted.add(bytes);
                break;
            }
            splitted.add(subsequence(bytes, start, index.get()));
            start = index.get();
            bytes = subsequence(bytes, start, end);
        }
        return splitted;
    }

    public static byte[] subsequence(byte[] bytes, int start, int end) {
        final byte[] sequence = new byte[end - start];
        System.arraycopy(bytes, start, sequence, 0, end - start);
        return sequence;
    }
}
