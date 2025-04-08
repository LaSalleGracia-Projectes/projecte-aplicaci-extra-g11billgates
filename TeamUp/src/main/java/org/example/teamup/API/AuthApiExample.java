package org.example.teamup.API;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class AuthApiExample {

    // Variable estática para almacenar temporalmente el error de respuesta
    private static String responseError = "";

    public static String getResponseError() {
        return responseError;
    }

    /**
     * Método genérico para enviar peticiones POST y retornar la respuesta del servidor.
     * @param urlString URL destino.
     * @param jsonInputString Cuerpo del JSON.
     * @return La respuesta del servidor en formato String.
     * @throws IOException
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
            // Guarda la respuesta de error para poder mostrarla en la vista
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            responseError = response.toString();
            throw new IOException("Error en la petición, código " + responseCode);
        }
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        conn.disconnect();
        return response.toString();
    }

    public static String register(String Nombre, String email, String password, String password_confirmation, int Edad, String region) throws IOException {
        String url = "http://127.0.0.1:8000/api/register";
        // Asegúrate de que los nombres de los campos coinciden con los que espera Laravel
        String jsonInputString = String.format(
                "{\"Nombre\": \"%s\", \"email\": \"%s\", \"password\": \"%s\", \"password_confirmation\": \"%s\", \"Edad\": %d, \"region\": \"%s\"}",
                Nombre, email, password, password_confirmation, Edad, region);
        System.out.println("Registrandose...");
        return sendPostRequest(url, jsonInputString);
    }

    public static String login(String email, String password) throws IOException {
        String url = "http://127.0.0.1:8000/api/login";
        String jsonInputString = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password);
        System.out.println("Iniciando sesión...");
        return sendPostRequest(url, jsonInputString);
    }
}

    /*public static void main(String[] args) throws IOException {
        // Ejemplo de uso:
        // 1. Registro de un usuario nuevo.
        register("Roger", "roger@example.com", "123456aA#", "123456aA#", 32, "Europa");

        // 2. Login del usuario registrado.
        login("roger@example.com", "123456aA#");
    }*/


