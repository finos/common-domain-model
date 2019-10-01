package org.isda.cdm;

import org.isda.cdm.functions.Abs;
import org.isda.cdm.functions.AbsImpl;
import org.isda.cdm.functions.Allocate;
import org.isda.cdm.functions.AllocateImpl;
import org.isda.cdm.functions.CalculationPeriodImpl;
import org.isda.cdm.functions.EvaluatePortfolioState;
import org.isda.cdm.functions.EvaluatePortfolioStateImpl;
import org.isda.cdm.functions.ExtractQuantity;
import org.isda.cdm.functions.ExtractQuantityImpl;
import org.isda.cdm.functions.GetRateSchedule;
import org.isda.cdm.functions.GetRateScheduleImpl;
import org.isda.cdm.functions.GreaterThan;
import org.isda.cdm.functions.GreaterThanImpl;
import org.isda.cdm.functions.NewAllocationPrimitive;
import org.isda.cdm.functions.NewAllocationPrimitiveImpl;
import org.isda.cdm.functions.Plus;
import org.isda.cdm.functions.PlusImpl;
import org.isda.cdm.functions.ResolveRateIndex;
import org.isda.cdm.functions.ResolveRateIndexImpl;
import org.isda.cdm.functions.Sum;
import org.isda.cdm.functions.SumImpl;

import com.google.inject.AbstractModule;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.validation.ModelObjectValidator;

public class CdmRuntimeModule extends AbstractModule {

	@Override
	protected void configure() {
		// create bindings here
		bind(ModelObjectValidator.class).to(bindModelObjectValidator());
		bind(Abs.class).to(bindAbs());
		bind(Allocate.class).to(bindAllocate());
		bind(org.isda.cdm.functions.CalculationPeriod.class).to(bindCalculationPeriod());
//	FIXME	bind(EvaluatePortfolioState.class).to(bindEvaluatePortfolioState());
		bind(ExtractQuantity.class).to(bindExtractQuantity());
		bind(GetRateSchedule.class).to(bindGetRateSchedule());
		bind(GreaterThan.class).to(bindGreaterThan());
		bind(NewAllocationPrimitive.class).to(bindNewAllocationPrimitive());
		bind(Plus.class).to(bindPlus());
		bind(ResolveRateIndex.class).to(bindResolveRateIndex());
		bind(Sum.class).to(bindSum());
	}

	protected Class<? extends ModelObjectValidator> bindModelObjectValidator() {
		return RosettaTypeValidator.class;
	}

	// Functions
	
	protected Class<? extends Abs> bindAbs() {
		return AbsImpl.class;
	}
	protected Class<? extends Allocate> bindAllocate() {
		return AllocateImpl.class;
	}
	protected Class<? extends org.isda.cdm.functions.CalculationPeriod> bindCalculationPeriod() {
		return CalculationPeriodImpl.class;
	}
	protected Class<? extends EvaluatePortfolioState> bindEvaluatePortfolioState() {
		return EvaluatePortfolioStateImpl.class;
	}
	protected Class<? extends ExtractQuantity> bindExtractQuantity() {
		return ExtractQuantityImpl.class;
	}
	protected Class<? extends GetRateSchedule> bindGetRateSchedule() {
		return GetRateScheduleImpl.class;
	}
	protected Class<? extends GreaterThan> bindGreaterThan() {
		return GreaterThanImpl.class;
	}
	protected Class<? extends NewAllocationPrimitive> bindNewAllocationPrimitive() {
		return NewAllocationPrimitiveImpl.class;
	}
	protected Class<? extends Plus> bindPlus() {
		return PlusImpl.class;
	}
	protected Class<? extends ResolveRateIndex> bindResolveRateIndex() {
		return ResolveRateIndexImpl.class;
	}
	protected Class<? extends Sum> bindSum() {
		return SumImpl.class;
	}

}
