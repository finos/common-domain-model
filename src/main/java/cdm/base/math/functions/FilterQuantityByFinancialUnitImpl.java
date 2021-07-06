package cdm.base.math.functions;

import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class FilterQuantityByFinancialUnitImpl extends FilterQuantityByFinancialUnit {

	@Override
	protected Quantity.QuantityBuilder doEvaluate(List<? extends Quantity> quantities, FinancialUnitEnum financialUnit) {
		return emptyIfNull(quantities).stream()
				.filter(q -> matches(q, financialUnit))
				.map(Quantity::toBuilder)
				.findFirst()
				.orElse(null);
	}

	@NotNull
	private Boolean matches(Quantity quantity, FinancialUnitEnum filterFinancialUnit) {
		return Optional.ofNullable(quantity.getUnitOfAmount())
				.map(UnitType::getFinancialUnit)
				.filter(Objects::nonNull)
				// if filterFinancialUnit is empty then do not filter
				.map(c -> Optional.ofNullable(filterFinancialUnit).map(c::equals).orElse(true))
				// add other unit type filters
				.orElse(false);
	}
}
