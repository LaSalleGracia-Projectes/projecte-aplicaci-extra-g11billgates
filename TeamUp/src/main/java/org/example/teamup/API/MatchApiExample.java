package org.example.teamup.API;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class MatchApiExample {

    private static String responseError = "";

    public static String getResponseError() {
        return responseError;
    }

    private static String sendRequest(String method, String urlString, String bearerToken, String jsonInputString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + bearerToken);

        if (jsonInputString != null && !jsonInputString.isEmpty()) {
            conn.setDoOutput(true);
            try (DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream())) {
                outputStream.writeBytes(jsonInputString);
                outputStream.flush();
            }
        }

        int responseCode = conn.getResponseCode();
        BufferedReader in;
        StringBuilder response = new StringBuilder();

        if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            responseError = response.toString();
            throw new IOException("Error " + responseCode + ": " + responseError);
        }

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        conn.disconnect();
        return response.toString();
    }
    public static String like(int IDUsuario2, String bearerToken) throws IOException {
        String url = "http://127.0.0.1:8000/api/likes";
        String json = String.format("{\"IDUsuario2\": %d}", IDUsuario2);
        return sendRequest("POST", url, bearerToken, json);
    }

    public static String unlikeReceived(int IDUsuario1, String bearerToken) throws IOException {
        String url = "http://127.0.0.1:8000/api/likes/received";
        String json = String.format("{\"IDUsuario1\": %d}", IDUsuario1);
        return sendRequest("DELETE", url, bearerToken, json);
    }

    public static boolean checkMutualLike(int IDUsuario2, String bearerToken) throws IOException {
        String url = "http://127.0.0.1:8000/api/match/check";
        String json = String.format("{\"IDUsuario2\": %d}", IDUsuario2);
        String response = sendRequest("POST", url, bearerToken, json);
        JSONObject obj = new JSONObject(response);
        return obj.getBoolean("match");
    }

    public static int createMatch(int IDUsuario1, String bearerToken) throws IOException {
        String url = "http://127.0.0.1:8000/api/matches";
        String json = String.format("{\"IDUsuario1\": %d}", IDUsuario1);
        String response = sendRequest("POST", url, bearerToken, json);

        // Esperamos algo como { "status":"Match Created!", "match": { "IDMatch": 42, ... } }
        JSONObject obj = new JSONObject(response);
        JSONObject matchObj = obj.getJSONObject("match");
        return matchObj.getInt("IDMatch");
    }

    public static String deleteMatch(int IDUsuario2, String bearerToken) throws IOException {
        String url = "http://127.0.0.1:8000/api/match";
        String json = String.format("{\"IDUsuario2\": %d}", IDUsuario2);
        return sendRequest("DELETE", url, bearerToken, json);
    }


}
