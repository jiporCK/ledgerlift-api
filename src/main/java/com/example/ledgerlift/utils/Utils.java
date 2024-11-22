package com.example.ledgerlift.utils;

import jakarta.persistence.Column;
import jakarta.servlet.http.HttpServletRequest;

import java.security.SecureRandom;
import java.util.UUID;

public class Utils {

    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    public static String extractExtension(String mediaName) {
        return mediaName.substring(mediaName.lastIndexOf("."));
    }

    public static String getApplicationUrl(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getServletPath(), "");
    }

}
