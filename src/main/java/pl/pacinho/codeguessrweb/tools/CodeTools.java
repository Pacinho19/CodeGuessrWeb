package pl.pacinho.codeguessrweb.tools;

import pl.pacinho.codeguessrweb.model.enums.project.Projects;
import pl.pacinho.codeguessrweb.model.project.Code;
import pl.pacinho.codeguessrweb.model.project.CodeContentDto;
import pl.pacinho.codeguessrweb.model.project.Project;
import pl.pacinho.codeguessrweb.model.project.dto.LineDto;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

public class CodeTools {
    public static List<LineDto> getLines(Code code) {
        return IntStream.rangeClosed(code.lineIndex() - 1, code.lineIndex() + 1)
                .boxed()
                .map(idx -> getLine(idx, code))
                .filter(Objects::nonNull)
                .toList();
    }

    private static LineDto getLine(Integer idx, Code code) {
        List<String> fullCode = code.fullCode();

        if (idx < 0) return null;
        if (idx >= fullCode.size()) return null;
        return new LineDto(fullCode.get(idx), idx == code.lineIndex());
    }

    public static String getCode(CodeContentDto codeContentDto) {
        if (codeContentDto == null) return null;
        if (codeContentDto.getParent().equals("0")) return null;

        Projects projectsEnum = getProjectsEnum(getProjectDir(codeContentDto.getParent()));
        if (projectsEnum == null) return null;

        Project project = ProjectsTools.getProject(projectsEnum.name());
        String targetFileName = getFileName(codeContentDto,projectsEnum);
        for (Map.Entry<String, String> entry : project.getFilesContent().entrySet()) {
            String path = entry.getKey();
            String content = entry.getValue();
            if (path.endsWith(targetFileName)) return content;
        }

        return null;
    }

    private static String getFileName(CodeContentDto codeContentDto, Projects project) {
        return codeContentDto.getParent().replaceFirst(project.name(), project.getDirectory()).replace("/", "\\") + "\\" + codeContentDto.getName();
    }

    private static String getProjectDir(String parent) {
        if (!parent.contains("/")) return parent;
        return parent.split("/")[0];
    }

    private static Projects getProjectsEnum(String dir) {
        return Projects.findByDirectory(dir);
    }
}
