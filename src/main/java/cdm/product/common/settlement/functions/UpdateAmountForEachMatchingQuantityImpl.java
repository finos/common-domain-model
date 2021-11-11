package cdm.product.common.settlement.functions;

import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.product.common.settlement.PriceQuantity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class UpdateAmountForEachMatchingQuantityImpl extends UpdateAmountForEachMatchingQuantity {

	@Override
	protected List<PriceQuantity.PriceQuantityBuilder> doEvaluate(List<? extends PriceQuantity> priceQuantity, List<? extends Quantity> quantity) {
		return emptyIfNull(priceQuantity)
						.stream()
						.map(PriceQuantity::toBuilder)
						.map(pq -> pq.setQuantityValue(updateAmountForEachMatchingQuantity(pq.getQuantity(), quantity)))
						.collect(Collectors.toList());
	}

	@NotNull
	private List<? extends Quantity> updateAmountForEachMatchingQuantity(List<? extends FieldWithMetaQuantity> quantitiesToUpdate, List<? extends Quantity> newQuantityAmounts) {
		return emptyIfNull(quantitiesToUpdate)
				.stream()
				.map(FieldWithMetaQuantity::getValue)
				.filter(Objects::nonNull)
				.map(Quantity::toBuilder)
				.peek(quantityToUpdate -> filterQuantityByUnitOfAmount(newQuantityAmounts, quantityToUpdate.getUnitOfAmount())
						.forEach(filteredQuantity -> quantityToUpdate.setAmount(filteredQuantity.getAmount())))
				.collect(Collectors.toList());
	}

	@NotNull
	private List<? extends Quantity> filterQuantityByUnitOfAmount(List<? extends Quantity> quantities, UnitType unitOfAmount) {
		return emptyIfNull(quantities)
				.stream()
				.filter(quantity -> Objects.nonNull(quantity.getUnitOfAmount()))
				.filter(quantity -> Objects.equals(quantity.getUnitOfAmount().toBuilder().prune(), unitOfAmount.toBuilder().prune()))
				.collect(Collectors.toList());

	}
}
