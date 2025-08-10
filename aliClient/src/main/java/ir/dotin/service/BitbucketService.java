package ir.dotin.service;

import ir.dotin.config.ApiKeyConfig;
import ir.dotin.model.bitbucket.DiffModel;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class BitbucketService {

    private final RestClient restClient;
    private final String apiKey;

    public BitbucketService(RestClient.Builder restClientBuilder, ApiKeyConfig apiKeyConfig) {
        this.restClient = restClientBuilder.baseUrl("http://localhost:7990/rest/api/1.0").build();
        this.apiKey = apiKeyConfig.getBitbucketApiKey();
    }

    public DiffModel getPullRequestDiff(String projectKey, String repoSlug, int prId) {
        String url = String.format("/projects/%s/repos/%s/pull-requests/%d/diff", projectKey, repoSlug, prId);
        return restClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + apiKey)
                .retrieve()
                .toEntity(DiffModel.class)
                .getBody();
    }

    public String postPullRequestComment(String projectKey, String repoSlug, int prId, String comment) {
        String url = String.format("/projects/%s/repos/%s/pull-requests/%d/comments", projectKey, repoSlug, prId);
        JSONObject payload = new JSONObject();
        payload.put("text", comment);
        return restClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload.toString())
                .retrieve()
                .body(String.class);
    }

    public String approvePullRequest(String projectKey, String repoSlug, int prId) {
        String url = String.format("/projects/%s/repos/%s/pull-requests/%d/approve", projectKey, repoSlug, prId);
        return restClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + apiKey)
                .retrieve()
                .body(String.class);
    }
}
