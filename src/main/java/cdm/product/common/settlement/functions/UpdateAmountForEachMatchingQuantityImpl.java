package cdm.product.common.settlement.functions;

import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.product.common.settlement.PriceQuantitySettlement;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class UpdateAmountForEachMatchingQuantityImpl extends UpdateAmountForEachMatchingQuantity {

	@Override
	protected  List<PriceQuantitySettlement.PriceQuantitySettlementBuilder> doEvaluate(List<? extends PriceQuantitySettlement> priceQuantitySettlement, List<? extends Quantity> quantity) {
		return emptyIfNull(priceQuantitySettlement)
						.stream()
						.map(PriceQuantitySettlement::toBuilder)
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
				.map(quantityToUpdate -> {
					filterQuantityByUnitOfAmount(newQuantityAmounts, quantityToUpdate.getUnitOfAmount())
							.ifPresent(filteredQuantity -> quantityToUpdate.setAmount(filteredQuantity.getAmount()));
					return quantityToUpdate;
				})
				.collect(Collectors.toList());
	}

	@NotNull
	private Optional<? extends Quantity> filterQuantityByUnitOfAmount(List<? extends Quantity> quantities, UnitType unitOfAmount) {
		List<? extends Quantity> filteredQuantity = emptyIfNull(quantities)
				.stream()
				.filter(quantity -> Objects.nonNull(quantity.getUnitOfAmount()))
				.filter(quantity -> Objects.equals(quantity.getUnitOfAmount().toBuilder().prune(), unitOfAmount.toBuilder().prune()))
				.collect(Collectors.toList());

		if (filteredQuantity.isEmpty()) {
			return Optional.empty();
		} else if (filteredQuantity.size() == 1) {
			return Optional.of(filteredQuantity.get(0));
		} else {
			throw new IllegalArgumentException("Multiple quantities with same unit of amount " + unitOfAmount);
		}
	}
}
