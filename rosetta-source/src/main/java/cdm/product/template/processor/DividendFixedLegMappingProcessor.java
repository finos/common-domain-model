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

import static cdm.base.staticdata.party.PayerReceiver.*;
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
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
        AtomicInteger index = new AtomicInteger(0);
        emptyIfNull((List<? extends FixedPricePayout.FixedPricePayoutBuilder>) builders).stream()
                .filter(RosettaModelObjectBuilder::hasData)
                .forEach(fixedPricePayout ->
                        mapFixedPricePayoutPayerReceiver(synonymPath.getParent(),
                                getModelPath().withIndex(index.getAndIncrement()).newSubPath("payerReceiver"),
                                fixedPricePayout));
    }

    private void mapFixedPricePayoutPayerReceiver(Path dividendLegSynonymPath,
                                                  RosettaPath payerReceiverModelPath,
                                                  FixedPricePayout.FixedPricePayoutBuilder fixedPricePayout) {
        LOGGER.debug("Mapping FixedPricePayout.payerReceiver [synonymPath={}, modelPath={}]", dividendLegSynonymPath, payerReceiverModelPath);
        PartyMappingHelper.getInstance(getContext())
                .ifPresent(helper -> {
                    PayerReceiverBuilder payerReceiver = fixedPricePayout.getOrCreatePayerReceiver();

                    helper.setCounterpartyRoleEnum(
                            payerReceiverModelPath.newSubPath("payer"),
                            dividendLegSynonymPath.addElement("payerPartyReference"),
                            payerReceiver::setPayer);

                    helper.setCounterpartyRoleEnum(
                            payerReceiverModelPath.newSubPath("receiver"),
                            dividendLegSynonymPath.addElement("receiverPartyReference"),
                            payerReceiver::setReceiver);
                });
    }
}
