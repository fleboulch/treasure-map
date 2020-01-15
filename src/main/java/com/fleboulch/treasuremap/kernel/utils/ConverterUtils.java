package com.fleboulch.treasuremap.kernel.utils;

public class ConverterUtils {

    private ConverterUtils() {
    }

    public static int toInt(String valueAsString) {
        return Integer.parseInt(valueAsString.trim());
    }
}
