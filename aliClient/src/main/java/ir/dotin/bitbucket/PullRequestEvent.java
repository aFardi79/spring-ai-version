
package ir.dotin.bitbucket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestEvent {

    private PullRequest pullRequest;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PullRequest {
        private Long id;
        private String title;
        private Ref fromRef;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ref {
        private Repository repository;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Repository {
        private String slug;
        private Project project;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Project {
        private String key;
    }
}
