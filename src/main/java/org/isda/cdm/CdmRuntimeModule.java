package org.isda.cdm;

import org.isda.cdm.functions.AbsImpl;
import org.isda.cdm.functions.CalculationPeriodImpl;
import org.isda.cdm.functions.CurrencyAmountImpl;
import org.isda.cdm.functions.ListsCompareImpl;
import org.isda.cdm.functions.NoOfUnitsImpl;
import org.isda.cdm.functions.ResolveContractualProduct;
import org.isda.cdm.functions.ResolveContractualProductImpl;
import org.isda.cdm.functions.ResolveEquityInitialPrice;
import org.isda.cdm.functions.ResolveEquityInitialPriceImpl;
import org.isda.cdm.functions.ResolvePayoutQuantity;
import org.isda.cdm.functions.ResolvePayoutQuantityImpl;
import org.isda.cdm.functions.SumImpl;
import org.isda.cdm.functions.SumPostedCreditSupportItemAmounts;
import org.isda.cdm.functions.SumPostedCreditSupportItemAmountsImpl;

import com.google.inject.AbstractModule;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.qualify.QualifyFunctionFactory;
import com.rosetta.model.lib.validation.ModelObjectValidator;

import cdm.base.math.functions.Abs;
import cdm.base.math.functions.ListsCompare;
import cdm.base.math.functions.RoundToNearest;
import cdm.base.math.functions.RoundToNearestImpl;
import cdm.base.math.functions.Sum;
import cdm.observable.common.functions.CurrencyAmount;
import cdm.observable.common.functions.NoOfUnits;

public class CdmRuntimeModule extends AbstractModule {

	@Override
	protected void configure() {
		// create bindings here
		bind(ModelObjectValidator.class).to(bindModelObjectValidator());
		bind(QualifyFunctionFactory.class).to(bindQualifyFunctionFactory());

		// functions
		bind(Abs.class).to(bindAbs());
		bind(org.isda.cdm.functions.CalculationPeriod.class).to(bindCalculationPeriod());
		bind(Sum.class).to(bindSum());
		bind(ListsCompare.class).to(bindListsCompare());
		bind(ResolvePayoutQuantity.class).to(bindResolvePayoutQuantity());
		bind(ResolveContractualProduct.class).to(bindResolveContractualProduct());
		bind(ResolveEquityInitialPrice.class).to(bindResolveEquityInitialPrice());
		bind(NoOfUnits.class).to(bindNoOfUnits());
		bind(CurrencyAmount.class).to(bindCurrencyAmount());
		bind(SumPostedCreditSupportItemAmounts.class).to(bindSumPostedCreditSupportItemAmounts());
		bind(RoundToNearest.class).to(bindRoundToNearest());
		bind(FpmlIrd8.class).to(bindFpmlIrd8());
	}

	protected Class<? extends ListsCompare> bindListsCompare() {
		return ListsCompareImpl.class;
	}

	protected Class<? extends ModelObjectValidator> bindModelObjectValidator() {
		return RosettaTypeValidator.class;
	}

	protected Class<? extends QualifyFunctionFactory> bindQualifyFunctionFactory() {
		return QualifyFunctionFactory.Default.class;
	}

	// Functions

	protected Class<? extends Abs> bindAbs() {
		return AbsImpl.class;
	}

	protected Class<? extends org.isda.cdm.functions.CalculationPeriod> bindCalculationPeriod() {
		return CalculationPeriodImpl.class;
	}

	protected Class<? extends Sum> bindSum() {
		return SumImpl.class;
	}

	protected Class<? extends ResolvePayoutQuantity> bindResolvePayoutQuantity() {
		return ResolvePayoutQuantityImpl.class;
	}

	protected Class<? extends ResolveContractualProduct> bindResolveContractualProduct() {
		return ResolveContractualProductImpl.class;
	}

	protected Class<? extends ResolveEquityInitialPrice> bindResolveEquityInitialPrice() {
		return ResolveEquityInitialPriceImpl.class;
	}

	protected Class<? extends NoOfUnits> bindNoOfUnits() {
		return NoOfUnitsImpl.class;
	}

	protected Class<? extends CurrencyAmount> bindCurrencyAmount() {
		return CurrencyAmountImpl.class;
	}

	protected Class<? extends SumPostedCreditSupportItemAmounts> bindSumPostedCreditSupportItemAmounts() {
		return SumPostedCreditSupportItemAmountsImpl.class;
	}

	protected Class<? extends RoundToNearest> bindRoundToNearest() {
		return RoundToNearestImpl.class;
	}

	protected Class<? extends FpmlIrd8> bindFpmlIrd8() {
		return FpmlIrd8Impl.class;
	}
}
