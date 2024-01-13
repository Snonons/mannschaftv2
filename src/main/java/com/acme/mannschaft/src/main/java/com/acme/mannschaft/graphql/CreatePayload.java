package com.acme.mannschaft.graphql;

import java.util.UUID;

/**
 * Value-Klasse für das Resultat, wenn an der GraphQL-Schnittstelle eine neue Mannschaft angelegt wurde.
 *
 * @param id ID der neu angelegten Mannschaft
 */
record CreatePayload (UUID id) {
}
