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
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ByteArrayUtil {

    public static byte[] toByteArray(InputStream is) throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int count;
        byte[] data = new byte[1024];
        while ((count = is.read(data, 0, data.length)) > 0) {
            buffer.write(data, 0, count);
        }

        buffer.flush();
        return buffer.toByteArray();
    }

    public static byte[] toByteArray(final Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    public static String toString(final byte[] input) {
        return new String(input, StandardCharsets.UTF_8);
    }

    public static void display(final byte[] input) {
        for (byte b : input) {
            System.out.print(b + " ");
        }
        System.out.println();
    }

    public static byte[] concat(byte[] b1, byte[] b2) {
        final byte[] joined = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, joined, 0, b1.length);
        System.arraycopy(b2, 0, joined, b1.length, b2.length);
        return joined;
    }

    public static String getHash(final byte[] input) {
        try {
            final MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            return String.format("%040x", new BigInteger(1, sha1.digest(input)));
        } catch (NoSuchAlgorithmException e) {
            // SHA-1は必ずあるため、例外は発生しない
            return "";
        }
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
