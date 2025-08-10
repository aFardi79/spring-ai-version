package ir.dotin.model.bitbucket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hunk {


    private int sourceLine;
    private int sourceSpan;
    private int destinationLine;
    private int destinationSpan;
    private List<Segment> segments;
    private boolean truncated;
}
