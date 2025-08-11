
package ir.dotin.deepseek;

import ir.dotin.model.domain.DeepSeekMessageRequest;
import ir.dotin.model.domain.DeepSeekRequest;
import ir.dotin.model.domain.DeepSeekResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeepSeekClient {

    private final RestClient restClient;
    private final DeepSeekProperties deepSeekProperties;

    public String call(String prompt) {
        DeepSeekMessageRequest message = new DeepSeekMessageRequest("user", prompt);
        DeepSeekRequest request = new DeepSeekRequest();
        request.setModel("deepseek-coder");
        request.setMessages(List.of(message));

        DeepSeekResponse response = restClient.post()
                .uri(deepSeekProperties.getBaseUrl() + "/chat/completions")
                .header("Authorization", "Bearer " + deepSeekProperties.getApiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(DeepSeekResponse.class);

        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
            return response.getChoices().get(0).getMessage().getContent();
        }

        return "Error: No response from AI.";
    }
}
