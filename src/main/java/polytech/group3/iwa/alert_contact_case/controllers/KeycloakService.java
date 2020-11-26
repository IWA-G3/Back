package polytech.group3.iwa.alert_contact_case.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;


@Service
public class KeycloakService {

    @Autowired
    HttpClient httpClient;

    @Value("${keycloak.auth-server-url}")
    String keycloakURL;

    @Value("${keycloakpath.auth-server-path}")
    String keycloakLoginPath;

    @Value("${keycloakpath.admin-user}")
    String adminUser;

    @Value("${keycloakpath.admin-pwd}")
    String adminPwd;

    @Value("${keycloak.realm}")
    String realm;

    @Value("${keycloak.resource}")
    String clientId;

    @Value("${keycloak.credentials.secret}")
    String clientSecret;

    @Value("${keycloakpath.register-server-path}")
    String keycloakRegisterPath;

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .build();
    }


    public Optional<String> addUser(String username, String password) throws InvalidParameter, IOException, InterruptedException, InvalidAdminAccess, NoLocation, NoPasswordSet {

        if (username == null || password == null) {
            throw new InvalidParameter();
        }

        HttpResponse<String> response = login(adminUser, adminPwd);

        if (HttpStatus.valueOf(response.statusCode()) != HttpStatus.OK) {
            throw new InvalidAdminAccess();
        }

        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        String access_token = jsonObject.get("access_token").getAsString();

        response = registerUser(username, access_token);

        if (HttpStatus.valueOf(response.statusCode()) != HttpStatus.CREATED) {
            return Optional.empty();
        }

        Optional<String> location = response.headers().firstValue("Location");

        if (location.isEmpty()) {
            throw new NoLocation();
        }

        String passwordUrl = location.get() + "/reset-password";

        response = setPassword(password, passwordUrl, access_token);

        if (HttpStatus.valueOf(response.statusCode()) != HttpStatus.NO_CONTENT) {
            throw new NoPasswordSet();
        }

        return getJWT(username, password);
    }

    public Optional<String> getJWT(String username, String password) throws InvalidParameter, IOException, InterruptedException{

        if (username == null || password == null) {
            throw new InvalidParameter();
        }

        HttpResponse<String> response = login(username, password);

        if (HttpStatus.valueOf(response.statusCode()) != HttpStatus.OK) {
            return Optional.empty();
        }

        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        return Optional.of(jsonObject.get("access_token").getAsString());

    }

    private HttpResponse<String> login(String username, String password) throws IOException, InterruptedException {

        String requestBody = "grant_type=password&" +
                "client_id=" + clientId + "&" +
                "client_secret=" + clientSecret + "&" +
                "username=" + username + "&" +
                "password=" + password + "&";

//        System.out.println(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(keycloakURL + keycloakLoginPath))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> registerUser(String user_name, String access_token) throws IOException, InterruptedException {

        Gson gson = new Gson();
        String requestBody = gson.toJson(new Object() {
            public String username = user_name;
            public boolean enabled = true;
        });

        System.out.println(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(keycloakURL + keycloakRegisterPath))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + access_token)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> setPassword(String pass_word, String url, String access_token) throws IOException, InterruptedException {

        Gson gson = new Gson();
        String requestBody = gson.toJson(new Object() {
            public String value = pass_word;
            public String type = "password";
            public boolean temporary = false;
        });

        System.out.println(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + access_token)
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static class InvalidParameter extends Exception {
    }

    public static class InvalidAdminAccess extends Exception {
    }

    public static class InvalidUser extends Exception {
    }

    public static class NoLocation extends Exception {
    }

    public static class NoPasswordSet extends Exception {
    }

}
