package org.isda.cdm.processor;

import static org.isda.cdm.processor.CdmMappingProcessorUtils.PARTIES;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.isda.cdm.SecurityProviderRightsEvent;
import org.isda.cdm.SecurityProviderRightsEventElection;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

public class SecurityProviderRightsEventMappingProcessor extends MappingProcessor {

    private static final List<String> SUFFIXES = Arrays.asList("_pledgor_rights_event", "_chargor_rights_event", "_obligor_rights_event", "_security_provider_rights_event");


    public SecurityProviderRightsEventMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths,
                                                       MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }


    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {

        SecurityProviderRightsEvent.SecurityProviderRightsEventBuilder securityProviderRightsEventBuilder = (SecurityProviderRightsEvent.SecurityProviderRightsEventBuilder) builder;
        PARTIES.forEach(party ->
                SUFFIXES.forEach(suffix ->
                        getSecurityProviderRightsEventElection(synonymPath, party, suffix).ifPresent(securityProviderRightsEventBuilder::addPartyElection)
                )
        );
    }

    private Optional<SecurityProviderRightsEventElection> getSecurityProviderRightsEventElection(Path synonymPath, String party, String suffix) {
        SecurityProviderRightsEventElection.SecurityProviderRightsEventElectionBuilder securityProviderRightsEventElectionBuilder = SecurityProviderRightsEventElection.builder();
        setValueAndUpdateMappings(synonymPath.addElement(party + suffix),
                (value) -> {
                    securityProviderRightsEventElectionBuilder.setParty(party);
					securityProviderRightsEventElectionBuilder.setRightsEvent(value.equals("applicable"));
                });
        return securityProviderRightsEventElectionBuilder.hasData() ? Optional.of(securityProviderRightsEventElectionBuilder.build()) : Optional.empty();
    }
}
