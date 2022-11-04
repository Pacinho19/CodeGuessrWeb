package pl.pacinho.codeguessrweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pacinho.codeguessrweb.config.UIConfig;
import pl.pacinho.codeguessrweb.model.project.dto.NodeDto;
import pl.pacinho.codeguessrweb.service.TreeNodeService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TreeNodesController {

    private final TreeNodeService treeNodeService;

    @GetMapping(UIConfig.GAME_TREE_NODES)
    public List<NodeDto> getNodes() {
        return treeNodeService.getNodes();
    }
}
