package pl.pacinho.codeguessrweb.model.project;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Node {

    private String id;
    private String name;
    private boolean file;
    private Set<Node> children;

    public Node(String id, String name, boolean isFile) {
        this.name = name;
        this.id = id;
        this.file = isFile;
        children = new HashSet<>();
    }

    public void addChild(Node child) {
        children.add(child);
    }
}
