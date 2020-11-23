package polytech.group3.iwa.alert_contact_case.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/user")
public class RequestController {

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

    private Map<String, String> parseBody(String body) {

        String[] split = body.split("&");

        Map<String, String> map = new HashMap<>();

        for (String s : split) {
            String[] str = s.split("=");
            map.put(str[0], str[1]);
        }
        return map;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> addUser(@RequestBody String body) {

        Map<String, String> bodyParsed = parseBody(body);

        String username = bodyParsed.get("username");
        String password = bodyParsed.get("password");

        if (username == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        HttpResponse<String> response;
        try {
            response = login(adminUser, adminPwd);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if (HttpStatus.valueOf(response.statusCode()) != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.valueOf(response.statusCode())).build();
        }
        String responseBody = response.body();

        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

        String access_token = jsonObject.get("access_token").getAsString();

        try {
            response = registerUser(username, access_token);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if (HttpStatus.valueOf(response.statusCode()) != HttpStatus.CREATED) {
            return ResponseEntity.status(HttpStatus.valueOf(response.statusCode())).build();
        }

        Optional<String> location = response.headers().firstValue("Location");

        if (location.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        String passwordUrl = location.get() + "/reset-password";

        try {
            response = setPassword(password, passwordUrl, access_token);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if (HttpStatus.valueOf(response.statusCode()) != HttpStatus.NO_CONTENT) {
            return ResponseEntity.status(HttpStatus.valueOf(response.statusCode())).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> getJWT(@RequestBody String body) {

        Map<String, String> bodyParsed = parseBody(body);

        String username = bodyParsed.get("username");
        String password = bodyParsed.get("password");

        if (username == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        HttpResponse<String> response;
        try {
            response = login(username, password);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if (HttpStatus.valueOf(response.statusCode()) != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.valueOf(response.statusCode())).build();
        }

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response.body());

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

    private HttpResponse<String> registerUser(String username, String access_token) throws IOException, InterruptedException {

        String requestBody = "{ \"enabled\": \"true\", \"username\": \"" + username + "\" }";

//        System.out.println(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(keycloakURL + keycloakRegisterPath))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + access_token)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> setPassword(String password, String url, String access_token) throws IOException, InterruptedException {

        String requestBody = "{ \"type\": \"password\", \"temporary\": false, \"value\": \"" + password + "\" }";

//        System.out.println(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + access_token)
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
