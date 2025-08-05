package ir.dotin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        exclude = {
                org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration.class
        }
)
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
