package cdm.legalagreement.csa.processor;

import cdm.legalagreement.csa.RecalculationOfValue;
import cdm.legalagreement.csa.RecalculationOfValueElection;
import cdm.legalagreement.csa.RecalculationOfValueElectionEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.isda.cdm.processor.CdmMappingProcessorUtils.getEnumValue;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.synonymToEnumValueMap;
import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.*;

public class RecalculationOfValueMappingProcessor extends MappingProcessor {

    private static final List<String> SUFFIXES = Arrays.asList("_recalculation_of_market_value", "_recalculation_of_value");
    private static final List<String> TERMS_SUFFIXES = Arrays.asList("_recalculation_of_value_terms", "_recalculation_of_market_value_terms");

    private final Map<String, RecalculationOfValueElectionEnum> recalculationOfValueElectionEnumMap;

    public RecalculationOfValueMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths,
                                                MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
        this.recalculationOfValueElectionEnumMap = synonymToEnumValueMap(RecalculationOfValueElectionEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        RecalculationOfValue.RecalculationOfValueBuilder valueBuilder = (RecalculationOfValue.RecalculationOfValueBuilder) builder;

        SUFFIXES.forEach(suffix -> PARTIES.forEach(party -> getCalculationCurrencyElection(synonymPath, party, suffix).ifPresent(valueBuilder::addPartyElection)));

    }

    private Optional<RecalculationOfValueElection> getCalculationCurrencyElection(Path synonymPath, String party, String suffix) {
        RecalculationOfValueElection.RecalculationOfValueElectionBuilder recalculationOfValueElectionBuilder = RecalculationOfValueElection.builder();
        setValueAndUpdateMappings(synonymPath.addElement(party + suffix),
                (value) -> {
                    recalculationOfValueElectionBuilder.setParty(toCounterpartyRoleEnum(party));
                    if ("other".equals(value)) {
                        TERMS_SUFFIXES.forEach(termSuffix -> setValueAndUpdateMappings(synonymPath.addElement(party + termSuffix),
                                (terms) -> recalculationOfValueElectionBuilder.setRecalculationOfValueTerms(value)));
                    } else {
                        getEnumValue(recalculationOfValueElectionEnumMap, value, RecalculationOfValueElectionEnum.class)
                                .ifPresent(recalculationOfValueElectionBuilder::setRecalculationOfValueElection);
                    }
                });
        return recalculationOfValueElectionBuilder.hasData() ? Optional.of(recalculationOfValueElectionBuilder.build()) : Optional.empty();
    }
}