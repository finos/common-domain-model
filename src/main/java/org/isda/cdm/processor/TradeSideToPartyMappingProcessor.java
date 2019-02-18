package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.ReferenceWithMetaParty.ReferenceWithMetaPartyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class TradeSideToPartyMappingProcessor extends MappingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeSideToPartyMappingProcessor.class);

    public TradeSideToPartyMappingProcessor(RosettaPath rosettaPath, List<Mapping> mappings) {
        super(rosettaPath, mappings);
    }

    @Override
    public <R extends RosettaModelObject> void map(RosettaModelObjectBuilder<R> builder) {
        ReferenceWithMetaPartyBuilder partyReference = (ReferenceWithMetaPartyBuilder) builder;
        String tradeSideId = partyReference.getReference();
        getPartyId(tradeSideId)
                .ifPresent(partyId -> {
                    LOGGER.info("Mapped tradeSide.id ({}) to tradeSide.orderer.party.id ({}) at path {}", tradeSideId, partyId, getPath());
                    partyReference.setReference(partyId).build();
                });
    }

    private Optional<String> getPartyId(String tradeSideId) {
        for(Mapping mapping : getMappings()) {
            String xmlValue = String.valueOf(mapping.getXmlValue());
            if(mapping.getXmlPath().endsWith("id") && xmlValue.equals(tradeSideId)) {
                Path partyPath = mapping.getXmlPath()
                        .getParent()
                        .append(Path.valueOf("orderer.party.href"));
                Optional<Mapping> foundMapping = getMappings().stream()
                        .filter(p -> p.getXmlPath().toString().equals(partyPath.toString()))
                        .findFirst();
                Optional<String> partyId = foundMapping
                        .map(Mapping::getXmlValue)
                        .map(String::valueOf);
                if(partyId.isPresent()) {
                    // TODO: update mapping list
                    return partyId;
                }
            }
        }
        return Optional.empty();
    }
}
