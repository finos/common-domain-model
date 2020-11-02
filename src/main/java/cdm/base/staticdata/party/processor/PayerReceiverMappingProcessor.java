package cdm.base.staticdata.party.processor;

import cdm.base.staticdata.party.AncillaryRoleEnum;
import com.regnosys.rosetta.common.translation.*;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cdm.base.staticdata.party.PayerReceiver.PayerReceiverBuilder;
import static cdm.legalagreement.contract.processor.PartyMappingHelper.PRODUCT_SUB_PATH;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;

/**
 * FpML mapping processor.
 */
public abstract class PayerReceiverMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverMappingProcessor.class);

	PayerReceiverMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		if (!getModelPath().containsPath(PRODUCT_SUB_PATH)) {
			LOGGER.warn("PayerReceiver used outside of the Product definition {}", getModelPath().buildPath());
			setPartyReference(synonymPath, (PayerReceiverBuilder) parent);
		} else if (getModelPath().toIndexless().containsPath(RosettaPath.valueOf("economicTerms.payout.cashflow.payerReceiver"))) {
			setCashflowCounterpartyOrRelatedParty(synonymPath, (PayerReceiverBuilder) parent, getCashflowRelatedPartyEnum(synonymPath));
		} else {
			setCounterparty(synonymPath, (PayerReceiverBuilder) parent);
		}
	}

	abstract void setCounterparty(Path synonymPath, PayerReceiverBuilder builder);

	abstract void setCashflowCounterpartyOrRelatedParty(Path synonymPath, PayerReceiverBuilder builder, AncillaryRoleEnum cashflowRelatedPartyEnum);

	abstract void setPartyReference(Path synonymPath, PayerReceiverBuilder builder);

	@NotNull
	private AncillaryRoleEnum getCashflowRelatedPartyEnum(Path synonymPath) {
		String partyExternalReference = getNonNullMappedValue(filterMappings(getMappings(), synonymPath.addElement("href"))).orElse("");
		if (getMappedValue("brokerPartyReference", "href").map(partyExternalReference::equals).orElse(false)) {
			return AncillaryRoleEnum.ARRANGING_BROKER;
		}
		// TODO add other checks to determine related party enum based on FpML roles
		return AncillaryRoleEnum.OTHER_PARTY;
	}

	private Optional<String> getMappedValue(String... endsWith) {
		return getNonNullMappedValue(getMappings().stream()
				.filter(p -> p.getXmlPath().endsWith(endsWith))
				.collect(Collectors.toList()));
	}
}
