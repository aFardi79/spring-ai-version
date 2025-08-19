
package ir.dotin.deepseek;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "deepseek")
public class DeepSeekProperties {

    private String baseUrl;
    private String apiKey;
}
