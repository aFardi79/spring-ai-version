package ir.dotin.model.domain;


import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Getter
@Setter
public class DeepSeekChoice {

    private int index;
    private DeepSeekResponseMessage message;
    private String logprobs;
    private String finish_reason;


}
