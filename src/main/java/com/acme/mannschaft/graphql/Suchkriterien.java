package com.acme.mannschaft.graphql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Eine Value-Klasse für Eingabedaten passend zu Suchkriterien aus dem GraphQL-Schema.
 *
 * @param name Name
 */
record Suchkriterien (
    String name
) {
    Map<String, List<String>> toMap() {
        final Map<String, List<String>> map = new HashMap<>(2, 1);
        if (name != null) {
            map.put("name", List.of(name));
        }
        return map;
    }
}
