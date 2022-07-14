package net.kanorix.minigit.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ByteArrayUtilTest {

    @Test
    void testConcat() {
        final byte[] input1 = {0, 1, 2};
        final byte[] input2 = {3, 4};
        final byte[] expected = {0, 1, 2, 3, 4};

        assertThat(ByteArrayUtil.concat(input1, input2)).isEqualTo(expected);
    }

    @Test
    void testSlice() {
        final byte[] input = {0, 1, 2, 3, 4};
        final byte[] expected = {1, 2};

        assertThat(ByteArrayUtil.slice(input, 1, 3)).isEqualTo(expected);
    }

    @Test
    void testSplit() {
        final byte[] input = {0, 1, 2, 0, 3, 4, 0};
        final byte[] zero = {};
        final byte[] expected1 = {1, 2};
        final byte[] expected2 = {3, 4};

        final List<byte[]> actual = ByteArrayUtil.split(input, b -> b == 0);

        assertThat(actual).containsSequence(zero, expected1, expected2, zero);
    }

    @Test
    void testSplitn() {
        final byte[] input = {0, 1, 2, 0, 3, 4, 0};
        final byte[] zero = {};
        final byte[] expected1 = {1, 2};
        final byte[] expected2 = {3, 4, 0};

        final List<byte[]> actual = ByteArrayUtil.splitn(input, 3, b -> b == 0);

        assertThat(actual).containsSequence(zero, expected1, expected2);
    }

    @Test
    void testToByteArray() {
        final List<Byte> input = List.of((byte) 1, (byte) 2, (byte) 3);
        final byte[] expected = {1, 2, 3};

        assertThat(ByteArrayUtil.toByteArray(input)).isEqualTo(expected);
    }
}
