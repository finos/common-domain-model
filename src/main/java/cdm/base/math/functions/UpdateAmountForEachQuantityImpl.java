package cdm.base.math.functions;

import cdm.base.math.Quantity;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.observable.asset.PriceQuantity;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class UpdateAmountForEachQuantityImpl extends UpdateAmountForEachQuantity {

	@Override
	protected List<PriceQuantity.PriceQuantityBuilder> doEvaluate(List<? extends PriceQuantity> priceQuantity, BigDecimal amount) {
		return emptyIfNull(priceQuantity)
						.stream()
						.map(PriceQuantity::toBuilder)
						.map(pq -> pq.setQuantityValue(updateAmountForEachQuantity(pq.getQuantity(), amount)))
						.collect(Collectors.toList());
	}

	@NotNull
	private List<? extends Quantity> updateAmountForEachQuantity(List<? extends FieldWithMetaQuantity> quantitiesToUpdate, BigDecimal newAmount) {
		return emptyIfNull(quantitiesToUpdate)
				.stream()
				.map(FieldWithMetaQuantity::getValue)
				.filter(Objects::nonNull)
				.map(Quantity::toBuilder)
				.map(quantityToUpdate -> quantityToUpdate.setAmount(newAmount))
				.collect(Collectors.toList());
	}
}
