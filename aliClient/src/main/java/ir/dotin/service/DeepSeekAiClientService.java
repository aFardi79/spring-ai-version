package ir.dotin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.dotin.config.ApiKeyConfig;
import ir.dotin.model.bitbucket.DiffModel;
import ir.dotin.model.domain.DeepSeekMessageRequest;
import ir.dotin.model.domain.DeepSeekRequest;
import ir.dotin.model.domain.DeepSeekResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DeepSeekAiClientService {

    private final RestClient restClient;
    private final BitbucketService bitbucketService;
    private final String deepSeekApiKey;

    String promptTemplate = """
            ROLE: Senior Software Engineer (Java 8, Hibernate Expert)
            
            TASK: Review the provided Java source code for improper Hibernate session management.
            
            TARGET PATTERN:
            Find every place where a Hibernate session is opened with:
            HibernateSession <var> = HibernateManager.createSession();
            
            Then verify that the corresponding control-flow path closes that session via <var>.close(). Also track the session if it is passed to another method within the same class; if that callee (or any further intra-class callee it delegates to) does not close the session parameter, report the originating code.
            
            DETAILED RULES:
            
            A match occurs if:
            
            The method that creates the session does not call <var>.close() on all control-flow paths and does not pass <var> to a method that closes it, OR
            
            The session is passed to another method in the same class and that callee (and any chain of same-class callees) does not call the parameter’s .close() on all control-flow paths.
            
            Consider variable renames/aliases (e.g., Session s2 = s1;); closing any alias counts as closing the session.
            
            try-with-resources counts as closed. Otherwise, .close() must appear (typically in finally); early returns/throws must still ensure closure.
            
            Ignore strings/comments. Ignore framework-managed scopes (@Transactional) unless the code itself calls HibernateManager.createSession().
            
            If a session escapes the class (returned or stored in a field) without being closed in-class, treat as a leak.
            
            RESPONSE RULES:
            
            Output ONLY the raw code block(s) (smallest enclosing method or block) where the session may leak per the rules above.
            
            Do NOT add explanations, analysis, or extra text.
            
            If no matching code exists, output exactly: file has no session that are not Closed.
            
            INPUT_CODE:
            ""\"%s""\"
            """;

    public DeepSeekAiClientService(RestClient.Builder restClientBuilder,
                                   BitbucketService bitbucketService,
                                   ApiKeyConfig apiKeyConfig) {
        this.restClient = restClientBuilder.baseUrl("https://api.deepseek.com").build();
        this.bitbucketService = bitbucketService;
        this.deepSeekApiKey = apiKeyConfig.getDeepSeekApiKey();
    }

    public String getDeepSeekApiClientMessage(String deepSeekApiClientMessage) {
        List<DeepSeekMessageRequest> deepSeekMessageRequests = new ArrayList<>();
        deepSeekMessageRequests.add(new DeepSeekMessageRequest("system", "You are a helpful assistant."));
        deepSeekMessageRequests.add(new DeepSeekMessageRequest("user", deepSeekApiClientMessage));
        DeepSeekRequest deepSeekRequest = new DeepSeekRequest("deepseek-chat", deepSeekMessageRequests, false);
        DeepSeekResponse response = restClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + deepSeekApiKey)
                .body(deepSeekRequest)
                .retrieve()
                .body(DeepSeekResponse.class);
        return response.getChoices().get(0).getMessage().getContent();
    }

    public String getDeepSeekApiChatBitbucketMessage(String deepSeekApiChatBitbucketMessage) {
        JSONObject root = new JSONObject(deepSeekApiChatBitbucketMessage);
        JSONObject pullRequest = root.getJSONObject("pullRequest");
        int prId = pullRequest.getInt("id");
        JSONObject fromRef = pullRequest.getJSONObject("fromRef");
        JSONObject repository = fromRef.getJSONObject("repository");
        String repoSlug = repository.getString("slug");
        String projectKey = repository.getJSONObject("project").getString("key");
        try {
            DiffModel diffModel = bitbucketService.getPullRequestDiff(projectKey, repoSlug, prId);
            ObjectMapper objectMapper = new ObjectMapper();
            String diffValue = objectMapper.writeValueAsString(diffModel);
            String deepValue = getDeepSeekApiClientMessage(diffValue);
            String commentResponse = bitbucketService.postPullRequestComment(projectKey, repoSlug, prId, deepValue);
            bitbucketService.approvePullRequest(projectKey, repoSlug, prId);
            return commentResponse;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public String getOpenSessionSecarioFromfile() {
        try {
            String javaFilePath = "/home/ftamir79/IdeaProjects/spring-ai-version/aliClient/src/main/java/ir/dotin/tempo/Test.java";
            String javaFileCode = Files.readString(Paths.get(javaFilePath));
            List<DeepSeekMessageRequest> deepSeekMessageRequests = new ArrayList<>();
            String finalPromt = String.format(promptTemplate, javaFileCode);
            deepSeekMessageRequests.add(new DeepSeekMessageRequest("system", finalPromt));
            DeepSeekRequest deepSeekRequest = new DeepSeekRequest("deepseek-chat", deepSeekMessageRequests, false);
            DeepSeekResponse response = restClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + deepSeekApiKey)
                    .body(deepSeekRequest)
                    .retrieve()
                    .body(DeepSeekResponse.class);
            return response.getChoices().get(0).getMessage().getContent();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
