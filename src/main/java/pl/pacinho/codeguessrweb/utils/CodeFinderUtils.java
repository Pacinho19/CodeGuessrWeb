package pl.pacinho.codeguessrweb.utils;

import pl.pacinho.codeguessrweb.model.project.Code;
import pl.pacinho.codeguessrweb.model.project.Project;
import pl.pacinho.codeguessrweb.tools.ProjectsTools;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


public class CodeFinderUtils {


    public static Code getRandomCode() {
        List<Project> projects = ProjectsTools.getProjects();
        Project project = projects.get(RandomUtils.getInt(0, projects.size() - 1));
        Collection<String> contents = project.getFilesContent().keySet();
        String filePath = contents.stream()
                .toList()
                .get(RandomUtils.getInt(0, contents.size() - 1));

        List<String> allLines = Arrays.stream(project.getFilesContent()
                        .get(filePath)
                        .split("\n"))
                .toList();

        List<String> lines = allLines.stream()
                .filter(s -> s.trim().length() > 15)
                .toList();

        String text = lines.get(RandomUtils.getInt(0, lines.size() - 1));

        System.out.println(filePath + " " + allLines.indexOf(text));
        return new Code(UUID.randomUUID().toString(), project.getName(), filePath, allLines.indexOf(text), allLines);
    }
}