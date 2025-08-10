package ir.dotin.model.bitbucket;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Diff {
    private SourceFile source;
    private DestinationFile destination;
    private List<Hunk> hunks;
    private boolean truncated;
}
