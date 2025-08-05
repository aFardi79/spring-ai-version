package ir.dotin.model.domain;


import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Setter
@Getter
public class DeepSeekMessageRequest {
    private String role;
    private String content;
}
