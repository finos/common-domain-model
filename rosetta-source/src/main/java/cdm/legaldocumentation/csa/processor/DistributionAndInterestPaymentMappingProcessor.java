package cdm.legaldocumentation.csa.processor;

import cdm.product.collateral.CollateralInterestHandlingEnum;
import cdm.product.collateral.DeliveryAmountElectionEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static cdm.product.collateral.CollateralInterestCalculationParameters.CollateralInterestCalculationParametersBuilder;
import static cdm.product.collateral.CollateralInterestHandlingParameters.CollateralInterestHandlingParametersBuilder;
import static cdm.product.collateral.CollateralInterestParameters.CollateralInterestParametersBuilder;
import static cdm.product.collateral.DeliveryAmount.DeliveryAmountBuilder;
import static cdm.product.collateral.DistributionAndInterestPayment.DistributionAndInterestPaymentBuilder;
import static cdm.product.collateral.InterestAmountApplication.InterestAmountApplicationBuilder;
import static cdm.product.collateral.ReturnAmount.ReturnAmountBuilder;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * CreateiQ mapper
 */
@SuppressWarnings("unused") // used in generated code
public class DistributionAndInterestPaymentMappingProcessor extends MappingProcessor {

    public DistributionAndInterestPaymentMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        AtomicInteger index = new AtomicInteger(0);
        Path baseSynonymPath = synonymPath.getParent().getParent();
        emptyIfNull(((DistributionAndInterestPaymentBuilder) builder).getInterestParameters())
                .forEach(b -> mapInterestParameters(baseSynonymPath, b, index.getAndIncrement()));
    }

    private void mapInterestParameters(Path baseSynonymPath, CollateralInterestParametersBuilder interestParametersBuilder, int index) {
        mapInterestHandlingParameters(baseSynonymPath, interestParametersBuilder.getOrCreateInterestHandlingParameters());
        mapCreateInterestCalculationParameters(baseSynonymPath, interestParametersBuilder.getOrCreateInterestCalculationParameters());
    }

    private void mapInterestHandlingParameters(Path baseSynonymPath, CollateralInterestHandlingParametersBuilder interestHandlingParameters) {
        Path interestTransferPath = baseSynonymPath.addElement("interest_transfer").addElement("interest_transfer");
        boolean interestTransfer = getValueAndUpdateMappings(interestTransferPath)
                .map("applicable"::equals)
                .orElse(false);
        Path interestAdjustmentPath = baseSynonymPath.addElement("interest_adjustment").addElement("is_applicable");
        boolean interestAdjustment = getValueAndUpdateMappings(interestAdjustmentPath)
                .map("applicable"::equals)
                .orElse(false);
        Optional.ofNullable(interestTransfer && interestAdjustment ?
                CollateralInterestHandlingEnum.TRANSFER_OR_ADJUST :
                interestTransfer ?
                        CollateralInterestHandlingEnum.TRANSFER :
                        interestAdjustment ?
                                CollateralInterestHandlingEnum.ADJUST :
                                null)
                .ifPresent(interestHandlingParameters::setInterestPaymentHandling);

        Path interestPaymentNettingPath = baseSynonymPath.addElement("interest_payment_netting").addElement("interest_payment_netting");
        setValueAndUpdateMappings(interestPaymentNettingPath,
                value ->
                        interestHandlingParameters
                                .setNetPostedAndHeldInterest("applicable".equals(value)));

        mapInterestAmountApplication(baseSynonymPath, interestHandlingParameters.getOrCreateInterestAmountApplication());

        Path alternativePath = baseSynonymPath.addElement("alternative_to_interest_amounts_and_interest_payment");
        boolean specifyAlternative = getValueAndUpdateMappings(alternativePath.addElement("specify_alternative"))
                .map("true"::equals)
                .orElse(false);
        if (specifyAlternative) {
            setValueAndUpdateMappings(alternativePath.addElement("specify"),
                    value ->
                            interestHandlingParameters.setAlternativeProvision(value));
        }
    }

    private void mapInterestAmountApplication(Path baseSynonymPath, InterestAmountApplicationBuilder interestAmountApplication) {
        mapDeliveryAmount(baseSynonymPath, interestAmountApplication.getOrCreateDeliveryAmount());
        mapReturnAmount(baseSynonymPath, interestAmountApplication.getOrCreateReturnAmount());
    }

    private void mapDeliveryAmount(Path baseSynonymPath, DeliveryAmountBuilder deliveryAmount) {
        Path returnAndDeliveryAmountPath = baseSynonymPath.addElement("return_amount_delivery_amount");
        
        setValueAndUpdateMappings(returnAndDeliveryAmountPath.addElement("delivery_amount"),
                value -> getSynonymToEnumMap().getEnumValueOptional(DeliveryAmountElectionEnum.class, value)
                        .ifPresent(deliveryAmount::setStandardElection));
        
        setValueAndUpdateMappings(returnAndDeliveryAmountPath.addElement("specify_delivery_amount"),
                deliveryAmount::setCustomElection);
    }

    private void mapReturnAmount(Path baseSynonymPath, ReturnAmountBuilder returnAmount) {
        Path returnAndDeliveryAmountPath = baseSynonymPath.addElement("return_amount_delivery_amount");

        setValueAndUpdateMappings(returnAndDeliveryAmountPath.addElement("return_amount"),
                value -> returnAmount.setIncludesDefaultLanguage("include".equals(value)));

        setValueAndUpdateMappings(returnAndDeliveryAmountPath.addElement("specify_return_amount"),
                returnAmount::setCustomElection);
    }

    private void mapCreateInterestCalculationParameters(Path baseSynonymPath, CollateralInterestCalculationParametersBuilder interestCalculationParameters) {
        setValueAndUpdateMappings(baseSynonymPath.addElement("negative_interest").addElement("negative_interest"),
                value ->
                        interestCalculationParameters.getOrCreateFloatingRate()
                                .setNegativeInterest("applicable".equals(value)));
    }
}
