package pl.pacinho.codeguessrweb.model.mapper;

import pl.pacinho.codeguessrweb.model.project.Code;
import pl.pacinho.codeguessrweb.model.project.dto.CodeDto;
import pl.pacinho.codeguessrweb.tools.CodeLineTools;

public class CodeDtoMapper {
    public static CodeDto parse(Code code) {
        return new CodeDto(
                CodeLineTools.getLines(code)
        );
    }
}
