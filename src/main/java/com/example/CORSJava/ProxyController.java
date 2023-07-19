package com.example.CORSJava;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class ProxyController {

    @PostMapping("/")
    public ResponseEntity<String> proxyToSullyGnome(@RequestParam String url) throws IOException {

        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the response from the input stream
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            StringBuilder response = new StringBuilder();
            int charRead;
            while ((charRead = inputStreamReader.read()) != -1) {
                response.append((char) charRead);
            }
            inputStreamReader.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            return new ResponseEntity<>(response.toString(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error: " + responseCode, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}