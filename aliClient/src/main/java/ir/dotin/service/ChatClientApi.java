package ir.dotin.service;

import ir.dotin.model.local.OllamaRequest;
import ir.dotin.model.local.OllamaResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


import java.util.Map;


@Service
public class ChatClientApi {

    private RestClient restClient;

    public String sendMessage(String message) {
        OllamaRequest ollamaRequest = new OllamaRequest("deepseek-r1:8b", message, false,
                Map.of("num_predict", 1000, "temperature", 0.0), true,false);
        restClient = RestClient.builder().baseUrl("http://localhost:11434/api").build();
        OllamaResponse response = restClient.post()
                .uri("/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(ollamaRequest)
                .retrieve()
                .body(OllamaResponse.class);
         return response.response();
    }


    public String sendJavaApiStreamMessage(String message) {
        String systemPrompt = """
        You are a Java AI agent. Your task is to take imperative Java code and refactor it using Java Stream API wherever applicable.
        Input will always be raw Java code. Output only the refactored version using streams, with no explanation.
        """;
        String fullPrompt= systemPrompt+"\n\n"+message;
        OllamaRequest ollamaRequest = new OllamaRequest("deepseek-r1:8b",fullPrompt,false,
                Map.of("num_predict", 1000, "temperature", 0.0), true,false);
        restClient = RestClient.builder().baseUrl("http://localhost:11434/api").build();
        OllamaResponse response = restClient.post()
                .uri("/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(ollamaRequest)
                .retrieve()
                .body(OllamaResponse.class);
        return response.response();
    }
}
