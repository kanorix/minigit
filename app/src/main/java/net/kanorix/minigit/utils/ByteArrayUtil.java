package net.kanorix.minigit.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.function.Predicate;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ByteArrayUtil {

    public static byte[] readAllBytes(InputStream is) throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int count;
        byte[] data = new byte[1024];
        while ((count = is.read(data, 0, data.length)) > 0) {
            buffer.write(data, 0, count);
        }

        buffer.flush();
        return buffer.toByteArray();
    }

    public static byte[] readAllBytes(final Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    public static byte[] toByteArray(final List<Byte> input) {
        final int length = input.size();
        final byte[] output = new byte[length];
        for (int i = 0; i < length; i++) {
            output[i] = input.get(i);
        }
        return output;
    }

    public static byte[] slice(final byte[] input, final int begin, final int end) {
        final int length = end - begin;
        final byte[] sliced = new byte[length];
        System.arraycopy(input, begin, sliced, 0, length);
        return sliced;
    }

    public static String toString(final byte[] input) {
        return new String(input, StandardCharsets.UTF_8);
    }

    public static byte[] concat(byte[] b1, byte[] b2) {
        final byte[] joined = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, joined, 0, b1.length);
        System.arraycopy(b2, 0, joined, b1.length, b2.length);
        return joined;
    }

    public static List<byte[]> split(final byte[] input, final Predicate<Byte> pred) {
        final List<byte[]> output = new ArrayList<>();
        final int length = input.length;
        int index = 0;
        while (index <= length) {
            final List<Byte> result = new ArrayList<>();
            while (index < length && !pred.test(input[index])) {
                result.add(input[index++]);
            }
            output.add(toByteArray(result));
            index++;
        }
        return output;
    }

    public static List<byte[]> splitn(
            final byte[] input,
            final int size,
            final Predicate<Byte> pred) {

        final List<byte[]> output = new ArrayList<>();
        final int length = input.length;
        int index = 0;
        while (index <= length) {
            final List<Byte> result = new ArrayList<>();
            while (index < length && !pred.test(input[index])) {
                result.add(input[index++]);
            }
            output.add(toByteArray(result));
            if (output.size() + 1 == size) {
                output.add(slice(input, index + 1, length));
                break;
            }
            index++;
        }
        return output;
    }

    public static byte[] subArray(final byte[] input, int begin, int end) {
        return Arrays.copyOfRange(input, begin, end);
    }

    public static String subString(final byte[] input, int begin, int end) {
        return toString(subArray(input, begin, end));
    }

    public static String hashOf(final byte[] input) {
        try {
            final MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            return toHexString(sha1.digest(input));
        } catch (NoSuchAlgorithmException e) {
            // SHA-1は必ずあるため、例外は発生しない
            return "";
        }
    }

    /**
     * バイト配列から文字列に変換します。
     *
     * { 0x01, 0x3A, 0x20, 0x45 } => "013A2045"
     *
     * @param input バイト列
     * @return 16進数文字列
     */
    public static String toHexString(final byte[] input) {
        return String.format("%040x", new BigInteger(1, input));
    }

    /**
     * 文字列を16進数の配列に変換します。
     *
     * "013A2045" => { 0x01, 0x3A, 0x20, 0x45 }
     *
     * @param input 16進数文字列
     * @return バイト列
     */
    public static byte[] toHexBytes(final String input) {
        return HexFormat.of().parseHex(input);
    }

    public static void display(final byte[] input) {
        for (int i = 0; i < input.length; i++) {
            System.out.print("%2d ".formatted(i));
        }
        System.out.println();
        for (byte b : input) {
            System.out.print("%2x ".formatted(b));
        }
        System.out.println();
    }

    public static byte[] compress(final byte[] input) throws IOException {
        final Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.setLevel(1);

        try (final var outputStream = new ByteArrayOutputStream(input.length)) {
            deflater.finish();
            final byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                final int count = deflater.deflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            return outputStream.toByteArray();
        }
    }

    public static byte[] decompress(final byte[] input) throws IOException, DataFormatException {
        final Inflater inflater = new Inflater();
        inflater.setInput(input);

        try (final var outputStream = new ByteArrayOutputStream(input.length)) {
            final byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                final int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            return outputStream.toByteArray();
        }
    }
}
