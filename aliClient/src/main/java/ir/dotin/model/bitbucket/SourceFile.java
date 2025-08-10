package ir.dotin.model.bitbucket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SourceFile {

    private List<String> components;
    private String parent;
    private String name;
    private String extension;
    private String toString;
}
