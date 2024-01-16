package com.acme.mannschaft.graphql;

import com.acme.mannschaft.entity.Mannschaft;
import com.acme.mannschaft.entity.Spieler;
import com.acme.mannschaft.entity.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

/**
 * Mapper zwischen Entity-Klassen. Siehe build\generated\sources\annotationProcessor\java\...\MannschaftInputMapperImpl.java.
 */
@Mapper(nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
interface MannschaftInputMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "erzeugt", ignore = true)
    @Mapping(target = "aktualisiert", ignore = true)
    @Mapping(target = "spielerStr", ignore = true)
    Mannschaft toMannschaft(MannschaftInput input);

    @Mapping(target = "id", ignore = true)
    Spieler toSpieler(SpielerInput input);

    @Mapping(target = "id", ignore = true)
    Trainer toTrainer(TrainerInput input);
}
