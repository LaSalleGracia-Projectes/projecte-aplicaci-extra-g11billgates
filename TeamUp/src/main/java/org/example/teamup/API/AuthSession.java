package org.example.teamup.API;

public class AuthSession {
    private static String bearerToken;

    public static void setToken(String token) {
        bearerToken = token;
    }

    public static String getToken() {
        return bearerToken;
    }

    public static boolean isAuthenticated() {
        return bearerToken != null && !bearerToken.isEmpty();
    }

    public static void clear() {
        bearerToken = null;
    }
}