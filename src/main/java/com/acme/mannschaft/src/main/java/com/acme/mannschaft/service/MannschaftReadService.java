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
package com.acme.mannschaft.service;

import com.acme.mannschaft.entity.Mannschaft;
import com.acme.mannschaft.repository.MannschaftRepository;
import java.util.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Anwendungslogik für Mannschaften.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MannschaftReadService {
    private final MannschaftRepository repo;

    /**
     * Eine Mannschaft anhand seiner ID suchen.
     *
     * @param id Die Id der gesuchten Mannschaften
     * @return Die gefundene Mannschaft
     * @throws NotFoundException Falls keine Mannschaft gefunden wurde
     */
    public @NonNull Mannschaft findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var mannschaft = repo.findById(id)
            .orElseThrow(() -> new NotFoundException(id));
        log.debug("findById: {}", mannschaft);
        return mannschaft;
    }

    /**
     * Mannschaften anhand von Suchkriterien als Collection suchen.
     *
     * @param suchkriterien Die Suchkriterien
     * @return Die gefundenen Mannschaften oder eine leere Liste
     * @throws NotFoundException Falls keine Mannschaften gefunden wurden
     */
    @SuppressWarnings({"ReturnCount", "NestedIfDepth"})
    public @NonNull Collection<Mannschaft> find(@NonNull final Map<String, List<String>> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);

        if (suchkriterien.isEmpty()) {
            return repo.findAll();
        }

        final var namen = suchkriterien.get("name");
        if (suchkriterien.size() == 1) {
            final var name = suchkriterien.get("name");
            if (name != null && name.size() == 1) {
                final var mannschaften = repo.findByName(namen.getFirst());
                if (mannschaften.isEmpty()) {
                    throw new NotFoundException(suchkriterien);
                }
                log.debug("find (name): {}", mannschaften);
                return mannschaften;
            }
        }

        final var mannschaften = repo.find(suchkriterien);
        if (mannschaften.isEmpty()) {
            throw new NotFoundException(suchkriterien);
        }
        log.debug("find: {}", mannschaften);
        return mannschaften;
    }
}
