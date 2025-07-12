package ir.dotin.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


import java.util.Map;


@Service
public class ChatClientApi {

    private RestClient restClient;

    public String sendMessage(String message) {
        OllamaRequest ollamaRequest = new OllamaRequest("deepseek-r1:8b", message, false,
                Map.of("num_predict", 1000, "temperature", 0.0), true);
        restClient = RestClient.builder().baseUrl("http://localhost:11434/api").build();
        OllamaResponse response = restClient.post()
                .uri("/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(ollamaRequest)
                .retrieve()
                .body(OllamaResponse.class);
        String finalResponse = response.response.substring(response.response.lastIndexOf("</think>") + 8, response.response.length());
         return finalResponse;
    }


    public record OllamaRequest(String model, String prompt, boolean stream, Map<String, Object> options,
                                boolean concise) {
    }

    public record OllamaResponse(String response) {
    }
}
