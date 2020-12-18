package cdm.legalagreement.csa.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import cdm.legalagreement.csa.ElectiveAmountElection;
import cdm.legalagreement.csa.ElectiveAmountEnum;
import cdm.observable.asset.Money;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndUpdateMappings;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.setIsoCurrency;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.synonymToEnumValueMap;
import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.ISDA_CREATE_SYNONYM_SOURCE;
import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.toCounterpartyRoleEnum;

class ElectiveAmountElectionMappingHelper {

	private static final String ZERO = "zero";

	private final RosettaPath path;
	private final List<Mapping> mappings;
	private final Map<String, ISOCurrencyCodeEnum> synonymToIsoCurrencyCodeEnumMap;

	ElectiveAmountElectionMappingHelper(RosettaPath path, List<Mapping> mappings) {
		this.path = path;
		this.mappings = mappings;
		this.synonymToIsoCurrencyCodeEnumMap = synonymToEnumValueMap(ISOCurrencyCodeEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
	}

	Optional<ElectiveAmountElection> getElectiveAmountElection(Path synonymPath, String party) {
		ElectiveAmountElection.ElectiveAmountElectionBuilder electiveAmountElectionBuilder = ElectiveAmountElection.builder();
		Money.MoneyBuilder moneyBuilder = Money.builder();

		setValueAndUpdateMappings(synonymPath.addElement(party + "_amount"),
				(value) -> moneyBuilder.setAmount(new BigDecimal(value)), mappings, path);

		setValueAndOptionallyUpdateMappings(synonymPath.addElement(party + "_currency"),
				(value) -> setIsoCurrency(synonymToIsoCurrencyCodeEnumMap, moneyBuilder::setCurrency, value), mappings, path);

		if (moneyBuilder.hasData()) {
			electiveAmountElectionBuilder.setAmountBuilder(moneyBuilder);
		}

		setValueAndUpdateMappings(synonymPath.addElement(party + "_" + synonymPath.getLastElement().getPathName()),
				(value) -> {
					electiveAmountElectionBuilder.setParty(toCounterpartyRoleEnum(party));
					if (ZERO.equals(value)) {
						electiveAmountElectionBuilder.setElectiveAmount(ElectiveAmountEnum.ZERO);
					}
				}, mappings, path);

		setValueAndUpdateMappings(synonymPath.addElement(party + "_specify"),
				electiveAmountElectionBuilder::setCustomElection, mappings, path);

		return electiveAmountElectionBuilder.hasData() ? Optional.of(electiveAmountElectionBuilder.build()) : Optional.empty();
	}
}
