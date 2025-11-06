package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.MinimumTransferAmountElection;
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

public class MinimumTransferAmountElectionMappingHelper {

    private final RosettaPath path;
    private final List<Mapping> mappings;
    private final CreateiQMappingHelperUtils createiQMappingHelperUtils;

    public MinimumTransferAmountElectionMappingHelper(RosettaPath path, List<Mapping> mappings, SynonymToEnumMap synonymToEnumMap) {
        this.path = path;
        this.mappings = mappings;
        this.createiQMappingHelperUtils = new CreateiQMappingHelperUtils(path, mappings, synonymToEnumMap);
    }

    public Optional<MinimumTransferAmountElection> getMinimumTransferAmountElection(Path synonymPath, String party) {
        MinimumTransferAmountElection.MinimumTransferAmountElectionBuilder minimumTransferAmountElectionBuilder = MinimumTransferAmountElection.builder();
        Money.MoneyBuilder moneyBuilder = createiQMappingHelperUtils.getMoneyBuilder(synonymPath, party);

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
