package ir.dotin.service;


import ir.dotin.model.domain.DeepSeekMessageRequest;
import ir.dotin.model.domain.DeepSeekRequest;
import ir.dotin.model.domain.DeepSeekResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;


@Service
public class DeepSeekAiClientService {

    private RestClient restClient;

    public String getDeepSeekApiClientMessage(String deepSeekApiClientMessage) {
        List<DeepSeekMessageRequest> deepSeekMessageRequests = new ArrayList<>();
        deepSeekMessageRequests.add(new DeepSeekMessageRequest("system", "You are a helpful assistant."));
        deepSeekMessageRequests.add(new DeepSeekMessageRequest("user", deepSeekApiClientMessage));
        DeepSeekRequest deepSeekRequest = new DeepSeekRequest("deepseek-chat", deepSeekMessageRequests, false);
        restClient = RestClient.builder().baseUrl("https://api.deepseek.com").build();
        DeepSeekResponse response = restClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + "sk-6c97f1e767804952a92d5f2344c9e22f")
                .body(deepSeekRequest)
                .retrieve()
                .body(DeepSeekResponse.class);
        return response.getChoices().get(0).getMessage().getContent();
    }


}
