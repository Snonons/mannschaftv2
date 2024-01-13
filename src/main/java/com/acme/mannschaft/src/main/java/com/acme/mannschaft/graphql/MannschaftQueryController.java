package com.acme.mannschaft.graphql;

import com.acme.mannschaft.entity.Mannschaft;
import com.acme.mannschaft.service.MannschaftReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyMap;

/**
 * Eine Controller-Klasse für das Lesen mit der GraphQL-Schnittstelle und den Typen aus dem GraphQL-Schema.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
class MannschaftQueryController {
    private final MannschaftReadService service;

    /**
     * Suche anhand der Mannschaft-ID.
     *
     * @param id ID der zu suchenden Mannschaft
     * @return Die gefundene Mannschaft
     */
    @QueryMapping("mannschaft")
    Mannschaft findById(@Argument final UUID id) {
        log.debug("findById: id={}", id);
        final var mannschaft = service.findById(id);
        log.debug("findById: mannschaft={}", mannschaft);
        return mannschaft;
    }

    /**
     * Suche mit diversen Suchkriterien.
     *
     * @param input Suchkriterien und ihre Werte, z.B. der Name der Mannschaft
     * @return Die gefundenen Mannschaften als Collection
     */
    @QueryMapping("mannschaften")
    Collection<Mannschaft> find(@Argument final Optional<Suchkriterien> input) {
        log.debug("find: suchkriterien={}", input);
        final var suchkriterien = input.map(Suchkriterien::toMap).orElse(emptyMap());
        final var mannschaften = service.find(suchkriterien);
        log.debug("find: mannschaften={}", mannschaften);
        return mannschaften;
    }
}
