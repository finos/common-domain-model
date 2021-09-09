package org.isda.cdm;

import cdm.base.datetime.functions.*;
import cdm.base.math.functions.*;
import cdm.base.staticdata.party.functions.*;
import cdm.event.common.functions.*;
import cdm.legalagreement.csa.functions.SumPostedCreditSupportItemAmounts;
import cdm.legalagreement.csa.functions.SumPostedCreditSupportItemAmountsImpl;
import cdm.observable.asset.functions.FilterPrice;
import cdm.observable.asset.functions.FilterPriceImpl;
import cdm.observable.common.functions.CurrencyAmount;
import cdm.observable.common.functions.CurrencyAmountImpl;
import cdm.observable.common.functions.NoOfUnits;
import cdm.observable.common.functions.NoOfUnitsImpl;
import cdm.observable.event.functions.ResolveObservationAverage;
import cdm.observable.event.functions.ResolveObservationAverageImpl;
import cdm.product.asset.calculation.functions.SelectNonNegativeScheduleStep;
import cdm.product.asset.calculation.functions.SelectNonNegativeScheduleStepImpl;
import cdm.product.asset.floatingrate.functions.SelectScheduleStep;
import cdm.product.asset.floatingrate.functions.SelectScheduleStepImpl;
import cdm.observable.asset.fro.functions.*;
import cdm.product.asset.functions.ExtractFixedLeg;
import cdm.product.asset.functions.ExtractFixedLegImpl;
import cdm.product.asset.functions.ResolveEquityInitialPrice;
import cdm.product.asset.functions.ResolveEquityInitialPriceImpl;
import cdm.product.common.schedule.functions.CalculationPeriod;
import cdm.product.common.schedule.functions.CalculationPeriodImpl;
import cdm.product.common.schedule.functions.CalculationPeriodRange;
import cdm.product.common.schedule.functions.CalculationPeriodRangeImpl;
import cdm.product.common.settlement.functions.*;
import cdm.product.template.functions.FpmlIrd8;
import cdm.product.template.functions.FpmlIrd8Impl;
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
		bind(AppendToVector.class).to(bindAppendToVector());
		bind(SelectFromVector.class).to(bindSelectFromVector());
		bind(LastInVector.class).to(bindLastInVector());
		bind(ListsCompare.class).to(bindListsCompare());
		bind(VectorOperation.class).to(bindVectorOperation());
		bind(VectorScalarOperation.class).to(bindVectorScalarOperation());
		bind(VectorGrowthOperation.class).to(bindVectorGrowthOperation());

		bind(ResolveEquityInitialPrice.class).to(bindResolveEquityInitialPrice());
		bind(NoOfUnits.class).to(bindNoOfUnits());
		bind(CurrencyAmount.class).to(bindCurrencyAmount());
		bind(TransfersForDate.class).to(bindTransfersForDate());
		bind(FilterCashTransfers.class).to(bindFilterCashTransfers());
		bind(FilterSecurityTransfers.class).to(bindFilterSecurityTransfers());
		bind(SumPostedCreditSupportItemAmounts.class).to(bindSumPostedCreditSupportItemAmounts());
		bind(RoundToNearest.class).to(bindRoundToNearest());
		bind(RoundToPrecision.class).to(bindRoundToPrecision());
		bind(FpmlIrd8.class).to(bindFpmlIrd8());
		bind(ExtractCounterpartyByRole.class).to(bindExtractCounterpartyByRole());
		bind(ExtractAncillaryPartyByRole.class).to(bindExtractAncillaryPartyByRole());
		bind(FilterPartyRole.class).to(bindFilterPartyRole());
		bind(FilterPrice.class).to(bindFilterPrice());
		bind(FilterQuantity.class).to(bindFilterQuantity());
		bind(FilterPriceQuantity.class).to(bindFilterPriceQuantity());
		bind(Now.class).to(bindNow());
		bind(Today.class).to(bindToday());
		bind(SelectDate.class).to(bindSelectDate());
		bind(AppendDateToList.class).to(bindAppendDateToList());
		bind(LastInDateList.class).to(bindLastInDateList());
		bind(AddDays.class).to(bindAddDays());
		bind(PopOffDateList.class).to(bindPopOffDateList());
		bind(BusinessCenterHolidays.class).to(bindBusinessCenterHolidays());
		bind(BusinessCenterHolidaysDataProvider.class).to(bindBusinessCenterHolidaysDataProvider()).asEagerSingleton();
		bind(CombineBusinessCenters.class).to(bindCombineBusinessCenters());
		bind(DateDifference.class).to(bindDateDifference());
		bind(LeapYearDateDifference.class).to(bindLeapYearDateDiff());
		bind(DayOfWeek.class).to(bindDayOfWeek());
		bind(SelectScheduleStep.class).to(bindSelectScheduleStep());
		bind(SelectNonNegativeScheduleStep.class).to(bindSelectNonNegativeScheduleStep());
		bind(IndexValueObservationDataProvider.class).to(bindIndexValueObservationDataProvider()).asEagerSingleton();
		bind(IndexValueObservation.class).to(bindIndexValueObservation());
		bind(IndexValueObservationMultiple.class).to(bindIndexValueObservationMultiple());
		bind(UpdateSpreadAdjustmentAndRateOptionForEachPriceQuantity.class).to(bindUpdateSpreadAdjustmentAndRateOptionForEachPriceQuantity());

		bind(Create_SplitTrades.class).to(bindCreateSplitTrades());
		bind(Create_ContractFormationPrimitives.class).to(bindCreateContractFormationPrimitives());
		bind(ExtractFixedLeg.class).to(bindExtractFixedLeg());
		bind(FilterQuantityByFinancialUnit.class).to(bindFilterQuantityByFinancialUnit());
		bind(FilterOpenTradeStates.class).to(bindFilterOpenTradeStates());
		bind(UpdateAmountForEachQuantity.class).to(bindUpdateAmountForEachQuantity());
		bind(UpdateAmountForEachMatchingQuantity.class).to(bindUpdateAmountForEachMatchingQuantity());
		bind(DeductAmountForEachMatchingQuantity.class).to(bindDeductAmountForEachMatchingQuantity());
		bind(Create_DecreasedTradeQuantityChangePrimitives.class).to(bindCreateDecreasedTradeQuantityChangePrimitives());
		bind(ReplaceParty.class).to(bindReplaceParty());
		bind(Create_BillingRecords.class).to(bindCreateBillingRecords());
		bind(ResolveObservationAverage.class).to(bindResolveObservationAverage());
		bind(CalculationPeriodRange.class).to(bindCalculationPeriodRange());

	}

	protected Class<? extends CalculationPeriodRange> bindCalculationPeriodRange() {
		return CalculationPeriodRangeImpl.class;
	}

	protected Class<? extends ResolveObservationAverage> bindResolveObservationAverage() {
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

	protected Class<? extends SelectFromVector> bindSelectFromVector() {
		return SelectFromVectorImpl.class;
	}

	protected Class<? extends LastInVector> bindLastInVector() {
		return LastInVectorImpl.class;
	}

	protected Class<? extends AppendToVector> bindAppendToVector() {
		return AppendToVectorImpl.class;
	}

	protected Class<? extends VectorOperation> bindVectorOperation() {
		return VectorOperationImpl.class;
	}

	protected Class<? extends VectorScalarOperation> bindVectorScalarOperation() {
		return VectorScalarOperationImpl.class;
	}

	protected Class<? extends VectorGrowthOperation> bindVectorGrowthOperation() {
		return VectorGrowthOperationImpl.class;
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

	protected Class<? extends RoundToPrecision> bindRoundToPrecision() {
		return RoundToPrecisionImpl.class;
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

	protected Class<? extends SelectDate> bindSelectDate() {
		return SelectDateImpl.class;
	}

	protected Class<? extends AppendDateToList> bindAppendDateToList() {
		return AppendDateToListImpl.class;
	}

	protected Class<? extends LastInDateList> bindLastInDateList() {
		return LastInDateListImpl.class;
	}

	protected Class<? extends AddDays> bindAddDays() {
		return AddDaysImpl.class;
	}

	protected Class<? extends PopOffDateList> bindPopOffDateList() {
		return PopOffDateListImpl.class;
	}

	protected Class<? extends BusinessCenterHolidays> bindBusinessCenterHolidays() {
		return BusinessCenterHolidaysImpl.class;
	}

	protected Class<? extends CombineBusinessCenters> bindCombineBusinessCenters() {
		return CombineBusinessCentersImpl.class;
	}

	protected Class<? extends DateDifference> bindDateDifference() {
		return DateDifferenceImpl.class;
	}

	protected Class<? extends LeapYearDateDifference> bindLeapYearDateDiff() {
		return LeapYearDateDifferenceImpl.class;
	}

	protected Class<? extends DayOfWeek> bindDayOfWeek() {
		return DayOfWeekImpl.class;
	}

	protected Class<? extends SelectScheduleStep> bindSelectScheduleStep() {
		return SelectScheduleStepImpl.class;
	}

	protected Class<? extends SelectNonNegativeScheduleStep> bindSelectNonNegativeScheduleStep() {
		return SelectNonNegativeScheduleStepImpl.class;
	}

	protected Class<? extends IndexValueObservationDataProvider> bindIndexValueObservationDataProvider() {
		return IndexValueObservationEmptyDataProviderImpl.class;
	}

	protected Class<? extends IndexValueObservation> bindIndexValueObservation() {
		return IndexValueObservationImpl.class;
	}

	protected Class<? extends IndexValueObservationMultiple> bindIndexValueObservationMultiple() {
		return IndexValueObservationMultipleImpl.class;
	}

	protected Class<? extends BusinessCenterHolidaysDataProvider> bindBusinessCenterHolidaysDataProvider() {
		return BusinessCenterHolidaysEmptyDataProviderImpl.class;
	}

	protected Class<? extends Today> bindToday() {
		return TodayImpl.class;
	}

	protected Class<? extends UpdateSpreadAdjustmentAndRateOptionForEachPriceQuantity> bindUpdateSpreadAdjustmentAndRateOptionForEachPriceQuantity() {
		return UpdateSpreadAdjustmentAndRateOptionForEachPriceQuantityImpl.class;
	}

	protected Class<? extends Create_SplitTrades> bindCreateSplitTrades() {
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
