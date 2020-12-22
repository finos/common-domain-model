package cdm.observable.asset.processor;

import cdm.observable.asset.Price;
import cdm.observable.asset.PriceTypeEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

public class PriceTypeMappingProcessor extends MappingProcessor {

	public PriceTypeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		map(parent, synonymPath, PriceTypeEnum.CASH_PRICE,"additionalPayment", "paymentAmount", "amount");
		map(parent, synonymPath, PriceTypeEnum.CASH_PRICE,"otherPartyPayment", "paymentAmount", "amount");
		map(parent, synonymPath, PriceTypeEnum.CASH_PRICE,"initialPayment", "paymentAmount", "amount");

		map(parent, synonymPath, PriceTypeEnum.PREMIUM,"premium", "paymentAmount", "amount");

		map(parent, synonymPath, PriceTypeEnum.CLEAN_PRICE,"strike", "price", "strikePrice");

		map(parent, synonymPath, PriceTypeEnum.NET_PRICE,"initialPrice", "netPrice", "amount");

		map(parent, synonymPath, PriceTypeEnum.RATE_PRICE,"fixedRateSchedule", "initialValue");
		map(parent, synonymPath, PriceTypeEnum.RATE_PRICE,"fra", "fixedRate");
		map(parent, synonymPath, PriceTypeEnum.RATE_PRICE,"fixedAmountCalculation", "fixedRate");

		map(parent, synonymPath, PriceTypeEnum.SPREAD,"floatingRateCalculation", "initialRate"); // Check with Ted
		map(parent, synonymPath, PriceTypeEnum.SPREAD,"fixedAmountCalculation", "floatingRate", "initialRate"); // Check with Ted
		map(parent, synonymPath, PriceTypeEnum.SPREAD, "spreadSchedule", "initialValue");
		map(parent, synonymPath, PriceTypeEnum.SPREAD,"strike", "spread");

		map(parent, synonymPath, PriceTypeEnum.CAP_RATE,"capRateSchedule", "initialValue");

		map(parent, synonymPath, PriceTypeEnum.FLOOR_RATE,"floorRateSchedule", "initialValue");

		map(parent, synonymPath, PriceTypeEnum.REFERENCE_PRICE,"referencePrice");
	}

	private void map(RosettaModelObjectBuilder parent, Path synonymPath, PriceTypeEnum priceTypeEnum, String... endsWith) {
		if (synonymPath.endsWith(endsWith)) {
			((Price.PriceBuilder) parent).setPriceType(priceTypeEnum);
		}
	}
}
