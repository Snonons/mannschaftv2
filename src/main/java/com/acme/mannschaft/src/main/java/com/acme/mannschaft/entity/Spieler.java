package com.acme.mannschaft.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
import lombok.*;

/**
 * Vor- und Nachname des Spielers.
 */
@Entity
@Table(name = "spieler")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Spieler {
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Muster für einen gültigen Nachnamen.
     */
    public static final String NACHNAME_PATTERN =
        "(o'|von|von der|von und zu|van)?[A-ZÄÖÜ][a-zäöüß]+(-[A-ZÄÖÜ][a-zäöüß]+)?";

    /**
     * Der Vorname des Spielers.
     */
    @NotNull
    private String vorname;

    /**
     * Der Nachname des Spielers.
     */
    @Valid
    @Pattern(regexp = NACHNAME_PATTERN)
    @NotNull
    private String nachname;
}
