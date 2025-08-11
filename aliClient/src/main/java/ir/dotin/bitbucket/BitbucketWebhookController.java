
package ir.dotin.bitbucket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bitbucket")
@RequiredArgsConstructor
public class BitbucketWebhookController {

    private final BitbucketService bitbucketService;
    private final ObjectMapper objectMapper;

    @PostMapping("/webhook")
    public void handleWebhook(@RequestBody String payload) throws Exception {
        PullRequestEvent event = objectMapper.readValue(payload, PullRequestEvent.class);
        bitbucketService.processPullRequestEvent(event);
    }
}
