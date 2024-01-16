/*
 * Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.acme.mannschaft.rest;

import com.acme.mannschaft.service.ConstraintViolationsException;
import com.acme.mannschaft.service.MannschaftWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.UUID;
import static com.acme.mannschaft.rest.MannschaftGetController.ID_PATTERN;
import static com.acme.mannschaft.rest.MannschaftGetController.REST_PATH;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

/**
 * Eine Controller-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Methoden der Klasse abgebildet werden.
 */
@Controller
@RequestMapping(REST_PATH)
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings({"ClassFanOutComplexity", "java:S1075"})
class MannschaftWriteController {
    static final String PROBLEM_PATH = "/problem/";
    private final MannschaftWriteService service;
    private final MannschaftMapper mapper;
    private final UriHelper uriHelper;

    /**
     * Einen neuen Mannschaft-Datensatz anlegen.
     *
     * @param mannschaftDTO Das Mannschaftsobjekt aus dem eingegangenen Request-Body.
     * @param request Das Request-Objekt, um `Location` im Response-Header zu erstellen.
     * @return Response mit Statuscode 201 einschließlich Location-Header oder Statuscode 422 falls Constraints verletzt
     *      sind oder die Emailadresse bereits existiert oder Statuscode 400 falls syntaktische Fehler im Request-Body
     *      vorliegen.
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Einen neuen Mannschaften anlegen", tags = "Neuanlegen")
    @ApiResponse(responseCode = "201", description = "Mannschaft neu angelegt")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte vorhanden")
    ResponseEntity<Void> post(@RequestBody final MannschaftDTO mannschaftDTO, final HttpServletRequest request) {
        log.debug("post: {}", mannschaftDTO);

        final var mannschaftInput = mapper.toMannschaft(mannschaftDTO);
        final var mannschaft = service.create(mannschaftInput);
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var location = URI.create(STR."\{baseUri}/\{mannschaft.getId()}");
        return created(location).build();
    }

    /**
     * Einen vorhandenen Mannscaft-Datensatz überschreiben.
     *
     * @param id ID des zu aktualisierenden Mannschaften.
     * @param mannschaftDTO Das Mannschaftsobjekt aus dem eingegangenen Request-Body.
     */
    @PutMapping(path = "{id:" + ID_PATTERN + "}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Mannschaften mit neuen Werten aktualisieren", tags = "Aktualisieren")
    @ApiResponse(responseCode = "204", description = "Aktualisiert")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "404", description = "Mannschaft nicht vorhanden")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte vorhanden")
    void put(@PathVariable final UUID id, @RequestBody final MannschaftDTO mannschaftDTO) {
        log.debug("put: id={}, {}", id, mannschaftDTO);

        final var mannschaftInput = mapper.toMannschaft(mannschaftDTO);
        service.update(mannschaftInput, id);
    }

    @ExceptionHandler
    ProblemDetail onConstraintViolations(
        final ConstraintViolationsException ex,
        final HttpServletRequest request
    ) {
        log.debug("onConstraintViolations: {}", ex.getMessage());

        final var mannschaftViolations = ex.getViolations()
            .stream()
            .map(violation -> STR."\{violation.getPropertyPath()}: " +
                STR."\{violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()} " +
                violation.getMessage())
            .toList();
        log.trace("onConstraintViolations: {}", mannschaftViolations);
        final String detail;
        if (mannschaftViolations.isEmpty()) {
            detail = "N/A";
        } else {
            final var violationsStr = mannschaftViolations.toString();
            detail = violationsStr.substring(1, violationsStr.length() - 2);
        }

        final var problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, detail);
        problemDetail.setType(URI.create(STR."\{PROBLEM_PATH}\{ProblemType.CONSTRAINTS.getValue()}"));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));

        return problemDetail;
    }

    @ExceptionHandler
    ProblemDetail onMessageNotReadable(
        final HttpMessageNotReadableException ex,
        final HttpServletRequest request
    ) {
        log.debug("onMessageNotReadable: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create(STR."\{PROBLEM_PATH}\{ProblemType.BAD_REQUEST.getValue()}"));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }
}
