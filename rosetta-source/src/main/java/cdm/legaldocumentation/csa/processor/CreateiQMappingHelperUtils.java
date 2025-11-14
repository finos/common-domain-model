package cdm.legaldocumentation.csa.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import cdm.observable.asset.Money;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.translation.SynonymToEnumMap;
import com.rosetta.model.lib.path.RosettaPath;

import java.math.BigDecimal;
import java.util.List;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndUpdateMappings;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.setIsoCurrency;

public class CreateiQMappingHelperUtils {
    private final RosettaPath path;
    private final List<Mapping> mappings;
    private final SynonymToEnumMap synonymToEnumMap;

    public CreateiQMappingHelperUtils(RosettaPath path, List<Mapping> mappings, SynonymToEnumMap synonymToEnumMap) {
        this.path = path;
        this.mappings = mappings;
        this.synonymToEnumMap = synonymToEnumMap;
    }

    public Money.MoneyBuilder getMoneyBuilder(Path synonymPath, String party) {
        Money.MoneyBuilder moneyBuilder = Money.builder();

        setValueAndUpdateMappings(synonymPath.addElement(party + "_amount"),
                (value) -> moneyBuilder.setValue(new BigDecimal(value)), mappings, path);

        setValueAndOptionallyUpdateMappings(synonymPath.addElement(party + "_currency"),
                (value) -> setIsoCurrency(synonymToEnumMap.getEnumValue(ISOCurrencyCodeEnum.class, value),
                        cur -> moneyBuilder.getOrCreateUnit().setCurrency(cur)),
                mappings,
                path);
        return moneyBuilder;
    }
}
