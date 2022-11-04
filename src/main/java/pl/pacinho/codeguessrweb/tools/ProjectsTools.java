package pl.pacinho.codeguessrweb.tools;

import pl.pacinho.codeguessrweb.model.enums.project.Projects;
import pl.pacinho.codeguessrweb.model.project.Project;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ProjectsTools {

    public static List<Project> getProjects() {
        return Arrays.stream(Projects.values())
                .map(p -> new ProjectsTreeTool(p).getSchema())
                .filter(Objects::nonNull)
                .toList();
    }
}
