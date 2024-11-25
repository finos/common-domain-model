package cdm.event.common.processor;

import cdm.base.math.QuantityChangeDirectionEnum;
import cdm.base.math.QuantitySchedule;
import cdm.base.staticdata.asset.common.Asset;
import cdm.base.staticdata.asset.common.AssetIdTypeEnum;
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.*;
import cdm.legaldocumentation.contract.processor.PartyMappingHelper;
import cdm.observable.asset.FeeTypeEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.translation.SynonymToEnumMap;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.records.Date;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cdm.event.common.SplitInstruction.SplitInstructionBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;

public class NovationInstructionMappingProcessor extends MappingProcessor {

    private final SynonymToEnumMap synonymToEnumMap;

    public NovationInstructionMappingProcessor(RosettaPath rosettaPath, List<Path> synonymPaths, MappingContext context) {
        super(rosettaPath, synonymPaths, context);
        this.synonymToEnumMap = context.getSynonymToEnumMap();
    }

    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        // Breakdown 1
        PrimitiveInstruction.PrimitiveInstructionBuilder breakdown1Builder = PrimitiveInstruction.builder();
        getIncomingPartyChangeInstruction(synonymPath).ifPresent(breakdown1Builder::setPartyChange);
        getIncomingQuantityChangeInstruction(synonymPath).ifPresent(breakdown1Builder::setQuantityChange);
        getTransferInstruction(synonymPath).ifPresent(breakdown1Builder::setTransfer);

        // Breakdown 2
        PrimitiveInstruction.PrimitiveInstructionBuilder breakdown2Builder = PrimitiveInstruction.builder();
        getOutgoingQuantityChangeInstruction(synonymPath).ifPresent(breakdown2Builder::setQuantityChange);

        List<PrimitiveInstruction> breakdowns = new ArrayList<>();
        breakdowns.add(breakdown1Builder.build());
        breakdowns.add(breakdown2Builder.build());

        SplitInstructionBuilder splitInstructionBuilder = (SplitInstructionBuilder) parent;
        splitInstructionBuilder.setBreakdown(breakdowns);
    }

    private Optional<PartyChangeInstruction> getIncomingPartyChangeInstruction(Path synonymPath) {
        PartyChangeInstruction.PartyChangeInstructionBuilder partyChangeInstructionBuilder = PartyChangeInstruction.builder();
        Counterparty.CounterpartyBuilder counterpartyBuilder = partyChangeInstructionBuilder.getOrCreateCounterparty();

        PartyMappingHelper helper = PartyMappingHelper.getInstanceOrThrow(getContext());

        Path outgoingPartyPath = synonymPath.addElement("transferor");
        helper.setCounterpartyRoleEnum(getModelPath(),
                outgoingPartyPath,
                counterpartyBuilder::setRole);

        Path incomingPartyPath = synonymPath.addElement("transferee").addElement("href");
        setValueAndUpdateMappings(incomingPartyPath,
                xmlValue ->
                        counterpartyBuilder.setPartyReference(ReferenceWithMetaParty.builder()
                                .setExternalReference(xmlValue)));

        Identifier.IdentifierBuilder tradeIdBuilder = partyChangeInstructionBuilder.getOrCreateTradeId(0);
        AssignedIdentifier.AssignedIdentifierBuilder assignedIdentifierBuilder = tradeIdBuilder.getOrCreateAssignedIdentifier(0);
        Path newTradeIdentifierPath = synonymPath.addElement("newTradeIdentifier").addElement("versionedTradeId");

        Path tradeIdPath = newTradeIdentifierPath.addElement("tradeId");
        setValueAndUpdateMappings(tradeIdPath,
                assignedIdentifierBuilder.getOrCreateIdentifier()::setValue);
        setValueAndUpdateMappings(tradeIdPath.addElement("tradeIdScheme"),
                assignedIdentifierBuilder.getOrCreateIdentifier().getOrCreateMeta()::setScheme);
        setValueAndUpdateMappings(newTradeIdentifierPath.addElement("version"),
                xmlValue -> assignedIdentifierBuilder.setVersion(Integer.parseInt(xmlValue)));

        return partyChangeInstructionBuilder.prune().hasData() ?
                Optional.of(partyChangeInstructionBuilder.build()) : Optional.empty();
    }

    private Optional<QuantityChangeInstruction> getIncomingQuantityChangeInstruction(Path synonymPath) {
        QuantityChangeInstruction.QuantityChangeInstructionBuilder quantityChangeInstructionBuilder =
                QuantityChangeInstruction.builder();

        QuantitySchedule.QuantityScheduleBuilder novatedQuantityBuilder = quantityChangeInstructionBuilder
                .getOrCreateChange(0)
                .getOrCreateQuantity(0)
                .getOrCreateValue();

        // novationAmount.changeInNotionalAmount and novatedAmount are both valid ways to specify the amount that is novated
        setQuantity(novatedQuantityBuilder, synonymPath.addElement("novationAmount").addElement("changeInNotionalAmount"));
        setQuantity(novatedQuantityBuilder, synonymPath.addElement("novatedAmount"));

        return quantityChangeInstructionBuilder.prune().hasData() ?
                Optional.of(quantityChangeInstructionBuilder.setDirection(QuantityChangeDirectionEnum.REPLACE).build()) :
                Optional.empty();
    }


    private Optional<TransferInstruction> getTransferInstruction(Path synonymPath) {
        TransferInstruction.TransferInstructionBuilder transferInstructionBuilder = TransferInstruction.builder();
        TransferState.TransferStateBuilder tradeStateBuilder = transferInstructionBuilder.getOrCreateGrossTransfer(0);
        Transfer.TransferBuilder transferBuilder = tradeStateBuilder.getOrCreateTransfer();

        Path paymentPath = synonymPath.addElement("payment");

        // id
        setValueAndUpdateMappings(paymentPath.addElement("id"), tradeStateBuilder.getOrCreateMeta()::setExternalKey);

        // payer / receiver
        setValueAndUpdateMappings(paymentPath.addElement("payerPartyReference").addElement("href"),
                transferBuilder.getOrCreatePayerReceiver().getOrCreatePayerPartyReference()::setExternalReference);
        setValueAndUpdateMappings(paymentPath.addElement("receiverPartyReference").addElement("href"),
                transferBuilder.getOrCreatePayerReceiver().getOrCreateReceiverPartyReference()::setExternalReference);

        // payment amount
        setQuantity(transferBuilder.getDeliverableQuantity(), paymentPath.addElement("paymentAmount"));

        // payment amount
        setAsset(transferBuilder.getOrCreateAsset(), paymentPath.addElement("paymentAmount"));

        // settlement date
        setValueAndUpdateMappings(paymentPath.addElement("paymentDate").addElement("adjustedDate"),
                xmlValue ->
                        transferBuilder
                                .getOrCreateSettlementDate()
                                .getOrCreateAdjustedDate()
                                .setValue(Date.parse(xmlValue.replace("Z", ""))));

        // payment type
        setValueAndOptionallyUpdateMappings(paymentPath.addElement("paymentType"),
                xmlValue ->
                        synonymToEnumMap.getEnumValueOptional(FeeTypeEnum.class, xmlValue)
                                .map(transferBuilder.getOrCreateTransferExpression()::setPriceTransfer)
                                .isPresent(),
                getMappings(),
                getModelPath());

        // default fee type to Novation if not specified
        if (transferBuilder.getTransferExpression() == null) {
            transferBuilder.getOrCreateTransferExpression().setPriceTransfer(FeeTypeEnum.NOVATION);
        }

        return transferInstructionBuilder.prune().hasData() ?
                Optional.of(transferInstructionBuilder.build()) :
                Optional.empty();
    }

    private Optional<QuantityChangeInstruction> getOutgoingQuantityChangeInstruction(Path synonymPath) {
        QuantityChangeInstruction.QuantityChangeInstructionBuilder quantityChangeInstructionBuilder =
                QuantityChangeInstruction.builder();

        QuantitySchedule.QuantityScheduleBuilder remainingQuantityBuilder = quantityChangeInstructionBuilder
                .getOrCreateChange(0)
                .getOrCreateQuantity(0)
                .getOrCreateValue();

        // novationAmount.outstandingNotionalAmount and remainingAmount are both valid ways to specify the amount that is NOT novated
        setQuantity(remainingQuantityBuilder, synonymPath.addElement("novationAmount").addElement("outstandingNotionalAmount"));
        setQuantity(remainingQuantityBuilder, synonymPath.addElement("remainingAmount"));

        // remainingAmount is optional, so if not found, just decrease by the novated amount
        QuantityChangeDirectionEnum directionEnum;
        if (remainingQuantityBuilder.getValue() == null) {
            setQuantity(remainingQuantityBuilder, synonymPath.addElement("novatedAmount"));
            directionEnum = QuantityChangeDirectionEnum.DECREASE;
        } else {
            directionEnum = QuantityChangeDirectionEnum.REPLACE;
        }

        return quantityChangeInstructionBuilder.prune().hasData() ?
                Optional.of(quantityChangeInstructionBuilder.setDirection(directionEnum).build()) :
                Optional.empty();
    }

    private void setQuantity(QuantitySchedule.QuantityScheduleBuilder quantityBuilder, Path basePath) {
        setValueAndUpdateMappings(basePath.addElement("amount"),
                xmlValue -> quantityBuilder.setValue(new BigDecimal(xmlValue)));
        setValueAndUpdateMappings(basePath.addElement("currency"),
                quantityBuilder.getOrCreateUnit().getOrCreateCurrency()::setValue);
    }

    private void setAsset(Asset.AssetBuilder assetBuilder, Path paymentAmountPath) {
        setValueAndUpdateMappings(paymentAmountPath.addElement("currency"),
                xmlValue -> assetBuilder.getOrCreateCash().getOrCreateIdentifier(0)
                        .setIdentifierValue(xmlValue)
                        .setIdentifierType(AssetIdTypeEnum.CURRENCY_CODE));
    }
}
