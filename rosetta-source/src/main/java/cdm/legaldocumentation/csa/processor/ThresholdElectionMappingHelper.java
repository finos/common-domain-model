package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.ThresholdElection;
import cdm.legaldocumentation.csa.ThresholdMinimumTransferAmountFixedAmount;
import cdm.observable.asset.Money;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.translation.SynonymToEnumMap;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndUpdateMappings;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.*;

public class ThresholdElectionMappingHelper {

    private final RosettaPath path;
    private final List<Mapping> mappings;
    private final CreateiQMappingHelperUtils createiQMappingHelperUtils;

    public ThresholdElectionMappingHelper(RosettaPath path, List<Mapping> mappings, SynonymToEnumMap synonymToEnumMap) {
        this.path = path;
        this.mappings = mappings;
        this.createiQMappingHelperUtils = new CreateiQMappingHelperUtils(path, mappings, synonymToEnumMap);
    }

    public Optional<ThresholdElection> getThresholdElection(Path synonymPath, String party) {
        ThresholdElection.ThresholdElectionBuilder thresholdElectionBuilder = ThresholdElection.builder();
        thresholdElectionBuilder.setParty(toCounterpartyRoleEnum(party));
        setValueAndUpdateMappings(synonymPath.addElement(party + "_specify"),
                value -> thresholdElectionBuilder.setOther(removeHtml(value)), mappings, path);

        Money.MoneyBuilder moneyBuilder = createiQMappingHelperUtils.getMoneyBuilder(synonymPath, party);

        if (moneyBuilder.hasData()) {
            ThresholdMinimumTransferAmountFixedAmount.ThresholdMinimumTransferAmountFixedAmountBuilder thresholdMinimumTransferAmountFixedAmountBuilder = ThresholdMinimumTransferAmountFixedAmount.builder();
            thresholdMinimumTransferAmountFixedAmountBuilder.setAmount(moneyBuilder.build());
            thresholdElectionBuilder.setFixedAmount(thresholdMinimumTransferAmountFixedAmountBuilder.build());
        }
        return thresholdElectionBuilder.hasData() ? Optional.of(thresholdElectionBuilder.build()) : Optional.empty();
    }


}
