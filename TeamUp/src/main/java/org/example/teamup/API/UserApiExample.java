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

    // Prueba directa del método
    public static void main(String[] args) {
        try {
            // Reemplaza este token por uno real obtenido al hacer login
            String token = "58|3u76qw5OKi3D7YCi7JII5KqAH0PBlJRZJvZnYZea734ed234";
            String url = "http://127.0.0.1:8000/api/usuario/9"; // endpoint de prueba

            String response = sendGetRequest(url, token);
            System.out.println("Respuesta del servidor:");
            System.out.println(response);
        } catch (IOException e) {
            System.out.println("ERROR:");
            System.out.println(getResponseError());
        }
    }
}
