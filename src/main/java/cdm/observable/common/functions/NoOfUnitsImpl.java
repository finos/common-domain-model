package cdm.observable.common.functions;

import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.observable.asset.PriceQuantity;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * Extracts the quantity amount associated with the product identifier.
 */
public class NoOfUnitsImpl extends NoOfUnits {

	@Override
	protected BigDecimal doEvaluate(List<? extends Quantity> quantity) {
		Set<BigDecimal> noOfUnits = emptyIfNull(quantity).stream()
				.filter(Objects::nonNull)
				.filter(q -> Optional.ofNullable(q)
						.map(Quantity::getUnitOfAmount)
						.map(UnitType::getFinancialUnit)
						.map(FinancialUnitEnum.SHARE::equals)
						.orElse(false))
				.map(Quantity::getAmount)
				.collect(Collectors.toSet());
		if (noOfUnits.size() > 1) {
			throw new IllegalArgumentException("Multiple Quantity instances found with unitOfAmount FinancialUnitEnum.Share, expected only one.");
		}
		return noOfUnits.stream().findFirst().orElse(null);
	}
}
