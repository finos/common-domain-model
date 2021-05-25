package org.isda.cdm;

import cdm.base.datetime.functions.Now;
import cdm.base.datetime.functions.NowImpl;
import cdm.base.datetime.functions.Today;
import cdm.base.datetime.functions.TodayImpl;
import cdm.base.math.functions.*;
import cdm.base.staticdata.party.functions.ExtractAncillaryPartyByRole;
import cdm.base.staticdata.party.functions.ExtractAncillaryPartyByRoleImpl;
import cdm.base.staticdata.party.functions.ExtractCounterpartyByRole;
import cdm.base.staticdata.party.functions.ExtractCounterpartyByRoleImpl;
import cdm.event.common.functions.*;
import cdm.legalagreement.csa.functions.SumPostedCreditSupportItemAmounts;
import cdm.legalagreement.csa.functions.SumPostedCreditSupportItemAmountsImpl;
import cdm.observable.asset.functions.FilterPrice;
import cdm.observable.asset.functions.FilterPriceImpl;
import cdm.observable.asset.functions.FilterPriceQuantity;
import cdm.observable.asset.functions.FilterPriceQuantityImpl;
import cdm.observable.common.functions.CurrencyAmount;
import cdm.observable.common.functions.CurrencyAmountImpl;
import cdm.observable.common.functions.NoOfUnits;
import cdm.observable.common.functions.NoOfUnitsImpl;
import cdm.product.asset.functions.ResolveEquityInitialPrice;
import cdm.product.asset.functions.ResolveEquityInitialPriceImpl;
import cdm.product.common.schedule.functions.CalculationPeriod;
import cdm.product.common.schedule.functions.CalculationPeriodImpl;
import cdm.product.template.functions.FpmlIrd8;
import cdm.product.template.functions.FpmlIrd8Impl;
import com.google.inject.AbstractModule;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.qualify.QualifyFunctionFactory;
import com.rosetta.model.lib.validation.ModelObjectValidator;
import com.rosetta.model.lib.validation.ValidatorFactory;
import org.jetbrains.annotations.NotNull;

public class CdmRuntimeModule extends AbstractModule {

	@Override
	protected void configure() {
		// create bindings here
		bind(ModelObjectValidator.class).to(bindModelObjectValidator());
		bind(QualifyFunctionFactory.class).to(bindQualifyFunctionFactory());
		bind(ValidatorFactory.class).to(bindValidatorFactory());

		// functions
		bind(Abs.class).to(bindAbs());
		bind(CalculationPeriod.class).to(bindCalculationPeriod());
		bind(Sum.class).to(bindSum());
		bind(ListsCompare.class).to(bindListsCompare());
		bind(ResolveEquityInitialPrice.class).to(bindResolveEquityInitialPrice());
		bind(NoOfUnits.class).to(bindNoOfUnits());
		bind(CurrencyAmount.class).to(bindCurrencyAmount());
		bind(TransfersForDate.class).to(bindTransfersForDate());
		bind(FilterCashTransfers.class).to(bindFilterCashTransfers());
		bind(FilterSecurityTransfers.class).to(bindFilterSecurityTransfers());
		bind(SumPostedCreditSupportItemAmounts.class).to(bindSumPostedCreditSupportItemAmounts());
		bind(RoundToNearest.class).to(bindRoundToNearest());
		bind(FpmlIrd8.class).to(bindFpmlIrd8());
		bind(ExtractCounterpartyByRole.class).to(bindExtractCounterpartyByRole());
		bind(ExtractAncillaryPartyByRole.class).to(bindExtractAncillaryPartyByRole());
		bind(FilterPrice.class).to(bindFilterPrice());
		bind(FilterQuantity.class).to(bindFilterQuantity());
		bind(FilterPriceQuantity.class).to(bindFilterPriceQuantity());
		bind(Now.class).to(bindNow());
		bind(Today.class).to(bindToday());
		bind(UpdateSpreadAdjustmentAndRateOptionForEachPriceQuantity.class).to(bindUpdateSpreadAdjustmentAndRateOptionForEachPriceQuantity());
	}

	protected Class<? extends FilterSecurityTransfers> bindFilterSecurityTransfers() {
		return FilterSecurityTransfersImpl.class;
	}

	protected Class<? extends FilterCashTransfers> bindFilterCashTransfers() {
		return FilterCashTransfersImpl.class;
	}

	protected Class<? extends TransfersForDate> bindTransfersForDate() {
		return TransfersForDateImpl.class;
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

	protected Class<? extends ValidatorFactory> bindValidatorFactory() {
		return ValidatorFactory.Default.class;
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

	protected Class<? extends ExtractCounterpartyByRole> bindExtractCounterpartyByRole() {
		return ExtractCounterpartyByRoleImpl.class;
	}

	protected Class<? extends ExtractAncillaryPartyByRole> bindExtractAncillaryPartyByRole() {
		return ExtractAncillaryPartyByRoleImpl.class;
	}

	protected Class<? extends FilterPrice> bindFilterPrice() {
		return FilterPriceImpl.class;
	}

	protected Class<? extends FilterQuantity> bindFilterQuantity() {
		return FilterQuantityImpl.class;
	}

	protected Class<? extends FilterPriceQuantity> bindFilterPriceQuantity() {
		return FilterPriceQuantityImpl.class;
	}

	protected Class<? extends Now> bindNow() {
		return NowImpl.class;
	}

	protected Class<? extends Today> bindToday() {
		return TodayImpl.class;
	}

	protected Class<UpdateSpreadAdjustmentAndRateOptionForEachPriceQuantityImpl> bindUpdateSpreadAdjustmentAndRateOptionForEachPriceQuantity() {
		return UpdateSpreadAdjustmentAndRateOptionForEachPriceQuantityImpl.class;
	}
}
