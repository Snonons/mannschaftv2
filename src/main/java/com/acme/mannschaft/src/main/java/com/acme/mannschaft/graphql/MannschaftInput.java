package com.acme.mannschaft.graphql;

import java.util.List;

/**
 * Eine Value-Klasse für Eingabedaten passend zu MannschaftInput aus dem GraphQL-Schema.
 *
 * @param name Name
 * @param gruendungsjahr Gründungsjahr
 * @param spielerList Liste der Spieler
 * @param trainer Trainer
 */
record MannschaftInput (
    String name,
    String gruendungsjahr,
    List<SpielerInput> spielerList,
    TrainerInput trainer
){
}
