package cdm.product.common.settlement.functions;

import cdm.base.math.MeasureBase;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.QuantityChangeDirectionEnum;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import cdm.observable.asset.PriceComposite;
import cdm.observable.asset.PriceQuantity;
import cdm.observable.asset.PriceSchedule;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.FieldWithMetaPriceSchedule;
import com.rosetta.model.metafields.FieldWithMetaString;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule.FieldWithMetaNonNegativeQuantityScheduleBuilder;
import static cdm.observable.asset.metafields.FieldWithMetaPriceSchedule.FieldWithMetaPriceScheduleBuilder;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class UpdateAmountForEachMatchingQuantityImpl extends UpdateAmountForEachMatchingQuantity {

	@Override
	protected List<PriceQuantity.PriceQuantityBuilder> doEvaluate(List<? extends PriceQuantity> priceQuantity,
																  List<? extends PriceQuantity> change,
																  QuantityChangeDirectionEnum direction) {
		List<PriceQuantity.PriceQuantityBuilder> priceQuantityBuilders =
				emptyIfNull(priceQuantity).stream()
						.map(pq -> pq.build().toBuilder())
						.collect(Collectors.toList());
		return update(priceQuantityBuilders, change, direction);
	}

	private List<PriceQuantity.PriceQuantityBuilder> update(List<PriceQuantity.PriceQuantityBuilder> priceQuantityBuilders,
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
		emptyIfNull(priceQuantityBuilders)
				.forEach(pq -> {
					updateAmountForEachMatchingQuantity(pq.getQuantity(), newQuantities, direction);
					updateAmountForEachMatchingPrice(pq.getPrice(), newPrices, direction);
				});

		return priceQuantityBuilders;
	}

	private void updateAmountForEachMatchingQuantity(List<? extends FieldWithMetaNonNegativeQuantityScheduleBuilder> quantitiesToUpdate,
													 Set<? extends NonNegativeQuantitySchedule> newQuantities,
													 QuantityChangeDirectionEnum direction) {
		emptyIfNull(quantitiesToUpdate)
				.stream()
				.filter(Objects::nonNull)
				.map(FieldWithMetaNonNegativeQuantityScheduleBuilder::getValue)
				.filter(Objects::nonNull)
				.forEach(quantityToUpdate ->
						findQuantityByUnitOfAmount(newQuantities, quantityToUpdate.getUnit())
								.ifPresent(newQuantity -> updateAmount(quantityToUpdate, newQuantity.getValue(), direction)));
	}

	private Optional<? extends NonNegativeQuantitySchedule> findQuantityByUnitOfAmount(Set<? extends NonNegativeQuantitySchedule> quantities, UnitType unitOfAmount) {
		return Optional.ofNullable(quantities).orElseGet(HashSet::new).stream()
				.filter(quantity -> Objects.nonNull(quantity.getUnit()))
				.filter(quantity -> unitTypeEquals(quantity.getUnit(), unitOfAmount))
				.findFirst();
	}

	private void updateAmountForEachMatchingPrice(List<? extends FieldWithMetaPriceScheduleBuilder> pricesToUpdate,
												  Set<? extends PriceSchedule> newPrices,
												  QuantityChangeDirectionEnum direction) {
		emptyIfNull(pricesToUpdate)
				.stream()
				.filter(Objects::nonNull)
				.map(FieldWithMetaPriceScheduleBuilder::getValue)
				.forEach(priceToUpdate ->
						findPrice(newPrices, priceToUpdate.getUnit(), priceToUpdate.getPerUnitOf(), priceToUpdate.getPriceType(), priceToUpdate.getComposite())
								.ifPresent(matchingPrice ->
										updateAmount(priceToUpdate, matchingPrice.getValue(), direction)));
	}

	private Optional<? extends PriceSchedule> findPrice(Set<? extends PriceSchedule> prices, UnitType unitOfAmount, UnitType perUnitOfAmount, PriceTypeEnum priceTypeEnum, PriceComposite composite) {
		return Optional.ofNullable(prices).orElseGet(HashSet::new).stream()
				.filter(price -> Objects.nonNull(price.getUnit()))
				.filter(price -> unitTypeEquals(price.getUnit(), unitOfAmount))
				.filter(price -> unitTypeEquals(price.getPerUnitOf(), perUnitOfAmount))
				.filter(price -> Objects.equals(price.getPriceType(), priceTypeEnum))
				.filter(price -> {
					if (price.getComposite() == null && composite == null) {
						return true;
					}
					if (price.getComposite() == null || composite == null) {
						return false;
					}
                    return Objects.equals(price.getComposite().toBuilder().prune(), composite.toBuilder().prune());
                })
				.findFirst();
	}

	private void updateAmount(MeasureBase.MeasureBaseBuilder measureBaseToUpdate, BigDecimal newAmount, QuantityChangeDirectionEnum direction) {
		if (measureBaseToUpdate.getValue() == null) {
			return;
		}
		switch (direction) {
			case INCREASE:
				measureBaseToUpdate.setValue(measureBaseToUpdate.getValue().add(newAmount));
				break;
			case DECREASE:
				measureBaseToUpdate.setValue(measureBaseToUpdate.getValue().subtract(newAmount));
				break;
			case REPLACE:
				measureBaseToUpdate.setValue(newAmount);
				break;
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
		if (!Objects.equals(Optional.ofNullable(b1.getCurrency()).map(FieldWithMetaString::getValue), Optional.ofNullable(b2.getCurrency()).map(FieldWithMetaString::getValue)))
			return false;
		return true;
	}
}
