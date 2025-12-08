package cdm.product.template.processor;

import cdm.legaldocumentation.contract.processor.PartyMappingHelper;
import cdm.product.template.FixedPricePayout;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static cdm.base.staticdata.party.PayerReceiver.PayerReceiverBuilder;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class DividendFixedLegMappingProcessor extends MappingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DividendFixedLegMappingProcessor.class);

    public DividendFixedLegMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        FixedPricePayout.FixedPricePayoutBuilder fixedPricePayoutBuilder = (FixedPricePayout.FixedPricePayoutBuilder) builder;
        if (fixedPricePayoutBuilder.getPayerReceiver() != null) {
            return;
        }
        Path dividendOrFixedLegSynonymPath = synonymPath.getParent();
        Path fixedLegSynonymPath = dividendOrFixedLegSynonymPath.getParent().addElement("fixedLeg");
        PartyMappingHelper.getInstance(getContext())
                .ifPresent(helper -> {
                    PayerReceiverBuilder payerReceiver = fixedPricePayoutBuilder.getOrCreatePayerReceiver();
                    RosettaPath payerReceiverModelPath = getModelPath().newSubPath("payerReceiver");
                    LOGGER.debug("Mapping FixedPricePayout.payerReceiver [synonymPath={}, modelPath={}]", fixedLegSynonymPath, payerReceiverModelPath);

                    helper.setCounterpartyRoleEnum(
                            payerReceiverModelPath.newSubPath("payer"),
                            fixedLegSynonymPath.addElement("payerPartyReference"),
                            payerReceiver::setPayer);

                    helper.setCounterpartyRoleEnum(
                            payerReceiverModelPath.newSubPath("receiver"),
                            fixedLegSynonymPath.addElement("receiverPartyReference"),
                            payerReceiver::setReceiver);
                });
    }
}
