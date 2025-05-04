package org.example.teamup.API;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.example.teamup.API.AuthSession;
import org.json.JSONObject;

public class AuthApiExample {

    // Variable estática para almacenar el último error de respuesta
    private static String responseError = "";

    public static String getResponseError() {
        return responseError;
    }

    /**
     * Envía una petición POST con cuerpo JSON y devuelve la respuesta.
     * Lanza IOException con mensaje personalizado si la respuesta es error.
     */
    public static String sendPostRequest(String urlString, String jsonInputString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try (DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream())) {
            outputStream.writeBytes(jsonInputString);
            outputStream.flush();
        }

        int responseCode = conn.getResponseCode();
        BufferedReader in;
        StringBuilder response = new StringBuilder();

        if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                errorResponse.append(line);
            }
            in.close();
            responseError = extractMessage(errorResponse.toString());
            throw new IOException(responseError);
        }

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        conn.disconnect();
        return response.toString();
    }


    private static String extractMessage(String json) {
        try {
            int messageIndex = json.indexOf("\"message\"");
            if (messageIndex != -1) {
                int start = json.indexOf(":", messageIndex) + 1;
                int firstQuote = json.indexOf("\"", start);
                int secondQuote = json.indexOf("\"", firstQuote + 1);
                return json.substring(firstQuote + 1, secondQuote);
            }
        } catch (Exception e) {
            // Si falla la extracción, se devuelve el JSON completo
        }
        return json;
    }

    public static String register(String Nombre, String email, String password, String password_confirmation, int Edad, String region) throws IOException {
        String url = "http://127.0.0.1:8000/api/register";
        String jsonInputString = String.format(
                "{\"Nombre\": \"%s\", \"email\": \"%s\", \"password\": \"%s\", \"password_confirmation\": \"%s\", \"Edad\": %d, \"Region\": \"%s\"}",
                Nombre, email, password, password_confirmation, Edad, region);
        System.out.println("Registrando usuario...");
        return sendPostRequest(url, jsonInputString);
    }

    public static String login(String email, String password) throws IOException {
        String url = "http://127.0.0.1:8000/api/login";
        String jsonInputString = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password);
        System.out.println("Iniciando sesión...");

        String response = sendPostRequest(url, jsonInputString);

        // Extraer token y guardarlo en sesión
        try {
            JSONObject json = new JSONObject(response);
            String token = json.getJSONObject("token").getString("plainTextToken"); // ✅
            AuthSession.setToken(token);
            System.out.println("Token guardado: " + token);
            AuthSession.setToken(token);
            System.out.println("Token guardado: " + token);
        } catch (Exception e) {
            System.out.println("Error al extraer token: " + e.getMessage());
        }

        return response;
    }

    /*
    // Ejemplo de uso en consola
    public static void main(String[] args) {
        try {
            register("Roger", "roger@example.com", "123456aA#", "123456aA#", 30, "Europa");
            login("roger@example.com", "123456aA#");
        } catch (IOException e) {
            System.out.println("ERROR: " + getResponseError());
        }
    }
    */
}
