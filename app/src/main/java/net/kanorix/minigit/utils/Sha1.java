package net.kanorix.minigit.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1 {

    public static String digest(final byte[] input) {
        try {
            final MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            return String.format("%040x", new BigInteger(1, sha1.digest(input)));
        } catch (NoSuchAlgorithmException e) {
            return "0000000000000000000000000000000000000000";
        }
    }
}
