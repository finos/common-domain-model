package cdm.legaldocumentation.csa.processor;

import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.legaldocumentation.csa.PostingObligations;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * CreateiQ mapping processor.
 */
@SuppressWarnings("unused")
public class SecurityProviderMappingProcessor extends MappingProcessor {

    private static final String XML_VALUE_BOTH = "both";

    public SecurityProviderMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public <T> void mapBasic(Path synonymPath, Collection<? extends T> instance, RosettaModelObjectBuilder parent) {
        setValueAndUpdateMappings(synonymPath, xmlValue -> {
            if (XML_VALUE_BOTH.equals(xmlValue)) {
                ((PostingObligations.PostingObligationsBuilder) parent)
                        .setSecurityProvider(Arrays.asList(CounterpartyRoleEnum.PARTY_1, CounterpartyRoleEnum.PARTY_2));
            }
        });
    }
}
