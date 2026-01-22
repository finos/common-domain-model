package cdm.base.staticdata.asset.common.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.meta.Reference;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappedValue;

public class FxMetaHelper {

    private final List<Mapping> mappings;

    public FxMetaHelper(List<Mapping> mappings) {
        this.mappings = mappings;
    }

    public List<Mapping> getNonReferenceMappings() {
        return mappings.stream()
                .filter(m -> !(m.getRosettaValue() instanceof Reference))
                .collect(Collectors.toList());
    }

    public Optional<Path> getCurrencySynonymPath(Path synonymPath) {
        if (synonymPath.endsWith("quoteBasis")) {
            return getQuoteBasisCurrencySynonymPath(synonymPath);
        } else if (synonymPath.endsWith("strikeQuoteBasis")) {
            return getStrikeQuoteBasisCurrencySynonymPath(synonymPath);
        } else {
            return Optional.empty();
        }
    }

    private Optional<Path> getQuoteBasisCurrencySynonymPath(Path quoteBasisPath) {
        return getNonNullMappedValue(quoteBasisPath, mappings)
                .flatMap(this::isUnderlierCurrency1)
                .map(isUnderlierCurrency1 -> {
                    Path quotedCurrencyPairPath = quoteBasisPath.getParent();
                    return isUnderlierCurrency1 ?
                            quotedCurrencyPairPath.addElement("currency1") :
                            quotedCurrencyPairPath.addElement("currency2");
                });
    }

    private Optional<Boolean> isUnderlierCurrency1(String quoteBasis) {
        return Optional.ofNullable(quoteBasis.equals("Currency2PerCurrency1") ?
                Boolean.TRUE :
                quoteBasis.equals("Currency1PerCurrency2") ?
                        false : null);
    }

    public Optional<Path> getStrikeQuoteBasisCurrencySynonymPath(Path strikeQuoteBasisPath) {
        return getNonNullMappedValue(strikeQuoteBasisPath, mappings)
                .flatMap(this::isUnderlierPutCurrency)
                .map(isUnderlierPutCurrency -> {
                    Path productPath = strikeQuoteBasisPath.getParent().getParent();
                    return isUnderlierPutCurrency ?
                            productPath.addElement("putCurrencyAmount").addElement("currency") :
                            productPath.addElement("callCurrencyAmount").addElement("currency");
                });
    }

    private Optional<Boolean> isUnderlierPutCurrency(String strikeQuoteBasis) {
        return Optional.ofNullable(strikeQuoteBasis.equals("CallCurrencyPerPutCurrency") ?
                Boolean.TRUE :
                strikeQuoteBasis.equals("PutCurrencyPerCallCurrency") ?
                        false : null);
    }

    public Optional<Path> getExchangedCurrencyPath(Path quoteBasisPath) {
        return getQuoteBasisCurrencySynonymPath(quoteBasisPath)
                .flatMap(quoteBasisCurrencySynonymPath -> getNonNullMappedValue(quoteBasisCurrencySynonymPath, mappings))
                .map(currency -> {
                    Path productPath = quoteBasisPath.getParent().getParent().getParent();

                    Path exchangedCurrency1Path = productPath.addElement("exchangedCurrency1");
                    Path exchangedCurrency1CurrencyPath = exchangedCurrency1Path.addElement("paymentAmount").addElement("currency");
                    String exchangedCurrency1Currency = getNonNullMappedValue(exchangedCurrency1CurrencyPath, mappings).orElse(null);
                    if (currency.equals(exchangedCurrency1Currency)) {
                        return exchangedCurrency1Path;
                    }
                    Path exchangedCurrency2Path = productPath.addElement("exchangedCurrency2");
                    Path exchangedCurrency2CurrencyPath = exchangedCurrency2Path.addElement("paymentAmount").addElement("currency");
                    String exchangedCurrency2Currency = getNonNullMappedValue(exchangedCurrency2CurrencyPath, mappings).orElse(null);
                    if (currency.equals(exchangedCurrency2Currency)) {
                        return exchangedCurrency2Path;
                    }
                    return null;
                });
    }
}
