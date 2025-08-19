package ir.dotin.model.domain;


import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DeepSeekResponse {
    private String object;
    private String created;
    private String model;
    private List<DeepSeekChoice> choices;
    private DeepSeekResponseUsage usage;
    private String system_fingerprint;
}
