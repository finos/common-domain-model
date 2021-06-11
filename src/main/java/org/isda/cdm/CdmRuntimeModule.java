package org.isda.cdm;

import cdm.base.datetime.functions.*;
import cdm.base.math.functions.*;
import cdm.base.staticdata.party.functions.ExtractAncillaryPartyByRole;
import cdm.base.staticdata.party.functions.ExtractAncillaryPartyByRoleImpl;
import cdm.base.staticdata.party.functions.ExtractCounterpartyByRole;
import cdm.base.staticdata.party.functions.ExtractCounterpartyByRoleImpl;
import cdm.event.common.FilterCashTransfersImpl;
import cdm.event.common.FilterSecurityTransfersImpl;
import cdm.event.common.TransfersForDateImpl;
import cdm.event.common.functions.FilterCashTransfers;
import cdm.event.common.functions.FilterSecurityTransfers;
import cdm.event.common.functions.TransfersForDate;
import cdm.legalagreement.csa.functions.SumPostedCreditSupportItemAmounts;
import cdm.legalagreement.csa.functions.SumPostedCreditSupportItemAmountsImpl;
import cdm.observable.asset.functions.FilterPrice;
import cdm.observable.asset.functions.FilterPriceImpl;
import cdm.observable.common.functions.CurrencyAmount;
import cdm.observable.common.functions.CurrencyAmountImpl;
import cdm.observable.common.functions.NoOfUnits;
import cdm.observable.common.functions.NoOfUnitsImpl;
import cdm.product.asset.functions.*;
import cdm.product.common.schedule.functions.CalculationPeriod;
import cdm.product.common.schedule.functions.CalculationPeriodImpl;
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
		bind(FpmlIrd8.class).to(bindFpmlIrd8());
		bind(ExtractCounterpartyByRole.class).to(bindExtractCounterpartyByRole());
		bind(ExtractAncillaryPartyByRole.class).to(bindExtractAncillaryPartyByRole());
		bind(FilterPrice.class).to(bindFilterPrice());
		bind(FilterQuantity.class).to(bindFilterQuantity());
		bind(Now.class).to(bindNow());
		bind(Today.class).to(bindToday());
		bind(SelectDate.class).to(bindSelectDate());
		bind(AppendDateToList.class).to(bindAppenDateToList());
		bind(AddDays.class).to(bindAddDays());
		bind(PopOffDateList.class).to(bindPopOffDateList());
		bind(RetrieveBusinessCenterHolidays.class).to(bindRetrieveBusinessCenterHolidays());
		bind(CombineBusinessCenters.class).to(bindCombineBusinessCenters());
		bind(DateDifference.class).to(bindDateDifference());
		bind(LeapYearDateDifference.class).to(bindLeapYearDateDiff());
		bind(DayOfWeek.class).to(bindDayOfWeek());
//		bind(IsHoliday.class).to(bindIsHoliday());
//		bind(IsBusinessDay.class).to(bindIsBusinessDay());
//		bind(GenerateDateList.class).to(bindGenerateDateList());

		bind(SelectScheduleStep.class).to(bindSelectScheduleStep());
		bind(SelectNonNegativeScheduleStep.class).to(bindSelectNonNegativeScheduleStep());
		bind(IndexValueObservation.class).to(bindIndexValueObservation());
		bind(IndexValueObservation.class).to(bindIndexValueObservation());
		bind(IndexValueObservationMultiple.class).to(bindIndexValueObservationMultiple());
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
	protected Class<? extends AppendToVector> bindAppendToVector() { return AppendToVectorImpl.class; }
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

	protected Class<? extends Now> bindNow() {
		return NowImpl.class;
	}
	protected Class<? extends AppendDateToList> bindAppenDateToList() {
		return AppendDateToListImpl.class;
	}
	protected Class<? extends SelectDate> bindSelectDate() {
		return SelectDateImpl.class;
	}

	protected Class<? extends DateDifference> bindDateDifference() {
		return DateDifferenceImpl.class;
	}
	protected Class<? extends LeapYearDateDifference> bindLeapYearDateDiff() { return LeapYearDateDifferenceImpl.class; }

	protected Class<? extends DayOfWeek> bindDayOfWeek() { return DayOfWeekImpl.class; }
	protected Class<? extends AddDays> bindAddDays() { return AddDaysImpl.class; }
	protected Class<? extends CombineBusinessCenters> bindCombineBusinessCenters() { return CombineBusinessCentersImpl.class; }
	protected Class<? extends PopOffDateList> bindPopOffDateList() { return PopOffDateListImpl.class; }
	protected Class<? extends RetrieveBusinessCenterHolidays> bindRetrieveBusinessCenterHolidays() { return RetrieveBusinessCenterHolidaysImpl.class; }
//	protected Class<? extends IsHoliday> bindIsHoliday() { return IsHolidayImpl.class; }
//	protected Class<? extends IsBusinessDay> bindIsBusinessDay() { return IsBusinessDayImpl.class; }
//	protected Class<? extends GenerateDateList> bindGenerateDateList() { return GenerateDateListImpl.class; }

	protected Class<? extends SelectScheduleStep> bindSelectScheduleStep() { return SelectScheduleStepImpl.class; }
	protected Class<? extends SelectNonNegativeScheduleStep> bindSelectNonNegativeScheduleStep() { return SelectNonNegativeScheduleStepImpl.class; }
	protected Class<? extends IndexValueObservation> bindIndexValueObservation() { return IndexValueObservationImpl.class; }
	protected Class<? extends IndexValueObservationMultiple> bindIndexValueObservationMultiple() { return IndexValueObservationMultipleImpl.class; }

	protected Class<? extends Today> bindToday() {
		return TodayImpl.class;
	}
}
