package net.kanorix.minigit.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArrayUtils {

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
