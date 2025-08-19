package ir.dotin.model.domain;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeepSeekResponseUsage {

    private int prompt_tokens;
    private int completion_tokens;
    private int total_tokens;
    private DeepSeekResponsePromptTokenDetails prompt_tokens_details;
    private int prompt_cache_hint_tokens;
    private int prompt_cache_miss_tokens;


}
