package org.isda.cdm;

import cdm.base.math.functions.*;
import cdm.base.staticdata.party.functions.ExtractCounterpartyBySide;
import cdm.base.staticdata.party.functions.ExtractCounterpartyBySideImpl;
import cdm.base.staticdata.party.functions.ExtractRelatedParty;
import cdm.base.staticdata.party.functions.ExtractRelatedPartyImpl;
import cdm.legalagreement.csa.functions.SumPostedCreditSupportItemAmounts;
import cdm.legalagreement.csa.functions.SumPostedCreditSupportItemAmountsImpl;
import cdm.observable.common.functions.CurrencyAmount;
import cdm.observable.common.functions.CurrencyAmountImpl;
import cdm.observable.common.functions.NoOfUnits;
import cdm.observable.common.functions.NoOfUnitsImpl;
import cdm.product.asset.functions.ResolveEquityInitialPrice;
import cdm.product.asset.functions.ResolveEquityInitialPriceImpl;
import cdm.product.common.functions.ResolveContractualProduct;
import cdm.product.common.functions.ResolveContractualProductImpl;
import cdm.product.common.functions.ResolvePayoutQuantity;
import cdm.product.common.functions.ResolvePayoutQuantityImpl;
import cdm.product.common.schedule.functions.CalculationPeriod;
import cdm.product.common.schedule.functions.CalculationPeriodImpl;
import cdm.product.template.functions.FpmlIrd8;
import cdm.product.template.functions.FpmlIrd8Impl;
import com.google.inject.AbstractModule;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.qualify.QualifyFunctionFactory;
import com.rosetta.model.lib.validation.ModelObjectValidator;

public class CdmRuntimeModule extends AbstractModule {

	@Override
	protected void configure() {
		// create bindings here
		bind(ModelObjectValidator.class).to(bindModelObjectValidator());
		bind(QualifyFunctionFactory.class).to(bindQualifyFunctionFactory());

		// functions
		bind(Abs.class).to(bindAbs());
		bind(CalculationPeriod.class).to(bindCalculationPeriod());
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
		bind(ExtractCounterpartyBySide.class).to(bindExtractCounterpartyBySide());
		bind(ExtractRelatedParty.class).to(bindExtractRelatedParty());
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

	protected Class<? extends CalculationPeriod> bindCalculationPeriod() {
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

	protected Class<? extends ExtractCounterpartyBySide> bindExtractCounterpartyBySide() {
		return ExtractCounterpartyBySideImpl.class;
	}

	protected Class<? extends ExtractRelatedParty> bindExtractRelatedParty() {
		return ExtractRelatedPartyImpl.class;
	}
}
