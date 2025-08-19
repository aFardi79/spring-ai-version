package ir.dotin.model.domain;


import lombok.*;

import java.util.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DeepSeekRequest {
    private String model;
    private List<DeepSeekMessageRequest> messages;
    private boolean stream;
}
