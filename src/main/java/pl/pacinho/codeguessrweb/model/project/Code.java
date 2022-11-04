package pl.pacinho.codeguessrweb.model.project;

import java.util.List;

public record Code(String projectName, String filePath, int lineIndex, List<String> fullCode) {

}
