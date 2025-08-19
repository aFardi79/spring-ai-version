package ir.dotin.model.domain;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeepSeekResponsePromptTokenDetails {
    private int cached_tokens;
}
