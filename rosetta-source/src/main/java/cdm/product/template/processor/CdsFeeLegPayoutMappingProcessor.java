package cdm.product.template.processor;

import cdm.product.template.EconomicTerms;
import cdm.product.template.Payout;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static cdm.product.template.NonTransferableProduct.NonTransferableProductBuilder;

/**
 * Removes unwanted mapping of InterestRatePayout for CD index samples.
 */
@SuppressWarnings("unused") // used in generated code
public class CdsFeeLegPayoutMappingProcessor extends MappingProcessor {

    public CdsFeeLegPayoutMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path creditDefaultSwapSynonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        Path feeLegSynonymPath = creditDefaultSwapSynonymPath.addElement("feeLeg").addElement("periodicPayment");
        if (getMappings().stream().noneMatch(m -> feeLegSynonymPath.nameStartMatches(m.getXmlPath()))) {
            NonTransferableProductBuilder productBuilder = (NonTransferableProductBuilder) builder;
            removeInterestRatePayout(getPayoutBuilders(productBuilder));
        }
    }

    private List<? extends Payout.PayoutBuilder> getPayoutBuilders(NonTransferableProductBuilder productBuilder) {
        return Optional.ofNullable(productBuilder)
                .map(NonTransferableProductBuilder::getEconomicTerms)
                .map(EconomicTerms.EconomicTermsBuilder::getPayout)
                .orElse(Collections.emptyList());
    }

    private void removeInterestRatePayout(List<? extends Payout.PayoutBuilder> payoutBuilders) {
        payoutBuilders.forEach(payoutBuilder -> {
            if (payoutBuilder.getInterestRatePayout() != null) {
                payoutBuilder.setInterestRatePayout(null);
            }
        });
    }
}