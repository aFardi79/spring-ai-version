package ir.dotin.model.bitbucket;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Line {

    private int source;
    private int destination;
    private String line;
    private boolean truncated;
}
