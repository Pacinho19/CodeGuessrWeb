package pl.pacinho.codeguessrweb.tools;

import pl.pacinho.codeguessrweb.model.enums.project.Projects;
import pl.pacinho.codeguessrweb.model.project.NodeDto;
import pl.pacinho.codeguessrweb.model.project.ProjectDto;

import java.util.HashMap;

public class ProjectsTreeTool {

    private final Projects project;
    private HashMap<String, NodeDto> nodes;
    private ProjectDto projectDto;

    public ProjectsTreeTool(Projects project) {
        this.project = project;
        nodes = new HashMap<>();
    }

    public ProjectDto getSchema() {
        return null;
    }
}
