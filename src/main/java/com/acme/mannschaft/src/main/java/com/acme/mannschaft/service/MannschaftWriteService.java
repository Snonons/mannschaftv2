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
import jakarta.validation.Validator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Anwendungslogik für Mannschaften auch mit Bean Validation.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MannschaftWriteService {
    private final MannschaftRepository repo;
    private final Validator validator;

    public Mannschaft create(final Mannschaft mannschaft) {
        log.debug("create: {}", mannschaft);

        final var violations = validator.validate(mannschaft);
        if (!violations.isEmpty()) {
            log.debug("create: violations={}", violations);
            throw new ConstraintViolationsException(violations);
        }
        final var mannschaftDB = repo.create(mannschaft);
        log.debug("create: {}", mannschaftDB);
        return mannschaftDB;
    }

    public void update(final Mannschaft mannschaft, final UUID id) {
        log.debug("update: {}", mannschaft);
        log.debug("update: id={}", id);

        final var violations = validator.validate(mannschaft);
        if (!violations.isEmpty()) {
            log.debug("update: violations={}", violations);
            throw new ConstraintViolationsException(violations);
        }

        final var mannschaftDbOptional = repo.findById(id);
        if (mannschaftDbOptional.isEmpty()) {
            throw new NotFoundException(id);
        }

        mannschaft.setId(id);
        repo.update(mannschaft);
    }
}
