package pl.pacinho.codeguessrweb.config.projects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Projects {

    MAPA_TEREN_2("MapaTeren2"),
    PAYMENT_LINK_SERVICE("PaymentLinkService");

    @Getter
    private final String directory;
}
