package cdm.product.template.processor;

import cdm.base.staticdata.asset.common.processor.FxMetaHelper;
import cdm.base.staticdata.party.PayerReceiver;
import cdm.legaldocumentation.contract.processor.PartyMappingHelper;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

@SuppressWarnings("unused")
public class FxPayerReceiverMappingProcessor extends MappingProcessor {

    private final FxMetaHelper fxHelper;
    
    public FxPayerReceiverMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
        this.fxHelper = new FxMetaHelper(context.getMappings());
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        fxHelper.getExchangedCurrencyPath(synonymPath).ifPresent(exchangedCurrencyPath -> {
            PayerReceiver.PayerReceiverBuilder payerReceiverBuilder = (PayerReceiver.PayerReceiverBuilder) builder;
            PartyMappingHelper.getInstance(getContext())
                    .ifPresent(helper -> {
                                helper.setCounterpartyRoleEnum(
                                        getModelPath(),
                                        exchangedCurrencyPath.addElement("payerPartyReference"),
                                        payerReceiverBuilder::setPayer);
                                helper.setCounterpartyRoleEnum(
                                        getModelPath(),
                                        exchangedCurrencyPath.addElement("receiverPartyReference"),
                                        payerReceiverBuilder::setReceiver);
                            }
                    );
        });
    }


}
