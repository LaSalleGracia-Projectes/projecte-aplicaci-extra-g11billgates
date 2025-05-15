package org.example.teamup.API;

public class AuthSession {
    private static String bearerToken;
    private static int userId;

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
        userId = 0;
    }

    public static void setUserId(int id) {
        userId = id;
    }

    public static int getUserId() {
        return userId;
    }
}
