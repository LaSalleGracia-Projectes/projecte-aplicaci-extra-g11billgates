package org.example.teamup.API;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class JuegoApiExample {

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
    public static String addJuego(int juegoId, String bearerToken) throws IOException {
        String url = "http://127.0.0.1:8000/api/juego";
        String json = String.format("{\"juego_id\": %d}", juegoId);
        return sendRequest("POST", url, bearerToken, json);
    }
    public static String removeJuego(int juegoId, String bearerToken) throws IOException {
        String url = "http://127.0.0.1:8000/api/juego";
        String json = String.format("{\"juego_id\": %d}", juegoId);
        return sendRequest("DELETE", url, bearerToken, json);
    }
    public static String obtenerTodosLosJuegos(String bearerToken) throws IOException {
        String url = "http://127.0.0.1:8000/api/juegos";
        return sendRequest("GET", url, bearerToken, null);
    }
    public static String obtenerJuegosFavoritosDeUsuario(int userId, String bearerToken) throws IOException {
        String url = "http://127.0.0.1:8000/api/juegoFavorito/" + userId;
        return sendRequest("GET", url, bearerToken, null);
    }



}
