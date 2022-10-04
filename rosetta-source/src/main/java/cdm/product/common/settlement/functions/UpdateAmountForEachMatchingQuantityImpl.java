package cdm.product.common.settlement.functions;

import cdm.base.math.MeasureBase;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.QuantityChangeDirectionEnum;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import cdm.observable.asset.PriceExpression;
import cdm.observable.asset.PriceSchedule;
import cdm.observable.asset.metafields.FieldWithMetaPriceSchedule;
import cdm.product.common.settlement.PriceQuantity;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule.FieldWithMetaNonNegativeQuantityScheduleBuilder;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class UpdateAmountForEachMatchingQuantityImpl extends UpdateAmountForEachMatchingQuantity {

	@Override
	protected List<PriceQuantity.PriceQuantityBuilder> doEvaluate(List<? extends PriceQuantity> priceQuantity,
			List<? extends PriceQuantity> change,
			QuantityChangeDirectionEnum direction) {
		List<PriceQuantity.PriceQuantityBuilder> priceQuantityBuilders = emptyIfNull(priceQuantity).stream().map(PriceQuantity::toBuilder).collect(Collectors.toList());
		return update(priceQuantityBuilders, change, direction);
	}

	@NotNull
	private List<PriceQuantity.PriceQuantityBuilder> update(List<PriceQuantity.PriceQuantityBuilder> priceQuantity,
			List<? extends PriceQuantity> change,
			QuantityChangeDirectionEnum direction) {
		// Get new quantities
		Set<? extends NonNegativeQuantitySchedule> newQuantities = emptyIfNull(change).stream()
				.map(PriceQuantity::getQuantity)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.map(FieldWithMetaNonNegativeQuantitySchedule::getValue)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		// Get new prices
		Set<? extends PriceSchedule> newPrices = emptyIfNull(change).stream()
				.map(PriceQuantity::getPrice)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.map(FieldWithMetaPriceSchedule::getValue)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		// Update matching quantities and prices for each price quantity
		List<PriceQuantity.PriceQuantityBuilder> updatedPriceQuantity = emptyIfNull(priceQuantity)
				.stream()
				.map(pq -> pq
						.setQuantity(updateAmountForEachMatchingQuantity(pq.getQuantity(), newQuantities, direction))
						.setPrice(updateAmountForEachMatchingPrice(pq.getPrice(), newPrices, direction)))
				.collect(Collectors.toList());

		return updatedPriceQuantity;
	}

	@NotNull
	private List<? extends FieldWithMetaNonNegativeQuantitySchedule> updateAmountForEachMatchingQuantity(List<? extends FieldWithMetaNonNegativeQuantityScheduleBuilder> quantitiesToUpdate,
																					  Set<? extends NonNegativeQuantitySchedule> newQuantities,
																					  QuantityChangeDirectionEnum direction) {
		return emptyIfNull(quantitiesToUpdate)
				.stream()
				.filter(Objects::nonNull)
				.filter(fieldWithMeta -> fieldWithMeta.getValue() != null)
				.map(FieldWithMetaNonNegativeQuantitySchedule::toBuilder)
				.peek(quantityToUpdate ->
						filterQuantityByUnitOfAmount(newQuantities, quantityToUpdate.getValue().getUnit())
								.forEach(newQuantity ->
										updateAmount(quantityToUpdate.getValue(), newQuantity.getValue(), direction)))
				.collect(Collectors.toList());
	}

	@NotNull
	private List<? extends NonNegativeQuantitySchedule> filterQuantityByUnitOfAmount(Set<? extends NonNegativeQuantitySchedule> quantities, UnitType unitOfAmount) {
		return  Optional.ofNullable(quantities).orElseGet(HashSet::new).stream()
				.filter(quantity -> Objects.nonNull(quantity.getUnit()))
				.filter(quantity -> unitTypeEquals(quantity.getUnit(), unitOfAmount))
				.collect(Collectors.toList());
	}

	@NotNull
	private List<? extends FieldWithMetaPriceSchedule> updateAmountForEachMatchingPrice(List<? extends FieldWithMetaPriceSchedule> pricesToUpdate,
																				Set<? extends PriceSchedule> newPrices,
																				QuantityChangeDirectionEnum direction) {
		return emptyIfNull(pricesToUpdate)
				.stream()
				.filter(Objects::nonNull)
				.filter(fieldWithMeta -> fieldWithMeta.getValue() != null)
				.map(FieldWithMetaPriceSchedule::toBuilder)
				.peek(priceToUpdate ->
						filterPrice(newPrices,
								priceToUpdate.getValue().getUnit(),
								priceToUpdate.getValue().getPerUnitOf(),
								priceToUpdate.getValue().getPriceExpression())
								.forEach(matchingPrice ->
										updateAmount(priceToUpdate.getValue(), matchingPrice.getValue(), direction)))
				.collect(Collectors.toList());
	}

	@NotNull
	private List<? extends PriceSchedule> filterPrice(Set<? extends PriceSchedule> prices, UnitType unitOfAmount, UnitType perUnitOfAmount, PriceExpression priceExpression) {
		return  Optional.ofNullable(prices).orElseGet(HashSet::new).stream()
				.filter(price -> Objects.nonNull(price.getUnit()))
				.filter(price -> unitTypeEquals(price.getUnit(), unitOfAmount))
				.filter(price -> unitTypeEquals(price.getPerUnitOf(), perUnitOfAmount))
				.filter(price -> Objects.equals(price.getPriceExpression().toBuilder().prune(), priceExpression.toBuilder().prune()))
				.collect(Collectors.toList());
	}

	private <T extends MeasureBase.MeasureBaseBuilder> T updateAmount(T priceToUpdate, BigDecimal newAmount, QuantityChangeDirectionEnum direction) {
		switch (direction) {
			case DECREASE:
				return (T) priceToUpdate.setValue(priceToUpdate.getValue().subtract(newAmount));
			case REPLACE:
				return (T) priceToUpdate.setValue(newAmount);
			default:
				throw new IllegalArgumentException("Unexpected QuantityChangeDirectionEnum " + direction);
		}
	}

	/**
	 * TODO: Code generation should ignore schemes and keys
	 */
	private boolean unitTypeEquals(UnitType o1, UnitType o2) {
		UnitType.UnitTypeBuilder b1 = o1.toBuilder().prune();
		UnitType.UnitTypeBuilder b2 = o2.toBuilder().prune();

		if (!Objects.equals(b1.getCapacityUnit(), b2.getCapacityUnit())) return false;
		if (!Objects.equals(b1.getFinancialUnit(), b2.getFinancialUnit())) return false;
		if (!Objects.equals(b1.getWeatherUnit(), b2.getWeatherUnit())) return false;
		if (!Objects.equals(Optional.ofNullable(b1.getCurrency()).map(FieldWithMetaString::getValue), Optional.ofNullable(b2.getCurrency()).map(FieldWithMetaString::getValue))) return false;
		return true;
	}
}
