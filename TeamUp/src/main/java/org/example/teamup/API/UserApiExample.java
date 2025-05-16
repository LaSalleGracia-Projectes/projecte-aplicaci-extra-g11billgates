package org.example.teamup.API;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

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
    public static String getUserById(int id, String bearerToken) throws IOException {
        String url = "http://127.0.0.1:8000/api/usuario/" + id;
        return sendGetRequest(url, bearerToken);
    }
    public static String subirFotoPerfil(File foto, String bearerToken) throws IOException {
        String url = "http://127.0.0.1:8000/api/user/foto-perfil";

        String boundary = Long.toHexString(System.currentTimeMillis());
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (OutputStream output = connection.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true)) {

            // -- Foto
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"foto\"; filename=\"")
                    .append(foto.getName()).append("\"\r\n");
            writer.append("Content-Type: image/jpeg\r\n\r\n").flush();
            Files.copy(foto.toPath(), output);
            output.flush();
            writer.append("\r\n").flush();

            // -- End
            writer.append("--").append(boundary).append("--").append("\r\n").flush();
        }

        int responseCode = connection.getResponseCode();
        BufferedReader in;
        StringBuilder response = new StringBuilder();

        if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
            throw new IOException("Error al subir foto: " + response);
        }

        return response.toString();
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
