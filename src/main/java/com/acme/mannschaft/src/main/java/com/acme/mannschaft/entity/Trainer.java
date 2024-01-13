package com.acme.mannschaft.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.util.UUID;

/**
 * Vor- und Nachname des Spielers.
 */

@Entity
@Table(name = "trainer")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Trainer {
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Muster für einen gültigen Nachnamen.
     */
    public static final String NACHNAME_PATTERN =
        "(o'|von|von der|von und zu|van)?[A-ZÄÖÜ][a-zäöüß]+(-[A-ZÄÖÜ][a-zäöüß]+)?";

    /**
     * Der Vorname des Trainers.
     */
    @NotNull
    private String vorname;

    /**
     * Der Nachname des Trainers.
     */
    @Valid
    @Pattern(regexp = NACHNAME_PATTERN)
    @NotNull
    private String nachname;
}
