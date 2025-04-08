package org.example.teamup.API;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


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

        try (DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream())){
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

        while ((inputLine = in.readLine()) != null){
            response.append(inputLine);
        }
        in.close();
        System.out.println("Respuesta: " + response.toString());
        conn.disconnect();

    }
}
