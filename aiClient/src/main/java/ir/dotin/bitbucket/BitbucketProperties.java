
package ir.dotin.bitbucket;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "bitbucket")
public class BitbucketProperties {

    private String apiUrl;
    private String token;
}
