package org.example.teamup.API;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatApiExample {

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

    public static String crearChat(int idMatch, String bearerToken) throws IOException {
        String url = "http://127.0.0.1:8000/api/chats";
        String json = String.format("{\"IDMatch\": %d}", idMatch);
        return sendRequest("POST", url, bearerToken, json);
    }

    public static String obtenerMensajesPorChat(int idChat, String bearerToken) throws IOException {
        String url = "http://127.0.0.1:8000/api/chats/" + idChat + "/messages";
        return sendRequest("GET", url, bearerToken, null);
    }

    public static String enviarMensaje(int idChat, String tipo, String fechaEnvio, String texto, String bearerToken) throws IOException {
        String url = "http://127.0.0.1:8000/api/messages";
        String json = String.format("{\"IDChat\": %d, \"Tipo\": \"%s\", \"FechaEnvio\": \"%s\", \"Texto\": \"%s\"}", idChat, tipo, fechaEnvio, texto);
        return sendRequest("POST", url, bearerToken, json);
    }

    public static String eliminarChat(int idChat, String bearerToken) throws IOException {
        String url = "http://127.0.0.1:8000/api/chats/" + idChat;
        return sendRequest("DELETE", url, bearerToken, null);
    }
}
