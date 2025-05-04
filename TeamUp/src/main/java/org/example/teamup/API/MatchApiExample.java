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


}
