package cdm.observable.asset.processor;

import cdm.observable.asset.PriceTypeEnum;
import com.google.common.collect.ImmutableMap;
import com.regnosys.rosetta.common.translation.Path;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public class PriceTypeHelper {

	private static final Map<Path, PriceTypeEnum> PRICE_TYPE_ENUM_MAP = new ImmutableMap.Builder<Path, PriceTypeEnum>()
			.put(Path.parse("additionalPayment.paymentAmount.amount"), PriceTypeEnum.CASH_PRICE)
			.put(Path.parse("otherPartyPayment.paymentAmount.amount"), PriceTypeEnum.CASH_PRICE)
			.put(Path.parse("initialPayment.paymentAmount.amount"), PriceTypeEnum.CASH_PRICE)
			.put(Path.parse("premium.paymentAmount.amount"), PriceTypeEnum.PREMIUM)
			.put(Path.parse("strike.price.strikePrice"), PriceTypeEnum.CLEAN_PRICE)
			.put(Path.parse("initialPrice.netPrice.amount"), PriceTypeEnum.NET_PRICE)
			.put(Path.parse("fixedRateSchedule.initialValue"), PriceTypeEnum.RATE_PRICE)
			.put(Path.parse("fra.fixedRate"), PriceTypeEnum.RATE_PRICE)
			.put(Path.parse("fixedAmountCalculation.fixedRate"), PriceTypeEnum.RATE_PRICE)
			.put(Path.parse("floatingRateCalculation.initialRate"), PriceTypeEnum.SPREAD) // Check with Ted
			.put(Path.parse("fixedAmountCalculation.floatingRate.initialRate"), PriceTypeEnum.SPREAD) // Check with Ted
			.put(Path.parse("spreadSchedule.initialValue"), PriceTypeEnum.SPREAD)
			.put(Path.parse("strike.spread"), PriceTypeEnum.SPREAD)
			.put(Path.parse("capRateSchedule.initialValue"), PriceTypeEnum.CAP_RATE)
			.put(Path.parse("floorRateSchedule.initialValue"), PriceTypeEnum.FLOOR_RATE)
			.put(Path.parse("floatingRateMultiplierSchedule.initialValue"), PriceTypeEnum.MULTIPLIER_OF_INDEX_VALUE)
			.put(Path.parse("referencePrice"), PriceTypeEnum.REFERENCE_PRICE)
			.build();

	static Optional<PriceTypeEnum> getPriceTypeEnum(Path synonymPath) {
		return PRICE_TYPE_ENUM_MAP.entrySet().stream()
				.filter(e -> synonymPath.endsWith(toPathName(e.getKey())))
				.map(Map.Entry::getValue)
				.findFirst();
		}

	@NotNull
	private static String[] toPathName(Path path) {
		return path.getElements().stream().map(Path.PathElement::getPathName).toArray(String[]::new);
	}
}
