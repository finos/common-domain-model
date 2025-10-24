package cdm.legaldocumentation.csa.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import cdm.legaldocumentation.csa.MinimumTransferAmountElection;
import cdm.legaldocumentation.csa.ThresholdMinimumTransferAmountFixedAmount;
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

public class MinimumTransferAmountElectionMappingHelper {

    private final RosettaPath path;
    private final List<Mapping> mappings;
    private final SynonymToEnumMap synonymToEnumMap;

    public MinimumTransferAmountElectionMappingHelper(RosettaPath path, List<Mapping> mappings, SynonymToEnumMap synonymToEnumMap) {
        this.path = path;
        this.mappings = mappings;
        this.synonymToEnumMap = synonymToEnumMap;
    }

    public Optional<MinimumTransferAmountElection> getMinimumTransferAmountElection(Path synonymPath, String party) {
        MinimumTransferAmountElection.MinimumTransferAmountElectionBuilder minimumTransferAmountElectionBuilder = MinimumTransferAmountElection.builder();
        Money.MoneyBuilder moneyBuilder = Money.builder();

        setValueAndUpdateMappings(synonymPath.addElement(party + "_amount"),
                (value) -> moneyBuilder.setValue(new BigDecimal(value)), mappings, path);

        setValueAndOptionallyUpdateMappings(synonymPath.addElement(party + "_currency"),
                (value) -> setIsoCurrency(synonymToEnumMap.getEnumValue(ISOCurrencyCodeEnum.class, value),
                        cur -> moneyBuilder.getOrCreateUnit().setCurrency(cur)),
                mappings,
                path);

        if (moneyBuilder.hasData()) {
            minimumTransferAmountElectionBuilder.setFixedAmount(ThresholdMinimumTransferAmountFixedAmount.builder().setAmount(moneyBuilder));
        }

        setValueAndUpdateMappings(synonymPath.addElement(party + "_" + synonymPath.getLastElement().getPathName()),
                (value) -> minimumTransferAmountElectionBuilder
                        .setParty(toCounterpartyRoleEnum(party)).build(), mappings, path);

        setValueAndUpdateMappings(synonymPath.addElement(party + "_specify"),
                value -> minimumTransferAmountElectionBuilder.setOther(removeHtml(value)), mappings, path);

        return minimumTransferAmountElectionBuilder.hasData() ? Optional.of(minimumTransferAmountElectionBuilder.build()) : Optional.empty();
    }
}
