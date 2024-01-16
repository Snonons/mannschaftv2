package com.acme.mannschaft.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;

/**
 * Daten einer Mannschaft. In DDD ist Mannschaft ein Aggregate Root.
 */
@Entity
@Table(name = "mannschaft")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString
public class Mannschaft {
    private static final int USERNAME_MAX_LENGTH = 20;

    /**
     * Die ID der Mannschaft.
     */
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Versionsnummer für optimistische Synchronisation.
     */
    @Version
    private int version;

    /**
     * Der Name der Mannschaft.
     */
    @NotNull
    private String name;

    /**
     * Das Gründungsjahr der Mannschaft.
     */
    @Past
    private LocalDate gruendungsjahr;

    /**
     * Liste der Spieler, die der Mannschaft angehören.
     */
    @OneToMany(cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    @Valid
    @NotNull
    private List<Spieler> spielerList;

    @Column(name = "spieler")
    private String spielerStr;

    /**
     * Der Trainer der Mannschaft.
     */
    @OneToOne(optional = false, cascade = {PERSIST, REMOVE}, fetch = LAZY, orphanRemoval = true)
    @NotNull
    private Trainer trainer;

    @Size(max = USERNAME_MAX_LENGTH)
    private String username;

    @CreationTimestamp
    private LocalDateTime erzeugt;

    @UpdateTimestamp
    private LocalDateTime aktualisiert;
}
