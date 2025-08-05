package ir.dotin.model.local;

import lombok.*;

import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
public class OllamaRequest {

    String model;
    String prompt;
    boolean stream;
    Map<String, Object> options;
    boolean concise;
    boolean think;
}
