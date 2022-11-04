package pl.pacinho.codeguessrweb.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.pacinho.codeguessrweb.config.UIConfig;

@Controller
public class HomeController {

    @GetMapping
    public String home() {
        return "redirect:" + UIConfig.HOME;
    }
}