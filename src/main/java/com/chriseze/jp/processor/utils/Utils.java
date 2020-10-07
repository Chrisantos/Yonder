package com.chriseze.jp.processor.utils;

import java.util.Random;

public class Utils {
    private static final char[] DIGITS = "123456789".toCharArray();
    private final Random random = new Random();

    public static final String ACCOUNT_SID = "ACc4b74aca6b01acd0cc036f092e4b645b";
    public static final String AUTH_TOKEN = "435be224700ee78e254ba99778e4bb4e";

    public String generateDigits(int width) {
        return generate(DIGITS, width);
    }

    private String generate(char[] characters, int width) {
        int initSize = 0;
        int characterSize = characters.length - 1;
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < width; i++) {
            int index = initSize + (random.nextInt(characterSize - initSize + 1));
            buf.append(characters[index]);
        }
        return buf.toString();
    }
}
