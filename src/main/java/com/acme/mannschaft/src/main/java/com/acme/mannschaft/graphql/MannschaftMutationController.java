package com.acme.mannschaft.graphql;

import com.acme.mannschaft.entity.Mannschaft;
import com.acme.mannschaft.service.ConstraintViolationsException;
import com.acme.mannschaft.service.MannschaftWriteService;
import graphql.GraphQLError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.graphql.execution.ErrorType.BAD_REQUEST;

/**
 * Eine Controller-Klasse für das Schreiben mit der GraphQL-Schnittstelle und den Typen aus dem GraphQL-Schema.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
class MannschaftMutationController {
    private final MannschaftWriteService service;
    private final MannschaftInputMapper mapper;

    /**
     * Eine neue Mannschaft anlegen.
     *
     * @param input Die Eingabedaten für eine neue Mannschaft
     * @return Die generierte ID für den neue Mannschaft als Payload
     */
    @MutationMapping
    CreatePayload create(@Argument final MannschaftInput input) {
        log.debug("create: input={}", input);
        final var mannschaftNew = mapper.toMannschaft(input);
        final var id = service.create(mannschaftNew).getId();
        log.debug("create: id={}", id);
        return new CreatePayload(id);
    }

    @GraphQlExceptionHandler
    @SuppressWarnings("unused")
    Collection<GraphQLError> onConstraintViolations(final ConstraintViolationsException ex) {
        return ex.getViolations()
            .stream()
            .map(this::violationToGraphQLError)
            .collect(Collectors.toList());
    }

    private GraphQLError violationToGraphQLError(final ConstraintViolation<Mannschaft> violation) {
        final List<Object> path = new ArrayList<>(5);
        path.add("input");
        for (final Path.Node node: violation.getPropertyPath()) {
            path.add(node.toString());
        }
        return GraphQLError.newError()
            .errorType(BAD_REQUEST)
            .message(violation.getMessage())
            .path(path)
            .build();
    }
}
