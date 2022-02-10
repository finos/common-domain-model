package cdm.product.common.settlement.functions;

import cdm.base.math.Quantity;
import cdm.base.math.QuantityChangeDirectionEnum;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceExpression;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.FieldWithMetaPrice;
import cdm.product.common.settlement.PriceQuantity;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
		Set<? extends Quantity> newQuantities = emptyIfNull(change).stream()
				.map(PriceQuantity::getQuantity)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.map(FieldWithMetaQuantity::getValue)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		// Update matching quantities for each price quantity
		List<PriceQuantity.PriceQuantityBuilder> updatedPriceQuantity = emptyIfNull(priceQuantity)
				.stream()
				.map(pq -> pq.setQuantity(updateAmountForEachMatchingQuantity(pq.getQuantity(), newQuantities, direction)))
				.collect(Collectors.toList());
		// Get new cash price
		List<? extends Price.PriceBuilder> newCashPrice = change.stream()
				.map(PriceQuantity::getPrice)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.map(FieldWithMetaPrice::getValue)
				.filter(Objects::nonNull)
				.filter(p -> Optional.ofNullable(p.getPriceExpression())
						.map(PriceExpression::getPriceType)
						.map(PriceTypeEnum.CASH_PRICE::equals)
						.orElse(false))
				.map(Price::toBuilder)
				.collect(Collectors.toList());
		// Create new PriceQuantity with cash price, and add to list of PriceQuantity
		if (!newCashPrice.isEmpty()) {
			priceQuantity.add(PriceQuantity.builder().addPriceValue(newCashPrice));
		}

		return updatedPriceQuantity;
	}

	@NotNull
	private List<? extends FieldWithMetaQuantity> updateAmountForEachMatchingQuantity(List<? extends FieldWithMetaQuantity> quantitiesToUpdate,
			Set<? extends Quantity> newQuantities,
			QuantityChangeDirectionEnum direction) {
		return emptyIfNull(quantitiesToUpdate)
				.stream()
				.filter(Objects::nonNull)
				.filter(fieldWithMeta -> fieldWithMeta.getValue() != null)
				.map(FieldWithMetaQuantity::toBuilder)
				.peek(quantityToUpdate ->
						filterQuantityByUnitOfAmount(newQuantities, quantityToUpdate.getValue().getUnitOfAmount())
								.forEach(newQuantityWithMatchingUnitOfAmount -> updateAmount(quantityToUpdate.getValue(), newQuantityWithMatchingUnitOfAmount, direction)))
				.collect(Collectors.toList());
	}

	@NotNull
	private List<? extends Quantity> filterQuantityByUnitOfAmount(Set<? extends Quantity> quantities, UnitType unitOfAmount) {
		return  Optional.ofNullable(quantities).orElseGet(HashSet::new).stream()
				.filter(quantity -> Objects.nonNull(quantity.getUnitOfAmount()))
				.filter(quantity -> Objects.equals(quantity.getUnitOfAmount().toBuilder().prune(), unitOfAmount.toBuilder().prune()))
				.collect(Collectors.toList());
	}

	private Quantity.QuantityBuilder updateAmount(Quantity.QuantityBuilder quantityToUpdate, Quantity newQuantity, QuantityChangeDirectionEnum direction) {
		// decrease or replace
		BigDecimal newQuantityAmount = newQuantity.getAmount();
		switch (direction) {
		case DECREASE:
			return quantityToUpdate.setAmount(quantityToUpdate.getAmount().subtract(newQuantityAmount));
		case REPLACE:
			return quantityToUpdate.setAmount(newQuantityAmount);
		default:
			throw new IllegalArgumentException("Unexpected QuantityChangeDirectionEnum " + direction);
		}
	}
}
