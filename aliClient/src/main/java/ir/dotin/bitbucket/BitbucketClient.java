
package ir.dotin.bitbucket;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class BitbucketClient {

    private final RestTemplate restTemplate;
    private final BitbucketProperties bitbucketProperties;

    public PullRequestDetails getPullRequest(String projectKey, String repoSlug, Long pullRequestId) {
        String url = String.format("%s/projects/%s/repos/%s/pull-requests/%d", bitbucketProperties.getApiUrl(), projectKey, repoSlug, pullRequestId);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders()), PullRequestDetails.class).getBody();
    }

    public String getPullRequestDiff(String projectKey, String repoSlug, Long pullRequestId) {
        String url = String.format("%s/projects/%s/repos/%s/pull-requests/%d/diff", bitbucketProperties.getApiUrl(), projectKey, repoSlug, pullRequestId);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders()), String.class).getBody();
    }

    public void postComment(String projectKey, String repoSlug, Long pullRequestId, String comment) {
        String url = String.format("%s/projects/%s/repos/%s/pull-requests/%d/comments", bitbucketProperties.getApiUrl(), projectKey, repoSlug, pullRequestId);
        restTemplate.postForObject(url, new HttpEntity<>(Collections.singletonMap("text", comment), createHeaders()), String.class);
    }

    public void approvePullRequest(String projectKey, String repoSlug, Long pullRequestId) {
        String url = String.format("%s/projects/%s/repos/%s/pull-requests/%d/approve", bitbucketProperties.getApiUrl(), projectKey, repoSlug, pullRequestId);
        restTemplate.postForObject(url, new HttpEntity<>(createHeaders()), String.class);
    }

    public void rejectPullRequest(String projectKey, String repoSlug, Long pullRequestId) {
        String url = String.format("%s/projects/%s/repos/%s/pull-requests/%d/approve", bitbucketProperties.getApiUrl(), projectKey, repoSlug, pullRequestId);
        restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(createHeaders()), String.class);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bitbucketProperties.getToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
