package pl.pacinho.codeguessrweb.service;

import org.springframework.stereotype.Service;
import pl.pacinho.codeguessrweb.model.project.Node;
import pl.pacinho.codeguessrweb.model.project.Project;
import pl.pacinho.codeguessrweb.model.project.dto.NodeDto;
import pl.pacinho.codeguessrweb.tools.ProjectsTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TreeNodeService {

    public List<NodeDto> getNodes() {
        List<NodeDto> nodes = new ArrayList<>();
        ProjectsTools.getProjects()
                .forEach(p -> nodes.addAll(getProjectNodes(p)));

        return nodes;
    }

    private List<NodeDto> getProjectNodes(Project p) {
        List<NodeDto> out = new ArrayList<>();
        out.add(new NodeDto(p.getName(), "0", p.getName(), true));
        getChildNodes(p.getName(), p.getNodes(), out);
        return out;
    }

    private void getChildNodes(String parentId, Set<Node> nodes, List<NodeDto> out) {

        nodes.forEach(n -> {
            out.add(new NodeDto(n.getId(), parentId, n.getName(), !n.isFile()));
            if (!n.getChildren().isEmpty()) getChildNodes(n.getId(), n.getChildren(), out);
        });
    }
}
