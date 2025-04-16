package cdm.legaldocumentation.csa.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import cdm.legaldocumentation.csa.ElectiveAmountElection;
import cdm.legaldocumentation.csa.ElectiveAmountEnum;
import cdm.observable.asset.Money;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.translation.SynonymToEnumMap;
import com.rosetta.model.lib.path.RosettaPath;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndUpdateMappings;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.*;

public class ElectiveAmountElectionMappingHelper {

	private static final String ZERO = "zero";

	private final RosettaPath path;
	private final List<Mapping> mappings;
	private final SynonymToEnumMap synonymToEnumMap;

	public ElectiveAmountElectionMappingHelper(RosettaPath path, List<Mapping> mappings, SynonymToEnumMap synonymToEnumMap) {
		this.path = path;
		this.mappings = mappings;
		this.synonymToEnumMap = synonymToEnumMap;
	}

	public Optional<ElectiveAmountElection> getElectiveAmountElection(Path synonymPath, String party) {
		ElectiveAmountElection.ElectiveAmountElectionBuilder electiveAmountElectionBuilder = ElectiveAmountElection.builder();
		Money.MoneyBuilder moneyBuilder = Money.builder();

		setValueAndUpdateMappings(synonymPath.addElement(party + "_amount"),
				(value) -> moneyBuilder.setValue(new BigDecimal(value)), mappings, path);

		setValueAndOptionallyUpdateMappings(synonymPath.addElement(party + "_currency"),
				(value) -> setIsoCurrency(synonymToEnumMap.getEnumValue(ISOCurrencyCodeEnum.class, value),
						cur -> moneyBuilder.getOrCreateUnit().setCurrency(cur)),
				mappings,
				path);

		if (moneyBuilder.hasData()) {
			electiveAmountElectionBuilder.setAmount(moneyBuilder);
		}

		setValueAndUpdateMappings(synonymPath.addElement(party + "_" + synonymPath.getLastElement().getPathName()),
				(value) -> {
					electiveAmountElectionBuilder.setParty(toCounterpartyRoleEnum(party));
					if (ZERO.equals(value)) {
						electiveAmountElectionBuilder.setElectiveAmount(ElectiveAmountEnum.ZERO);
					}
				}, mappings, path);

		setValueAndUpdateMappings(synonymPath.addElement(party + "_specify"),
				value -> electiveAmountElectionBuilder.setCustomElection(removeHtml(value)), mappings, path);

		return electiveAmountElectionBuilder.hasData() ? Optional.of(electiveAmountElectionBuilder.build()) : Optional.empty();
	}
}
