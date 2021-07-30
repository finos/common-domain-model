package org.isda.cdm;

import cdm.base.datetime.functions.Now;
import cdm.base.datetime.functions.NowImpl;
import cdm.base.datetime.functions.Today;
import cdm.base.datetime.functions.TodayImpl;
import cdm.base.math.functions.*;
import cdm.base.staticdata.party.functions.*;
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
import cdm.observable.event.functions.ResolveObservationAverage;
import cdm.product.asset.functions.ExtractFixedLeg;
import cdm.product.asset.functions.ExtractFixedLegImpl;
import cdm.product.asset.functions.ResolveEquityInitialPrice;
import cdm.product.asset.functions.ResolveEquityInitialPriceImpl;
import cdm.product.common.schedule.functions.CalculationPeriod;
import cdm.product.common.schedule.functions.CalculationPeriodImpl;
import cdm.product.common.schedule.functions.CalculationPeriodRange;
import cdm.product.common.schedule.functions.CalculationPeriodRangeImpl;
import cdm.product.template.functions.FpmlIrd8;
import cdm.product.template.functions.FpmlIrd8Impl;
import cdm.event.common.functions.Create_BillingRecordsImpl;
import cdm.observable.event.functions.ResolveObservationAverageImpl;
import com.google.inject.AbstractModule;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.qualify.QualifyFunctionFactory;
import com.rosetta.model.lib.validation.ModelObjectValidator;
import com.rosetta.model.lib.validation.ValidatorFactory;

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
		bind(FilterPartyRole.class).to(bindFilterPartyRole());
		bind(FilterPrice.class).to(bindFilterPrice());
		bind(FilterQuantity.class).to(bindFilterQuantity());
		bind(FilterPriceQuantity.class).to(bindFilterPriceQuantity());
		bind(Now.class).to(bindNow());
		bind(Today.class).to(bindToday());
		bind(UpdateSpreadAdjustmentAndRateOptionForEachPriceQuantity.class).to(bindUpdateSpreadAdjustmentAndRateOptionForEachPriceQuantity());

		bind(Create_SplitTrades.class).to(bindCreateSplitTrades());
		bind(Create_ContractFormationPrimitives.class).to(bindCreateContractFormationPrimitives());
		bind(ExtractFixedLeg.class).to(bindExtractFixedLeg());
		bind(FilterQuantityByFinancialUnit.class).to(bindFilterQuantityByFinancialUnit());
		bind(FilterOpenTradeStates.class).to(bindFilterOpenTradeStates());
		bind(UpdateAmountForEachQuantity.class).to(bindUpdateAmountForEachQuantity());
		bind(UpdateAmountForEachMatchingQuantity.class).to(bindUpdateAmountForEachMatchingQuantity());
		bind(DeductAmountForEachMatchingQuantity.class).to(bindDeductAmountForEachMatchingQuantity());
		bind(Create_DecreasedTradeQuantityChangePrimitives.class)
				.to(bindCreateDecreasedTradeQuantityChangePrimitives());
		bind(ReplaceParty.class).to(bindReplaceParty());
		bind(Create_BillingRecords.class).to(bindCreateBillingRecords());
		bind(ResolveObservationAverage.class).to(bindResolveObservationAverage());
		bind(CalculationPeriodRange.class).to(bindCalculationPeriodRange());

	}

	protected Class<CalculationPeriodRangeImpl> bindCalculationPeriodRange() {
		return CalculationPeriodRangeImpl.class;
	}

	protected Class<ResolveObservationAverageImpl> bindResolveObservationAverage() {
		return ResolveObservationAverageImpl.class;
	}

	protected Class<? extends Create_BillingRecords> bindCreateBillingRecords() {
		return Create_BillingRecordsImpl.class;
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

	protected Class<? extends FilterPartyRole> bindFilterPartyRole() {
		return FilterPartyRoleImpl.class;
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
	}    protected Class<? extends Create_SplitTrades> bindCreateSplitTrades() {
		return Create_SplitTradesImpl.class;
	}

	protected Class<? extends Create_ContractFormationPrimitives> bindCreateContractFormationPrimitives() {
		return Create_ContractFormationPrimitivesImpl.class;
	}

	protected Class<? extends ExtractFixedLeg> bindExtractFixedLeg() {
		return ExtractFixedLegImpl.class;
	}

	protected Class<? extends FilterQuantityByFinancialUnit> bindFilterQuantityByFinancialUnit() {
		return FilterQuantityByFinancialUnitImpl.class;
	}

	protected Class<? extends FilterOpenTradeStates> bindFilterOpenTradeStates() {
		return FilterOpenTradeStatesImpl.class;
	}

	protected Class<? extends UpdateAmountForEachQuantity> bindUpdateAmountForEachQuantity() {
		return UpdateAmountForEachQuantityImpl.class;
	}

	protected Class<? extends UpdateAmountForEachMatchingQuantity> bindUpdateAmountForEachMatchingQuantity() {
		return UpdateAmountForEachMatchingQuantityImpl.class;
	}

	protected Class<? extends DeductAmountForEachMatchingQuantity> bindDeductAmountForEachMatchingQuantity() {
		return DeductAmountForEachMatchingQuantityImpl.class;
	}

	protected Class<? extends Create_DecreasedTradeQuantityChangePrimitives> bindCreateDecreasedTradeQuantityChangePrimitives() {
		return Create_DecreasedTradeQuantityChangePrimitivesImpl.class;
	}

	protected Class<? extends ReplaceParty> bindReplaceParty() {
		return ReplacePartyImpl.class;
	}
}
