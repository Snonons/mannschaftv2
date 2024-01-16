package com.acme.mannschaft.rest;

import com.acme.mannschaft.service.MannschaftReadService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import static com.acme.mannschaft.rest.MannschaftGetController.REST_PATH;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Eine Controller-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Methoden der Klasse abgebildet werden.
 */
@RestController
@RequestMapping(REST_PATH)
@OpenAPIDefinition(info = @Info(title = "Mannschaft API", version = "v1"))
@RequiredArgsConstructor
@Slf4j
public class MannschaftGetController {
    /**
     * Basispfad für die REST-Schnittstelle.
     */
    public static final String REST_PATH = "/rest";

    /**
     * Muster für eine UUID.
     */
    public static final String ID_PATTERN = "[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}";
    private final MannschaftReadService service;
    private final UriHelper uriHelper;

    /**
     * Suche anhand der Mannschaft-ID als Pfad-Parameter.
     *
     * @param id ID des zu suchenden Mannschaften
     * @param request Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Gefundene Mannschaft mit Atom-Links.
     */
    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Suche mit der Mannschaft-ID", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Mannschaft gefunden")
    @ApiResponse(responseCode = "404", description = "Mannschaft nicht gefunden")
    MannschaftModel getById(@PathVariable final UUID id, final HttpServletRequest request) {
        log.debug("getById: id={}, Thread={}", id, Thread.currentThread().getName());

        // Geschaeftslogik bzw. Anwendungskern
        final var mannschaft = service.findById(id);

        // HATEOAS
        final var model = new MannschaftModel(mannschaft);
        // evtl. Forwarding von einem API-Gateway
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var idUri = STR."\{baseUri}/\{mannschaft.getId()}";
        final var selfLink = Link.of(idUri);
        final var listLink = Link.of(baseUri, LinkRelation.of("list"));
        final var addLink = Link.of(baseUri, LinkRelation.of("add"));
        final var updateLink = Link.of(idUri, LinkRelation.of("update"));
        final var removeLink = Link.of(idUri, LinkRelation.of("remove"));
        model.add(selfLink, listLink, addLink, updateLink, removeLink);

        log.debug("getById: {}", model);
        return model;
    }

    /**
     * Suche mit diversen Suchkriterien als Query-Parameter.
     *
     * @param suchkriterien Query-Parameter als Map.
     * @param request Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Gefundene Mannschaft als CollectionModel.
     */
    @GetMapping(produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche mit Suchkriterien", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Collection mit den Mannschaften")
    @ApiResponse(responseCode = "404", description = "Keine Mannschaften gefunden")
    CollectionModel<MannschaftModel> get(
        @RequestParam @NonNull final MultiValueMap<String, String> suchkriterien,
        final HttpServletRequest request
    ) {
        log.debug("get: suchkriterien={}", suchkriterien);

        final var baseUri = uriHelper.getBaseUri(request).toString();

        // Geschaeftslogik bzw. Anwendungskern
        final var models = service.find(suchkriterien)
            .stream()
            .map(mannschaft -> {
                final var model = new MannschaftModel(mannschaft);
                model.add(Link.of(STR."\{baseUri}/\{mannschaft.getId()}"));
                return model;
            })
            .toList();

        log.debug("get: {}", models);
        return CollectionModel.of(models);
    }
}
