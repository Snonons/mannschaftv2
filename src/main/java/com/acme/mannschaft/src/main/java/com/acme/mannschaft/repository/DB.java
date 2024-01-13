package com.acme.mannschaft.repository;

import com.acme.mannschaft.entity.Mannschaft;
import com.acme.mannschaft.entity.Spieler;
import com.acme.mannschaft.entity.Trainer;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Emulation der Datenbasis für persistente Mannschaften.
 */
final class DB {
    static final List<Mannschaft> MANNSCHAFTEN;

    static {
        MANNSCHAFTEN = Stream.of(
            Mannschaft.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .name("FC Ballspielverein")
                .gruendungsjahr(LocalDate.of(2000, 1, 1))
                .spielerList(Stream.of(
                    Spieler.builder().vorname("Eins").nachname("Mustermann").build(),
                    Spieler.builder().vorname("Zwei").nachname("Musterman").build(),
                    Spieler.builder().vorname("Drei").nachname("Musterfrau").build()
                ).collect(Collectors.toList()))
                .trainer(Trainer.builder().vorname("Ein").nachname("Trainer").build())
                .build(),
            Mannschaft.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000011"))
                .name("Karlsruher SC")
                .gruendungsjahr(LocalDate.of(1894, 7, 6))
                .spielerList(Stream.of(
                    Spieler.builder().vorname("Lars").nachname("Stindl").build(),
                    Spieler.builder().vorname("Marvin").nachname("Wanitzek").build(),
                    Spieler.builder().vorname("Fabian").nachname("Schleusener").build(),
                    Spieler.builder().vorname("Jerome").nachname("Gondorf").build()
                ).collect(Collectors.toList()))
                .trainer(Trainer.builder().vorname("Christian").nachname("Eichner").build())
                .build()
        )
        .collect(Collectors.toList());
    }
}
