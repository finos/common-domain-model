package org.isda.cdm.functions.testing;

import cdm.base.datetime.Period;
import cdm.base.datetime.PeriodEnum;
import cdm.base.math.UnitType;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.base.staticdata.asset.rates.metafields.FieldWithMetaFloatingRateIndexEnum;
import cdm.event.common.BusinessEvent;
import cdm.event.common.IndexTransitionInstruction;
import cdm.event.common.Trade;
import cdm.event.common.TradeState;
import cdm.event.common.functions.Create_IndexTransition;
import cdm.observable.asset.*;
import cdm.product.common.ProductIdentification;
import cdm.product.common.settlement.PriceQuantity;
import cdm.product.template.ContractualProduct;
import cdm.product.template.Product;
import cdm.product.template.TradableProduct;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import com.rosetta.model.metafields.MetaFields;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Optional;

public class RunCreateIndexTransition  implements ExecutableFunction<TradeState, BusinessEvent> {

	@Inject
	Create_IndexTransition func;

	@Override
	public BusinessEvent execute(TradeState tradeState) {
		if (isProductQualifier(tradeState, "InterestRate_CrossCurrency_Basis")) {
			IndexTransitionInstruction instruction = IndexTransitionInstruction.builder()
					.addPriceQuantity(PriceQuantity.builder()
							.setObservable(Observable.builder()
									.setRateOptionValue(FloatingRateOption.builder()
											.setFloatingRateIndex(FieldWithMetaFloatingRateIndexEnum.builder()
													.setValue(FloatingRateIndexEnum.USD_LIBOR_ISDA)
													.setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/floating-rate-index")))
											.setIndexTenor(Period.builder()
													.setPeriod(PeriodEnum.M)
													.setPeriodMultiplier(3))))
							.addPriceValue(Price.builder()
									.setAmount(BigDecimal.valueOf(0.002))
									.setUnitOfAmount(UnitType.builder().setCurrencyValue("USD"))
									.setPerUnitOfAmount(UnitType.builder().setCurrencyValue("USD"))
									.setPriceExpression(PriceExpression.builder()
											.setPriceType(PriceTypeEnum.INTEREST_RATE)
											.setSpreadType(SpreadTypeEnum.SPREAD))))
					.addPriceQuantity(PriceQuantity.builder()
							.setObservable(Observable.builder()
									.setRateOptionValue(FloatingRateOption.builder()
											.setFloatingRateIndex(
													FieldWithMetaFloatingRateIndexEnum.builder()
															.setValue(FloatingRateIndexEnum.EUR_EURIBOR_REUTERS)
															.setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/floating-rate-index")))
											.setIndexTenor(Period.builder()
													.setPeriod(PeriodEnum.M)
													.setPeriodMultiplier(3))))
							.addPriceValue(Price.builder()
									.setAmount(BigDecimal.valueOf(0.001))
									.setUnitOfAmount(UnitType.builder().setCurrencyValue("EUR"))
									.setPerUnitOfAmount(UnitType.builder().setCurrencyValue("EUR"))
									.setPriceExpression(PriceExpression.builder()
											.setPriceType(PriceTypeEnum.INTEREST_RATE)
											.setSpreadType(SpreadTypeEnum.SPREAD))))
					.setEffectiveDate(DateImpl.of(2018, 6, 19));
			Date date = DateImpl.of(2018, 6, 17);
			return func.evaluate(tradeState, instruction, date);
		}
		else if (isProductQualifier(tradeState, "InterestRate_IRSwap_FixedFloat")) {
			IndexTransitionInstruction instruction = IndexTransitionInstruction.builder()
					.addPriceQuantity(PriceQuantity.builder()
							.setObservable(Observable.builder()
									.setRateOptionValue(FloatingRateOption.builder()
											.setFloatingRateIndexValue(FloatingRateIndexEnum.EUR_EURIBOR_REUTERS)
											.setIndexTenor(Period.builder()
													.setPeriod(PeriodEnum.M)
													.setPeriodMultiplier(6))))
							.addPriceValue(Price.builder()
									.setAmount(BigDecimal.valueOf(0.003))
									.setUnitOfAmount(UnitType.builder().setCurrencyValue("EUR"))
									.setPerUnitOfAmount(UnitType.builder().setCurrencyValue("EUR"))
									.setPriceExpression(PriceExpression.builder()
											.setPriceType(PriceTypeEnum.INTEREST_RATE)
											.setSpreadType(SpreadTypeEnum.SPREAD))))
					.setEffectiveDate(DateImpl.of(2000, 10, 3));
			Date date = DateImpl.of(2000, 10, 1);
			return func.evaluate(tradeState, instruction, date);
		}
		else {
			throw new IllegalArgumentException("Unsupported product");
		}
	}

	private boolean isProductQualifier(TradeState tradeState, String qualifier) {
		return Optional.ofNullable(tradeState)
				.map(TradeState::getTrade)
				.map(Trade::getTradableProduct)
				.map(TradableProduct::getProduct)
				.map(Product::getContractualProduct)
				.map(ContractualProduct::getProductIdentification)
				.map(ProductIdentification::getProductQualifier)
				.map(qualifier::equals)
				.orElse(false);
	}

	@Override
	public Class<TradeState> getInputType() {
		return TradeState.class;
	}

	@Override
	public Class<BusinessEvent> getOutputType() {
		return BusinessEvent.class;
	}
}
