import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;

public class ConsultarConversion {

    private static final String URL_TEMPLATE = "https://v6.exchangerate-api.com/v6/%s/pair/%s/%s/%s";

    private String getApiKey() {
        // 1) Try environment variable
        String key = System.getenv("EXCHANGE_API_KEY");
        if (key != null && !key.isBlank()) return key.trim();

        // 2) Try config.properties in working directory
        Path localConfig = Path.of("config.properties");
        if (Files.exists(localConfig)) {
            Properties p = new Properties();
            try (InputStream is = Files.newInputStream(localConfig)) {
                p.load(is);
                key = p.getProperty("api.key");
                if (key != null && !key.isBlank()) return key.trim();
            } catch (IOException ignored) {
                // fallthrough to try classpath
            }
        }

        // 3) Try config.properties on classpath (e.g., src/main/resources)
        try (InputStream is = ConsultarConversion.class.getResourceAsStream("/config.properties")) {
            if (is != null) {
                Properties p = new Properties();
                p.load(is);
                key = p.getProperty("api.key");
                if (key != null && !key.isBlank()) return key.trim();
            }
        } catch (IOException ignored) {
            // ignore and throw below
        }

        throw new RuntimeException("API key no encontrada. Define la variable de entorno EXCHANGE_API_KEY o crea config.properties con 'api.key=TU_API_KEY'.");
    }

    @SuppressWarnings("resource") // HttpClient is not Closeable; try-with-resources not applicable
    public Conversor realizarConversion(String base_code, String target_code, double amount) {

        String apiKey = getApiKey();
        String url = String.format(URL_TEMPLATE, apiKey, base_code, target_code, amount);
        URI direccion = URI.create(url);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(direccion)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();
            String body = response.body();

            if (status != 200) {
                throw new RuntimeException("Respuesta HTTP inválida: " + status + ". Cuerpo: " + body);
            }

            Conversor result = new Gson().fromJson(body, Conversor.class);

            if (result == null) {
                throw new RuntimeException("Respuesta inesperada del servicio (JSON nulo). Cuerpo: " + body);
            }

            return result;

        } catch (IOException e) {
            throw new RuntimeException("Error de E/S al comunicarse con la API: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("La petición fue interrumpida: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo completar la solicitud: " + e.getMessage(), e);
        }
    }
}
