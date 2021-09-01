package cdm.base.math.functions;

import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class DeductAmountForEachMatchingQuantityImpl extends DeductAmountForEachMatchingQuantity {


	protected List<Quantity.QuantityBuilder> doEvaluate(List<? extends Quantity> quantity, List<? extends Quantity> quantityDeducted) {
		List<Quantity.QuantityBuilder> collect = emptyIfNull(quantity)
				.stream()
				.filter(Objects::nonNull)
				.map(Quantity::toBuilder)
				.map(quantityToUpdate -> {
					filterQuantityByUnitOfAmount(quantityDeducted, quantityToUpdate.getUnitOfAmount())
							.ifPresent(filteredQuantity -> quantityToUpdate.setAmount(quantityToUpdate.getAmount().subtract(filteredQuantity.getAmount())));
					return quantityToUpdate;
				})
				.collect(Collectors.toList());
		return collect;
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
