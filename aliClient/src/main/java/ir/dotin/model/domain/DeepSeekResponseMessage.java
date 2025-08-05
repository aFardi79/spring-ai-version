package ir.dotin.model.domain;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
public class DeepSeekResponseMessage {
    private String role;
    private String content;
}
