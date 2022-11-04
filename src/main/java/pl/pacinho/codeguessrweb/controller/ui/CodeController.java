package pl.pacinho.codeguessrweb.controller.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.pacinho.codeguessrweb.config.UIConfig;
import pl.pacinho.codeguessrweb.model.project.CodeContentDto;
import pl.pacinho.codeguessrweb.tools.CodeTools;
import pl.pacinho.codeguessrweb.utils.StringUtils;

@RequiredArgsConstructor
@Controller
public class CodeController {

    @PostMapping(UIConfig.CODE_CONTENT)
    public String getCode(Model model, @RequestBody CodeContentDto codeContentDto) {
        model.addAttribute("codeContent", CodeTools.getCode(codeContentDto));
        return "fragments/code-content :: codeContentFrag";
    }
}
