//package ir.dotin.deepseek;
//
//import ir.dotin.model.domain.DeepSeekMessageRequest;
//import ir.dotin.model.domain.DeepSeekRequest;
//import ir.dotin.model.domain.DeepSeekResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestClient;
//import org.springframework.web.client.RestClientException;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class DeepSeekAiClientService {
//
//    private static final Logger logger = LoggerFactory.getLogger(DeepSeekAiClientService.class);
//    private final RestClient restClient;
//    private final String deepSeekApiKey;
//
//    // It's better practice to inject configuration via the constructor
//    public DeepSeekAiClientService(@Value("${deepseek.api.url:https://api.deepseek.com/v1}") String apiUrl,
//                                   @Value("${deepseek.api.key:sk-6c97f1e767804952a92d5f2344c9e22f}") String apiKey) {
//        this.restClient = RestClient.builder().baseUrl(apiUrl).build();
//        this.deepSeekApiKey = apiKey; // Make sure this key is correctly set in your application.properties
//    }
//
//    public String getDeepSeekApiClientMessage(String deepSeekApiClientMessage) {
//        List<DeepSeekMessageRequest> deepSeekMessageRequests = new ArrayList<>();
//        deepSeekMessageRequests.add(new DeepSeekMessageRequest("system", "You are a helpful assistant."));
//        deepSeekMessageRequests.add(new DeepSeekMessageRequest("user", deepSeekApiClientMessage));
//
//        DeepSeekRequest deepSeekRequest = new DeepSeekRequest("deepseek-chat", deepSeekMessageRequests, false);
//
//        try {
//            DeepSeekResponse response = restClient.post()
//                    .uri("/chat/completions")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .header("Authorization", "Bearer " + deepSeekApiKey)
//                    .body(deepSeekRequest)
//                    .retrieve()
//                    // *** ADDED ERROR HANDLING BLOCK ***
//                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (request, responseBody) -> {
//                        logger.error("Error response from DeepSeek API. Status: {}, Body: {}", responseBody.getStatusCode(), responseBody.getStatusText());
//                        // You could throw a custom exception here
//                        throw new RestClientException(
//                                "DeepSeek API returned an error: " + responseBody.getStatusCode() + " " + responseBody.getStatusText()
//                        );
//                    })
//                    .body(DeepSeekResponse.class);
//
//            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
//                return response.getChoices().get(0).getMessage().getContent();
//            } else {
//                logger.warn("Received a valid but empty or unexpected response from DeepSeek.");
//                return "Received an empty or unexpected response from the AI.";
//            }
//
//        } catch (RestClientException e) {
//            logger.error("Failed to call DeepSeek API", e);
//            return "Error: Could not connect to or get a valid response from the AI service.";
//        }
//    }
//}
