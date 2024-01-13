package com.acme.mannschaft.repository;

import com.acme.mannschaft.entity.Mannschaft;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.IntStream;

import static com.acme.mannschaft.repository.DB.MANNSCHAFTEN;
import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;

/**
 * Repository für den DB-Zugriff bei Mannschaften.
 */
@Repository
@Slf4j
public class MannschaftRepository {
    public Optional<Mannschaft> findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var result = MANNSCHAFTEN.stream()
            .filter(mannschaft -> Objects.equals(mannschaft.getId(), id))
            .findFirst();
        log.debug("findById: {}", result);
        return result;
    }

    @SuppressWarnings({"ReturnCount"})
    public @NonNull Collection<Mannschaft> find(final Map<String, ? extends List<String>> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);

        if (suchkriterien.isEmpty()) {
            return findAll();
        }

        // for-Schleife statt "forEach" wegen return
        for (final var entry : suchkriterien.entrySet()) {
            if (entry.getKey().equals("name")) {
                return findByName(entry.getValue().getFirst());
            }
            log.debug("find: ungueltiges Suchkriterium={}", entry.getKey());
        }
        return emptyList();
    }

    public @NonNull Collection<Mannschaft> findByName(final CharSequence name) {
        log.debug("findByName: name={}", name);
        final var mannschaften = MANNSCHAFTEN.stream()
            .filter(mannschaft -> mannschaft.getName().contains(name))
            .toList();
        log.debug("findByName: mannschaften={}", mannschaften);
        return mannschaften;
    }

    public @NonNull Collection<Mannschaft> findAll() { return MANNSCHAFTEN;}

    public @NonNull Mannschaft create(final @NonNull Mannschaft mannschaft) {
        log.debug("create: {}", mannschaft);
        mannschaft.setId(randomUUID());
        MANNSCHAFTEN.add(mannschaft);
        log.debug("create: {}", mannschaft);
        return mannschaft;
    }

    public void update(final @NonNull Mannschaft mannschaft) {
        log.debug("update: {}", mannschaft);
        final OptionalInt index = IntStream
            .range(0, MANNSCHAFTEN.size())
            .filter(i -> Objects.equals(MANNSCHAFTEN.get(i).getId(), mannschaft.getId()))
            .findFirst();
        log.trace("update: index={}", index);
        if (index.isEmpty()) {
            return;
        }
        MANNSCHAFTEN.set(index.getAsInt(), mannschaft);
        log.debug("update: {}", mannschaft);
    }
}
