package com.example.ledgerlift.utils;

import java.util.UUID;

public class Utils {

    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    public static String extractExtension(String mediaName) {
        return mediaName.substring(mediaName.lastIndexOf(".") + 1);
    }

}
