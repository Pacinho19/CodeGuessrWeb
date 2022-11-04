package pl.pacinho.codeguessrweb.model.project;

import lombok.Getter;
import lombok.Setter;
import pl.pacinho.codeguessrweb.model.project.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
public class Project {

    private String name;
    @Setter
    private Set<Node> nodes;
    @Getter
    private Map<String, String> filesContent;

    public Project(String name) {
        this.name = name;
        nodes = new HashSet<>();
        filesContent = new HashMap<>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addFileContent(String name, String content) {
        filesContent.put(name, content);
    }
}
