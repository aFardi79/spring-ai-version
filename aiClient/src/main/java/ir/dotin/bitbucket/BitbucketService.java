
package ir.dotin.bitbucket;

import ir.dotin.ai.CodeReviewer;
import ir.dotin.config.PromtConfiguration;
import org.springframework.stereotype.Service;

@Service
public class BitbucketService {

    private final BitbucketClient bitbucketClient;
    private final CodeReviewer codeReviewer;
    private final PromtConfiguration promtConfiguration;

    public BitbucketService(BitbucketClient bitbucketClient, CodeReviewer codeReviewer, PromtConfiguration promtConfiguration) {
        this.bitbucketClient = bitbucketClient;
        this.codeReviewer = codeReviewer;
        this.promtConfiguration = promtConfiguration;
    }

    public void processPullRequestEvent(PullRequestEvent event) {
        String projectKey = event.getPullRequest().getFromRef().getRepository().getProject().getKey();
        String repoSlug = event.getPullRequest().getFromRef().getRepository().getSlug();
        Long pullRequestId = event.getPullRequest().getId();

        PullRequestDetails details = bitbucketClient.getPullRequest(projectKey, repoSlug, pullRequestId);
        String diff = bitbucketClient.getPullRequestDiff(projectKey, repoSlug, pullRequestId);
        String review = "";
        if (promtConfiguration.isOpenSession()) {
            review = codeReviewer.reviewOpenSession(details.getTitle(), details.getDescription(), diff);
        } else {
            review = codeReviewer.review(details.getTitle(), details.getDescription(), diff);
        }

        bitbucketClient.postComment(projectKey, repoSlug, pullRequestId, review);

        if (review.contains("DECISION: APPROVE")) {
            bitbucketClient.approvePullRequest(projectKey, repoSlug, pullRequestId);
        } else if (review.contains("DECISION: REJECT")) {
            bitbucketClient.rejectPullRequest(projectKey, repoSlug, pullRequestId);
        }
    }
}
