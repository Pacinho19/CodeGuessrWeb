package pl.pacinho.codeguessrweb.tools;

import pl.pacinho.codeguessrweb.model.project.Code;
import pl.pacinho.codeguessrweb.model.project.dto.LineDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class CodeLineTools {
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
}
