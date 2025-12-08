package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.PostingObligations;
import cdm.legaldocumentation.csa.PostingObligationsElection;
import cdm.product.collateral.CollateralTreatment;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.removeHtml;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.toCounterpartyRoleEnum;

public class PostingObligationsMappingProcessor extends MappingProcessor {

    public PostingObligationsMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths,
                                              MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        PostingObligations.PostingObligationsBuilder obligationsBuilder = (PostingObligations.PostingObligationsBuilder) builder;
        PARTIES.forEach(party -> getPostingObligationsElectionBuilder(synonymPath, party).ifPresent(obligationsBuilder::addPartyElection));
    }

    private Optional<PostingObligationsElection> getPostingObligationsElectionBuilder(Path synonymPath, String party) {
        PostingObligationsElection.PostingObligationsElectionBuilder postingObligationsElectionBuilder = PostingObligationsElection.builder();
        // if we find partyX_type, then set partyX as the party
        setValueAndUpdateMappings(synonymPath.addElement(party + "_type"), value -> postingObligationsElectionBuilder.setParty(toCounterpartyRoleEnum(party)));

        // if we find additional_language, then set additional_language as the additional party
        setValueAndUpdateMappings(synonymPath.addElement("additional_language"), value -> postingObligationsElectionBuilder.setAdditionalLanguage(removeHtml(value)));

        // if we find partyX_additional_language, then set partyX_additional_language as the additional party - this overrides the non party specific additional party setting.
        setValueAndUpdateMappings(synonymPath.addElement(party + "_additional_language"), value -> postingObligationsElectionBuilder.setAdditionalLanguage(removeHtml(value)));

        // if we find partyX_type and its value is 'specify', then set to true, else false
        setValueAndUpdateMappings(synonymPath.addElement(party + "_type"), (value) -> postingObligationsElectionBuilder.setAsPermitted(!"specify".equals(value)));

        // if we find partyX_collateral_management_agreement and its value is 'yey' then set ExcludedCollateral to partyX_collateral_management_agreement_specify if it exists.
        setExcludedCollateralValueIfYey(synonymPath, party, postingObligationsElectionBuilder, "_collateral_management_agreement", "_collateral_management_agreement_specify");

        // if we find partyX_control_agreement and its value is 'yey' then set ExcludedCollateral to partyX_control_agreement_specify if it exists.
        setExcludedCollateralValueIfYey(synonymPath, party, postingObligationsElectionBuilder, "_control_agreement", "_control_agreement_specify");

        // parses the text field in answers.partyA.chargor_posting_obligations.partyB_fx_haircut_percentage and looks for first occurrence of [d]% where d is one or more digits
        setFxHaircutPercentage(synonymPath, party, postingObligationsElectionBuilder);

        return postingObligationsElectionBuilder.hasData() ? Optional.of(postingObligationsElectionBuilder.build()) : Optional.empty();
    }

    private void setFxHaircutPercentage(Path synonymPath, String party, PostingObligationsElection.PostingObligationsElectionBuilder postingObligationsElectionBuilder) {
        Path fxHaircutPercentagePath = synonymPath.addElement(party + "_fx_haircut_percentage");

        setValueAndOptionallyUpdateMappings(fxHaircutPercentagePath,
                xmlValue -> {
                    Optional<BigDecimal> maybeFxHaircutValue = parseFxHaircutPercentageValue(xmlValue);

                    maybeFxHaircutValue.ifPresent(fxHaircutPercentage -> {
                        CollateralTreatment.CollateralTreatmentBuilder collateralTreatmentBuilder = postingObligationsElectionBuilder
                                .getOrCreateEligibleCollateral(0)
                                .getOrCreateTreatment();

                        collateralTreatmentBuilder.setIsIncluded(true);

                        collateralTreatmentBuilder
                                .getOrCreateValuationTreatment()
                                .setFxHaircutPercentage(fxHaircutPercentage);
                    });

                    return maybeFxHaircutValue.isPresent();
                },
                getMappings(),
                getModelPath()
        );
    }

    private Optional<BigDecimal> parseFxHaircutPercentageValue(Object xmlValue) {
        if (!(xmlValue instanceof String)) {
            return Optional.empty();
        }

        String value = (String) xmlValue;
        int intValue;
        try {
            intValue = Integer.parseInt(value.replaceAll(".*\\[(\\d+)\\]%.*", "$1"));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        if (intValue > 100 || intValue < 0) {
            return Optional.empty();
        }
        BigDecimal bdValue = BigDecimal.valueOf(intValue);
        return Optional.of(bdValue.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN));
    }

    private void setExcludedCollateralValueIfYey(Path synonymPath, String party, PostingObligationsElection.PostingObligationsElectionBuilder postingObligationsElectionBuilder, String collateral_management_agreement, String collateral_management_agreement_specify) {
        setValueAndUpdateMappings(synonymPath.addElement(party + collateral_management_agreement), (agreement) -> {
            if (agreement.equals("yey")) {
                setValueAndUpdateMappings(synonymPath.addElement(party + collateral_management_agreement_specify),
                        postingObligationsElectionBuilder::setExcludedCollateral);
            }
        });
    }


}
