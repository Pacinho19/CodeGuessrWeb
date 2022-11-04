package pl.pacinho.codeguessrweb.tools;

import pl.pacinho.codeguessrweb.config.projects.ProjectsSourcesConfig;
import pl.pacinho.codeguessrweb.model.enums.project.Projects;
import pl.pacinho.codeguessrweb.model.project.Node;
import pl.pacinho.codeguessrweb.model.project.Project;
import pl.pacinho.codeguessrweb.utils.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ProjectsTreeTool {

    private final Projects projectType;
    private HashMap<String, Node> nodes;
    private Project project;

    public ProjectsTreeTool(Projects project) {
        this.projectType = project;
        nodes = new HashMap<>();
    }

    public Project getSchema() {
        project = new Project(projectType.name());
        listEntries(ProjectsSourcesConfig.PROJECTS_PATH + projectType.getDirectory());
        return project;
    }

    private void listEntries(String path) {

        File file = new File(path);
        if (!file.exists())
            throw new IllegalStateException("Project " + projectType.name() + " sources not found!");

        List<File> projectFiles = Arrays.stream(file.listFiles()).toList();
        projectFiles.forEach(f -> {
            if (f.getAbsolutePath().contains("\\src")) {
                if (!checkDirAccess(f)) return;

                Node node = getNode(f);
                if (file.getAbsoluteFile().isDirectory() && file.getName().equals(projectType.getDirectory()))
                    project.addNode(node);
                else
                    addChild(path, node);

                if (f.isDirectory())
                    listEntries(f.getAbsolutePath());
                else
                    addFileContent(f);

            }
        });
    }

    private Node getNode(File file) {
        return nodes.computeIfAbsent(file.getAbsolutePath(), s -> new Node(getNodeId(file), file.getName(), file.isFile()));
    }

    private String getNodeId(File file) {
        return file.getAbsolutePath().replace(ProjectsSourcesConfig.PROJECTS_PATH, "");
    }

    private boolean checkDirAccess(File f) {
        if (f.isFile()) return true;
        return f.isDirectory()
               && !f.getName().equals("test")
               && !f.getName().equals("resources");
    }

    private void addFileContent(File file) {
        project.addFileContent(file.getAbsolutePath(), FileUtils.readAsText(file));
    }

    private void addChild(String path, Node node) {
        getParent(path)
                .addChild(node);
    }

    private Node getParent(String path) {
        return nodes.get(path);
    }
}
