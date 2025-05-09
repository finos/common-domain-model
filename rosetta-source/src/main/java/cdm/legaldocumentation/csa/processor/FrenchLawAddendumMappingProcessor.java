package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.FrenchLawAddendum;
import cdm.legaldocumentation.csa.FrenchLawAddendumElection;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.*;

@SuppressWarnings("unused")
public class FrenchLawAddendumMappingProcessor extends MappingProcessor {
    public FrenchLawAddendumMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        FrenchLawAddendum.FrenchLawAddendumBuilder frenchLawAddendumBuilder = (FrenchLawAddendum.FrenchLawAddendumBuilder) builder;
        PARTIES.forEach(party -> getFrenchLawAddendumElection(synonymPath, party).ifPresent(frenchLawAddendumBuilder::addPartyElection));
    }

    private Optional<FrenchLawAddendumElection> getFrenchLawAddendumElection(Path synonymPath, String party) {
        FrenchLawAddendumElection.FrenchLawAddendumElectionBuilder frenchLawAddendumElectionBuilder = FrenchLawAddendumElection.builder();
        setValueAndUpdateMappings(synonymPath.addElement(party + "_french_law_addendum"),
                (value) -> frenchLawAddendumElectionBuilder.setParty(toCounterpartyRoleEnum(party)).setIsApplicable(value.equals("specify")));
        setValueAndUpdateMappings(synonymPath.addElement(party + "_specify"), value -> frenchLawAddendumElectionBuilder.setAddendumLanguage(removeHtml(value)));
        return frenchLawAddendumElectionBuilder.hasData() ? Optional.of(frenchLawAddendumElectionBuilder.build()) : Optional.empty();
    }
}
