package org.example.teamup.API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserApiExample {

    private static String responseError = "";

    public static String getResponseError() {
        return responseError;
    }

    public static String sendGetRequest(String urlString, String bearerToken) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + bearerToken);

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
            throw new IOException("Error en GET: " + responseCode);
        }

        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }

        in.close();
        conn.disconnect();
        return response.toString();
    }

    /**
     * Llama al endpoint /api/usuarios/aleatorio y devuelve el ID como int
     */
    public static int getRandomUserId(String bearerToken) throws IOException {
        String url = "http://127.0.0.1:8000/api/usuarios/aleatorio";
        String json = sendGetRequest(url, bearerToken);

        // Espera respuesta como: { "random_user_id": 9 }
        int idStart = json.indexOf(":") + 1;
        int idEnd = json.indexOf("}", idStart);
        String idStr = json.substring(idStart, idEnd).trim();

        return Integer.parseInt(idStr);
    }

    /**
     * Prueba de getRandomUserId
     */
    /*
    public static void main(String[] args) {
        try {
            String token = "60|eago6OQpQ10N9I9l6W9sY5E5ByDNRbvRqDSFoZ4z7a6ea73c";
            int randomId = getRandomUserId(token);
            System.out.println("ID de usuario aleatorio: " + randomId);
        } catch (IOException e) {
            System.out.println("ERROR:");
            System.out.println(getResponseError());
        }
    }
    */
}
