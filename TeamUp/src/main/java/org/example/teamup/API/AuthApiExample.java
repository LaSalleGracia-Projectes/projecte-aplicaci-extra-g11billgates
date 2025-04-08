package org.example.teamup.API;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;


public class AuthApiExample {
    /**
     * Método genérico para enviar peticiones POST a una URL especificada.
     * @param urlString La URL a la que se enviará la petición.
     * @param jsonInputString El cuerpo del mensaje en formato JSON.
     */

    private static void sendPostRequest(String urlString, String jsonInputString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Configuración de la petición POST
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        // Escribir el cuerpo de la petición (JSON) en el stream
        try (DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream())) {
            outputStream.writeBytes(jsonInputString);  // Enviar el JSON
            outputStream.flush();
        }

        // Obtención y muestra del código de respuesta
        int responseCode = conn.getResponseCode();
        System.out.println("Código de respuesta: " + responseCode);

        // Lectura de la respuesta (manejo de error en caso de respuesta incorrecta)
        BufferedReader in;
        if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("Respuesta: " + response.toString());
        conn.disconnect();
    }

    /**
     * Envía una petición de registro.
     * Se espera que la API acepte el nombre, email, password, edad y region en formato JSON.
     * @param Nombre Nombre del usuario.
     * @param email Email del usuario.
     * @param password Contraseña del usuario.
     * @param password_confirmation confirmacion de contraseña
     * @param Edad edad del usuario
     * @param Region continente del usuario
     */
    public static void register(String Nombre, String email, String password, String password_confirmation, int Edad, String Region) throws IOException {
        String url = "http://127.0.0.1:8000/api/register";
        String jsonInputString = String.format("{\"Nombre\": \"%s\", \"email\": \"%s\", \"password\": \"%s\", \"password_confirmation\": \"%s\", \"Edad\": \"%d\", \"Region\": \"%s\"}", Nombre, email, password, password_confirmation, Edad, Region);
        System.out.println("Registandose...");
        sendPostRequest(url, jsonInputString);
    }
    /**
     * Envía una petición de login.
     * Se espera que la API acepte email y password en formato JSON.
     * @param email Email del usuario.
     * @param password Contraseña del usuario.
     */
    public static void login(String email, String password) throws IOException {
        String url = "http://127.0.0.1:8000/api/login";
        String jsonInputString = String.format("{\"email\": \"%s\", \"password\": \"%s\"}", email, password);
        System.out.println("Iniciando sesion");
        sendPostRequest(url, jsonInputString);
    }
    /**
     * Método para parsear el JSON de errores y devolver un mensaje concatenado.
     * @param jsonResponse La cadena JSON de la respuesta de error.
     * @return Los mensajes de error concatenados.
     */
    private String parseErrorMessages(String jsonResponse) {
        StringBuilder messages = new StringBuilder();
        JSONObject jsonObject = new JSONObject(jsonResponse);
        if (jsonObject.has("errors")) {
            JSONObject errors = jsonObject.getJSONObject("errors");
            for (String field : errors.keySet()) {
                JSONArray errorArray = errors.getJSONArray(field);
                for (int i = 0; i < errorArray.length(); i++) {
                    messages.append(errorArray.getString(i)).append("\n");
                }
            }
        }
        return messages.toString();
    }
    /*public static void main(String[] args) throws IOException {
        // Ejemplo de uso:
        // 1. Registro de un usuario nuevo.
        register("Roger", "roger@example.com", "123456aA#", "123456aA#", 32, "Europa");

        // 2. Login del usuario registrado.
        login("roger@example.com", "123456aA#");
    }*/

}
