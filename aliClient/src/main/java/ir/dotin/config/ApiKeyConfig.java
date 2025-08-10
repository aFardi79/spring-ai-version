package ir.dotin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiKeyConfig {

    @Value("${deepseek.api.key}")
    private String deepSeekApiKey;

    @Value("${bitbucket.api.key}")
    private String bitbucketApiKey;

    public String getDeepSeekApiKey() {
        return deepSeekApiKey;
    }

    public String getBitbucketApiKey() {
        return bitbucketApiKey;
    }
}

