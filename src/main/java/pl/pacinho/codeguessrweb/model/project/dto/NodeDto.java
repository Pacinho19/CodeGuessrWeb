package pl.pacinho.codeguessrweb.model.project.dto;

import lombok.Getter;
import pl.pacinho.codeguessrweb.utils.StringUtils;

@Getter
public class NodeDto {

    private String nodeId; // node id
    private String pid; // parent id
    private String name;
    private boolean dir;

    public NodeDto(String nodeId, String pid, String name, boolean dir) {
        this.nodeId = StringUtils.replaceSlashes(nodeId);
        this.pid = StringUtils.replaceSlashes(pid);
        this.name = name;
        this.dir = dir;
    }
}
