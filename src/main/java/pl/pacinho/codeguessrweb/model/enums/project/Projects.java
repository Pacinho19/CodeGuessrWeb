package pl.pacinho.codeguessrweb.model.enums.project;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum Projects {

    MAPA_TEREN_2("MapaTeren2"),
    PAYMENT_LINK_SERVICE("PaymentLinkService");

    @Getter
    private final String directory;

    public static Projects findByDirectory(String directory) {
        return Arrays.stream(Projects.values())
                .filter(p -> p.getDirectory().equals(directory))
                .findFirst()
                .orElse(null);
    }
}
