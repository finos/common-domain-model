package cdm.product.template.processor;

import cdm.base.staticdata.party.PayerReceiver;
import cdm.legaldocumentation.contract.processor.PartyMappingHelper;
import cdm.observable.asset.QuoteBasisEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappedValue;

@SuppressWarnings("unused")
public class FxPayerReceiverMappingProcessor extends MappingProcessor {

    public FxPayerReceiverMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        getExchangedCurrencyPath(synonymPath).ifPresent(exchangedCurrencyPath -> {
            PayerReceiver.PayerReceiverBuilder payerReceiverBuilder = (PayerReceiver.PayerReceiverBuilder) builder;
            PartyMappingHelper.getInstance(getContext())
                    .ifPresent(helper -> {
                                helper.setCounterpartyRoleEnum(getModelPath(),
                                        exchangedCurrencyPath.addElement("payerPartyReference"), payerReceiverBuilder::setPayer);
                                helper.setCounterpartyRoleEnum(getModelPath(),
                                        exchangedCurrencyPath.addElement("receiverPartyReference"), payerReceiverBuilder::setReceiver);
                            }
                    );
        });
    }

    private Optional<Path> getExchangedCurrencyPath(Path synonymPath) {
        return getNonNullMappedValue(synonymPath, getMappings())
                .map(quoteBasis -> {
                    Path productPath = synonymPath.getParent().getParent().getParent();
                    QuoteBasisEnum quoteBasisEnum = QuoteBasisEnum.fromDisplayName(quoteBasis);
                    switch (quoteBasisEnum) {
                        case CURRENCY_1_PER_CURRENCY_2:
                            return productPath.addElement("exchangedCurrency2");
                        case CURRENCY_2_PER_CURRENCY_1:
                            return productPath.addElement("exchangedCurrency1");
                        default:
                            return null;
                    }
                });
    }
}
