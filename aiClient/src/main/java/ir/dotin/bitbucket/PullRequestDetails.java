
package ir.dotin.bitbucket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestDetails {
    private String title;
    private String description;
}
