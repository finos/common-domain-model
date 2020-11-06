package cdm.legalagreement.csa.processor;

import cdm.legalagreement.csa.PostingObligations;
import cdm.legalagreement.csa.PostingObligationsElection;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.PARTIES;
import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.toCounterpartyEnum;

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

    @NotNull
    private Optional<PostingObligationsElection> getPostingObligationsElectionBuilder(Path synonymPath, String party) {
        PostingObligationsElection.PostingObligationsElectionBuilder postingObligationsElectionBuilder = PostingObligationsElection.builder();
        // if we find partyX_type, then set partyX as the party
        setValueAndUpdateMappings(synonymPath.addElement(party + "_type"), (value) -> postingObligationsElectionBuilder.setParty(toCounterpartyEnum(party)));

        // if we find additional_language, then set additional_language as the additional party
        setValueAndUpdateMappings(synonymPath.addElement("additional_language"), postingObligationsElectionBuilder::setAdditionalLanguage);

        // if we find partyX_additional_language, then set partyX_additional_language as the additional party - this overrides the non party specific additional party setting.
        setValueAndUpdateMappings(synonymPath.addElement(party + "_additional_language"), postingObligationsElectionBuilder::setAdditionalLanguage);

        // if we find partyX_type and its value is 'specify', then set to true, else false
        setValueAndUpdateMappings(synonymPath.addElement(party + "_type"), (value) -> postingObligationsElectionBuilder.setAsPermitted(!"specify".equals(value)));

        // if we find partyX_collateral_management_agreement and its value is 'yey' then set ExcludedCollateral to partyX_collateral_management_agreement_specify if it exists.
        setExcludedCollateralValueIfYey(synonymPath, party, postingObligationsElectionBuilder, "_collateral_management_agreement", "_collateral_management_agreement_specify");

        // if we find partyX_control_agreement and its value is 'yey' then set ExcludedCollateral to partyX_control_agreement_specify if it exists.
        setExcludedCollateralValueIfYey(synonymPath, party, postingObligationsElectionBuilder, "_control_agreement", "_control_agreement_specify");

        return postingObligationsElectionBuilder.hasData() ? Optional.of(postingObligationsElectionBuilder.build()) : Optional.empty();
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
