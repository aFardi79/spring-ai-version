package ir.dotin.model.bitbucket;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Segment {

    private String type; // CONTEXT, ADDED, REMOVED
    private List<Line> lines;
    private boolean truncated;
}
