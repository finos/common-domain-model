package cdm.base.math.functions;

import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class FilterQuantityImpl extends FilterQuantity {

	@Override
	protected Quantity.QuantityBuilder doEvaluate(List<? extends Quantity> quantities, String currency) {
		return emptyIfNull(quantities).stream()
				.filter(q -> matches(q, currency))
				.map(Quantity::toBuilder)
				.findFirst()
				.orElse(null);
	}

	@NotNull
	private Boolean matches(Quantity quantity, String filterCurrency) {
		return Optional.ofNullable(quantity.getUnitOfAmount())
				.map(UnitType::getCurrency)
				.map(FieldWithMetaString::getValue)
				.filter(Objects::nonNull)
				// if filterCurrency is empty then do not filter
				.map(c -> Optional.ofNullable(filterCurrency).map(c::equals).orElse(true))
				// add other unit type filters
				.orElse(true);
	}
}
