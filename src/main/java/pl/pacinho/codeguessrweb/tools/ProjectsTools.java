package pl.pacinho.codeguessrweb.tools;

import pl.pacinho.codeguessrweb.model.enums.project.Projects;
import pl.pacinho.codeguessrweb.model.project.Project;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ProjectsTools {

    private static List<Project> projects = initProjects();

    private static List<Project> initProjects() {
        return Arrays.stream(Projects.values())
                .map(p -> new ProjectsTreeTool(p).getSchema())
                .filter(Objects::nonNull)
                .toList();
    }

    public static List<Project> getProjects() {
        return projects;
    }

    public static Project getProject(String projectName) {
        return projects.stream()
                .filter(p -> p.getName().equals(projectName))
                .findFirst()
                .orElse(null);
    }

}
