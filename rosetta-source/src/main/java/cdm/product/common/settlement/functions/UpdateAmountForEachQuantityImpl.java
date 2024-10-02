package cdm.product.common.settlement.functions;

import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import cdm.observable.asset.PriceQuantity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule.FieldWithMetaNonNegativeQuantityScheduleBuilder;
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

	private List<? extends NonNegativeQuantitySchedule> updateAmountForEachQuantity(List<? extends FieldWithMetaNonNegativeQuantityScheduleBuilder> quantitiesToUpdate, BigDecimal newAmount) {
		return emptyIfNull(quantitiesToUpdate)
				.stream()
				.map(FieldWithMetaNonNegativeQuantitySchedule::getValue)
				.filter(Objects::nonNull)
				.map(NonNegativeQuantitySchedule::toBuilder)
				.map(quantityToUpdate -> quantityToUpdate.setValue(newAmount))
				.collect(Collectors.toList());
	}
}
