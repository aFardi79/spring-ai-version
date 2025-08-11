
package ir.dotin.bitbucket;

import ir.dotin.ai.CodeReviewer;
import org.springframework.stereotype.Service;

@Service
public class BitbucketService {

    private final BitbucketClient bitbucketClient;
    private final CodeReviewer codeReviewer;

    public BitbucketService(BitbucketClient bitbucketClient, CodeReviewer codeReviewer) {
        this.bitbucketClient = bitbucketClient;
        this.codeReviewer = codeReviewer;
    }

    public void processPullRequestEvent(PullRequestEvent event) {
        String projectKey = event.getPullRequest().getFromRef().getRepository().getProject().getKey();
        String repoSlug = event.getPullRequest().getFromRef().getRepository().getSlug();
        Long pullRequestId = event.getPullRequest().getId();

        PullRequestDetails details = bitbucketClient.getPullRequest(projectKey, repoSlug, pullRequestId);
        String diff = bitbucketClient.getPullRequestDiff(projectKey, repoSlug, pullRequestId);

        String review = codeReviewer.review(details.getTitle(), details.getDescription(), diff);

        bitbucketClient.postComment(projectKey, repoSlug, pullRequestId, review);

        if (review.contains("DECISION: APPROVE")) {
            bitbucketClient.approvePullRequest(projectKey, repoSlug, pullRequestId);
        } else if (review.contains("DECISION: REJECT")) {
            bitbucketClient.rejectPullRequest(projectKey, repoSlug, pullRequestId);
        }
    }
}
